## Project Overview

A Kotlin Multiplatform (KMP) app targeting **Android, iOS, Desktop (JVM), and Web (Kotlin/JS +
WASM)**, with Jetpack Compose Multiplatform for shared UI. Clean Architecture + **MVVM**.

**What it does:** a WeTransfer-style photo-sharing app that **clusters event photos by face** and
generates a **shareable per-person link**, so each recipient sees and downloads only the photos
they appear in. Recipients need no account — they open a link and view their gallery. Face
detection, storage, and link enforcement are server-side; the client talks only to the backend API.

### Stack / Patterns
- **Overall**: Clean Architecture, package-based separation (not Gradle modules)
- **Presentation**: MVVM — ViewModels expose `StateFlow<ScreenState<T>>`; screens observe
- **UI**: Compose Multiplatform
- **DI**: Koin
- **Navigation**: JetBrains Compose Navigation, nested graphs (a `navigation/` package, not a module)
- **Network**: Ktorfit for JSON REST; raw Ktor `HttpClient` for upload/streaming (see Network Layer)
- **Local storage**: DataStore (preferences). Add Room only if offline caching becomes a real need.
- **Logging**: Kermit, used directly
---

## Module Structure

Keep it flat. All product code lives in `cmp-shared`; the platform entry points are the only
other modules. There are no per-feature or per-core Gradle modules — separation is by package.

```
my-app/
├── cmp-android/          # Android entry point
├── cmp-ios/              # iOS entry point (CocoaPods)
├── cmp-desktop/          # Desktop entry point
├── cmp-web/              # Web entry point (WASM) — primary recipient surface
└── cmp-shared/           # All product code lives here
    └── src/commonMain/kotlin/
        ├── core/
        │   ├── model/        # Domain data classes only, no platform deps
        │   ├── common/       # ScreenState + shared utilities
        │   ├── network/      # Ktor/Ktorfit services + UploadService
        │   └── data/         # Repositories (bridge network ↔ UI)
        ├── designsystem/     # Theme, typography, color tokens
        ├── navigation/       # ROOT graph assembly — calls each feature's graph extension
        └── feature/
            ├── auth/         # Host login / registration
            ├── upload/       # Bulk photo upload + progress
            ├── dashboard/    # Cluster grid, rename, configure sharing
            ├── links/        # Link management: deactivate, regenerate, expiry
            └── gallery/      # Recipient view — no auth dependency (see below)
```

Navigation is a **package**, not a separate module. In a flat project there's no module boundary
to form a circular dependency across, and the graph-extension pattern below keeps the dependency
one-way regardless. Platform entry points depend on `cmp-shared` and call the ROOT `NavHost` from
the `navigation/` package.
 
---

## Dependency Direction (enforce by package convention, not Gradle walls)

```
feature/*  → core/data → core/network
             core/data → core/model
feature/*  → designsystem
navigation → feature/*                # ROOT graph assembles features — one-way
```

Rules:
- `core/model` has zero dependencies — plain Kotlin data classes only.
- `core/network` never imports from `core/data`.
- Features never import from other features — share data via repositories or navigation args.
- `feature/gallery` never imports from `feature/auth` or touches auth state.
- `navigation/` imports features to assemble the ROOT graph; **features never import `navigation/`** — each feature exposes a `NavGraphBuilder.xGraph(...)` extension and receives navigation as lambda callbacks (`onNavigateToX: () -> Unit`). This one-way flow is what removes any cycle.
---

## Navigation Structure

```
ROOT_GRAPH
  ├── AUTH_GRAPH        # host authentication only
  │     └── MAIN_GRAPH  # upload → dashboard → links
  └── SHARE_GRAPH       # recipient gallery; deep-linked, NO auth — peer, not child
```

Assembled in the `navigation/` package of `cmp-shared`. The ROOT graph calls each feature's
`NavGraphBuilder` extension and supplies navigation lambdas; features stay unaware of each other
and of the assembler.

`SHARE_GRAPH` is a **peer** of `AUTH_GRAPH`, not a child. It handles deep links at
`https://yourapp.com/share/:token` without passing through `AUTH_GRAPH`. A password-protected
link shows a prompt inside `SHARE_GRAPH` — it never touches the host auth flow.
 
---

## Network Layer

Two distinct approaches — use each where it fits.

### Ktorfit — JSON REST endpoints

Standard REST calls. Interfaces in `core/network`; base URL injected via Koin.
Responses mapped to `core/model` types inside `core/data` repositories — never in the network layer.

| Method & Endpoint            | Description                        | Auth            |
|------------------------------|------------------------------------|-----------------|
| `GET /uploads/:id/status`    | Poll processing status             | Host            |
| `GET /uploads/:id/clusters`  | List face clusters                 | Host            |
| `PATCH /clusters/:id`        | Rename a cluster                   | Host            |
| `POST /clusters/:id/links`   | Generate a shareable link          | Host            |
| `GET /share/:token`          | Recipient photos for a link        | None / password |
| `DELETE /links/:token`       | Deactivate a link                  | Host            |

Host token attached via a Ktor auth plugin; recipient endpoints called unauthenticated.

### Raw Ktor `HttpClient` — upload/streaming endpoints

Ktorfit is awkward for multipart and progress tracking. Use `HttpClient` directly in
`UploadService` for these two endpoints only.

| Method & Endpoint            | Description                        | Auth  |
|------------------------------|------------------------------------|-------|
| `POST /uploads`              | Create an upload session           | Host  |
| `POST /uploads/:id/photos`   | Upload photos with progress        | Host  |

```kotlin
// core/network/UploadService.kt
class UploadService(private val client: HttpClient) {
    suspend fun uploadPhotos(
        sessionId: String,
        photos: List<ByteArray>,
        onProgress: (Float) -> Unit,
    ): UploadResult {
        return client.submitFormWithBinaryData(
            url = "/uploads/$sessionId/photos",
            formData = formData { /* append each photo */ },
        ) {
            onUpload { bytesSent, contentLength ->
                // contentLength is nullable in Ktor's callback — guard against divide-by-zero
                onProgress(bytesSent.toFloat() / (contentLength ?: bytesSent))
            }
        }.body()
    }
}
```
 
---

## Recipient Gallery — Isolated Boundary

`feature/gallery` is the one area that must stay structurally isolated from the host flow:

- No imports from `feature/auth` or any auth-related code.
- `SHARE_GRAPH` loads directly from a deep link — never routes through `AUTH_GRAPH`.
- **WASM-first**: `cmp-web` is the primary surface for recipients; test the WASM build of `feature/gallery` early, before over-investing in shared UI.
- Password-protected links show a prompt inside `feature/gallery` — not routed to `feature/auth`.
- **Isolation check**: if you delete `feature/auth`, `feature/gallery` must still compile.
  If Compose Multiplatform on WASM proves problematic for the gallery within your timeline, a thin
  HTML/JS fallback for that surface is acceptable — it is the most user-visible and least
  Kotlin-specific surface in the app.

---

## Feature Package Layout

```
feature/[name]/
├── ui/           # Composable screens — observe state, no business logic
├── viewmodel/    # ViewModel exposing StateFlow<ScreenState<T>>
└── navigation/   # Route destinations + NavGraphBuilder.xGraph(...) extension
```

Each feature's `navigation/` defines its own routes and a graph extension; the ROOT `navigation/`
package (in `cmp-shared`) calls those extensions to assemble the full graph.
 
---

## ScreenState

Use in every ViewModel.

```kotlin
// core/common/ScreenState.kt
sealed interface ScreenState<out T> {
    data object Loading : ScreenState<Nothing>
    data class Success<T>(val data: T) : ScreenState<T>
    data class Error(val message: String) : ScreenState<Nothing>
}
```
 
---

## Key Conventions

- No business logic in Composables — it lives in ViewModels.
- Repositories (`core/data`) own all network-vs-cache decisions.
- `core/model` types are the only types that cross feature boundaries.
- Features expose a `NavGraphBuilder` extension and take navigation lambdas; they never import the `navigation/` package or each other.
- **Feature branches only** — never push to `main`:
```bash
  git checkout -b feature/[description]
  git push origin feature/[description]
```
- Commits: `<type>(<scope>): <subject>` — `feat`, `fix`, `docs`, `refactor`, `test`, `chore`.
---

## Run

- **Android**: `./gradlew :cmp-android:installDebug`
- **Web (WASM)**: `./gradlew :cmp-web:wasmJsBrowserDevelopmentRun` — validate gallery here first
- **Desktop**: `./gradlew :cmp-desktop:run`
- **iOS**: needs macOS + Xcode; other targets build on Ubuntu/Windows.
---

## Startup Sequence

1. Create entry points (`cmp-android`, `cmp-web`, etc.) + `cmp-shared` with `core/model` and `core/network` — no UI yet, just data plumbing.
2. Add `core/data` with one repository to validate the network → repository chain.
3. Add `designsystem`.
4. Add `feature/gallery` (with its own `navigation/` graph extension) and run it in the WASM target — validate the recipient path end-to-end before building the host flow.
5. Add `feature/auth`, `feature/upload`, `feature/dashboard`, `feature/links`, each shipping its own graph extension.
6. Assemble the ROOT graph in the `navigation/` package as features land — it just calls each feature's extension and wires `AUTH_GRAPH` + `SHARE_GRAPH` under `ROOT_GRAPH`.
