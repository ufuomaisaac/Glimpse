# Glimpsee — CLAUDE.md

## Project Overview

Kotlin Multiplatform (KMP) app targeting **Android, iOS, Desktop (JVM), and Web (WASM)**,
with Compose Multiplatform for shared UI. Clean Architecture + MVVM.

**What it does:** WeTransfer-style photo-sharing app — clusters event photos by face, generates
a per-person shareable link. Recipients see only photos they appear in, no account required.
Face detection and storage are server-side; the client talks to the backend API only.

---

## Stack

| Concern        | Library                                        |
|----------------|------------------------------------------------|
| UI             | Compose Multiplatform                          |
| DI             | Koin                                           |
| Navigation     | JetBrains Compose Navigation                   |
| Network        | Raw Ktor `HttpClient` (Ktorfit is **dropped**) |
| Local storage  | DataStore (preferences)                        |
| Logging        | Kermit                                         |

> **Note:** Ktorfit was dropped due to KSP/Kotlin 2.4.0 incompatibility and the small number of
> endpoints (8). Use raw Ktor `HttpClient` for every network call.

---

## Module / Package Structure

All product code lives in `shared/src/commonMain/kotlin/com/example/glimpse/`.
No per-feature Gradle modules — separation is by package only.

```
shared/src/commonMain/kotlin/com/example/glimpse/
├── core/
│   ├── model/        # Domain data classes only — zero deps
│   ├── common/       # ScreenState + shared utilities
│   ├── network/      # Ktor services + ApiEndPoints 
│   └── data/         # Repositories (bridge network ↔ UI)
├── designsystem/     # Theme, typography, color tokens
├── navigation/       # ROOT graph assembly
└── feature/
    ├── auth/         # Host login / registration
    ├── upload/       # Bulk photo upload + progress
    ├── dashboard/    # Cluster grid, rename, configure sharing
    ├── links/        # Link management
    └── gallery/      # Recipient view — NO auth dependency
```

### Feature package layout

```
feature/[name]/
├── ui/           # Composable screens — observe state only
├── viewmodel/    # ViewModel exposing StateFlow<ScreenState<T>>
└── navigation/   # Route destinations + NavGraphBuilder.xGraph(...) extension
```

---

## Dependency Direction

```
feature/*  → core/data → core/network
             core/data → core/model
feature/*  → designsystem
navigation → feature/*
```



---

## Navigation

```
ROOT_GRAPH
  ├── AUTH_GRAPH        # host auth only
  │     └── MAIN_GRAPH  # upload → dashboard → links
  └── SHARE_GRAPH       # recipient gallery; deep-linked, no auth — peer, not child
```

Each feature owns its routes and a `NavGraphBuilder.xGraph(...)` extension. The ROOT
`navigation/` package calls those extensions and supplies navigation lambdas. Features
receive navigation as lambdas (`onNavigateToX: () -> Unit`) 

---

## API Endpoints

```kotlin
// core/network/ApiEndPoints.kt
object ApiEndPoints {
    const val GET_ALL_UPLOADS               = "/uploads"                        // GET /v1/uploads
    const val CREATE_UPLOAD                 = "/uploads"                        // POST /v1/uploads
    const val UPLOAD_STATUS                 = "uploads/{id}/complete"           // POST /v1/uploads/{id}/complete
    const val GET_UPLOAD_BY_ID              = "/uploads"                        // GET  /v1/uploads/{id}
    const val UPDATE_UPLOAD                 = "/clusters"                       // PATCH /v1/uploads/{id}
    const val DELETE_UPLOAD                 = "/uploads{id}"                    // DELETE /uploads/{id}
    const val GET_PRESIGNED_URLS            = "uploads/{id}/photos"             // POST /v1/uploads/{id}/photos
    const val GET_RECEIVED_CLUSTER          = "/clusters"                       // GET /v1/cluster
    const val GET_RECIEVED_CLUSTER_BY_ID    = "/clusters/{id}"                  // GET /v1/cluster{id}
    const val GENERATED_CLUSTER             = "/generated_cluster"              // GET /v1/generated_cluster
}
```

Add new constants here when adding a feature. Never hardcode path strings outside this file.

---

## Network Service Pattern

Services are plain classes that hold a `HttpClient`. No annotations — raw Ktor only.

**GET → returns `Flow<T>`**
**POST / PUT / DELETE → returns `HttpResponse` (suspend)**

```kotlin
// core/network/services/FeatureService.kt
class FeatureService(private val client: HttpClient) {

    fun getFeatureList(): Flow<List<FeatureDto>> = flow {
        emit(client.get(ApiEndPoints.FEATURE).body())
    }

    fun getFeatureById(id: Long): Flow<FeatureDto> = flow {
        emit(client.get("${ApiEndPoints.FEATURE}/$id").body())
    }

    suspend fun createFeature(payload: FeaturePayload): HttpResponse =
        client.post(ApiEndPoints.FEATURE) { setBody(payload) }

    suspend fun updateFeature(id: Long, payload: FeaturePayload): HttpResponse =
        client.put("${ApiEndPoints.FEATURE}/$id") { setBody(payload) }

    suspend fun deleteFeature(id: Long): HttpResponse =
        client.delete("${ApiEndPoints.FEATURE}/$id")
}
```

### Upload / streaming (multipart + progress)

Use `submitFormWithBinaryData` directly in `core/network/UploadService.kt`:

```kotlin
class UploadService(private val client: HttpClient) {
    suspend fun uploadPhotos(
        sessionId: String,
        photos: List<ByteArray>,
        onProgress: (Float) -> Unit,
    ): UploadResult =
        client.submitFormWithBinaryData(
            url = "${ApiEndPoints.UPLOAD_STATUS}/$sessionId/photos",
            formData = formData { /* append each photo */ },
        ) {
            onUpload { bytesSent, contentLength ->
                onProgress(bytesSent.toFloat() / (contentLength ?: bytesSent))
            }
        }.body()
}
```

---

## Repository Pattern
1. Maps DTOs → domain models via .toModel() — the UI never sees network DTOs
2. Wraps outcomes in DataState (DataState.Success / DataState.Error) so the ViewModel has a unified type to observe
3. Moves work off the main thread via .flowOn(ioDispatcher)

### Interface

```kotlin
// core/data/FeatureRepository.kt
interface FeatureRepository {
    fun getFeatureList(): Flow<DataState<List<Feature>>>
    fun getFeatureById(id: Long): Flow<DataState<Feature>>
    suspend fun createFeature(data: Feature): DataState<Unit>
    suspend fun updateFeature(id: Long, data: Feature): DataState<Unit>
    suspend fun deleteFeature(id: Long): DataState<Unit>
}
```

### Implementation

```kotlin
// core/data/FeatureRepositoryImpl.kt
class FeatureRepositoryImpl(
    private val featureService: FeatureService,
) : FeatureRepository {

    override fun getFeatureList(): Flow<DataState<List<Feature>>> = flow {
        emit(DataState.Loading)
        try {
            val result = featureService.getFeatureList().first()
            emit(DataState.Success(result.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(DataState.Error(e.message ?: "Unknown error"))
        }
    }

    override fun getFeatureById(id: Long): Flow<DataState<Feature>> = flow {
        emit(DataState.Loading)
        try {
            val result = featureService.getFeatureById(id).first()
            emit(DataState.Success(result.toDomain()))
        } catch (e: Exception) {
            emit(DataState.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun createFeature(data: Feature): DataState<Unit> = try {
        featureService.createFeature(data.toPayload())
        DataState.Success(Unit)
    } catch (e: Exception) {
        DataState.Error(e.message ?: "Unknown error")
    }

    override suspend fun updateFeature(id: Long, data: Feature): DataState<Unit> = try {
        featureService.updateFeature(id, data.toPayload())
        DataState.Success(Unit)
    } catch (e: Exception) {
        DataState.Error(e.message ?: "Unknown error")
    }

    override suspend fun deleteFeature(id: Long): DataState<Unit> = try {
        featureService.deleteFeature(id)
        DataState.Success(Unit)
    } catch (e: Exception) {
        DataState.Error(e.message ?: "Unknown error")
    }
}
```

---

## ScreenState

```kotlin
// core/common/ScreenState.kt
sealed interface ScreenState<out T> {
    data object Loading : ScreenState<Nothing>
    data class Success<T>(val data: T) : ScreenState<T>
    data class Error(val message: String) : ScreenState<Nothing>
}
```

Every ViewModel exposes `StateFlow<ScreenState<T>>`.

---

## DI Pattern (Koin)

```kotlin
// NetworkModule.kt
val networkModule = module {
    single { FeatureService(get()) }       // get() resolves the shared HttpClient
}

// RepositoryModule.kt  (or DataModule.kt)
val repositoryModule = module {
    single<FeatureRepository> { FeatureRepositoryImpl(get()) }
}
```
Register the `HttpClient` singleton once in `NetworkModule`. Every service receives it via `get()`.

DI instructions for a single-module plain-Ktor app

Step 1 — Wire the HttpClient (once, as a singleton)

val networkModule = module {

      single<HttpClient> {
          HttpClient(OkHttp) {
              expectSuccess = true

              install(HttpTimeout) {
                  requestTimeoutMillis = 60_000
                  socketTimeoutMillis  = 60_000
              }

              install(ContentNegotiation) {
                  json(Json { ignoreUnknownKeys = true })
              }

              install(Logging) {
                  level = LogLevel.ALL
              }
          }
      }
}

One HttpClient shared across all services. Never create it per-request.

  ---
Step 2 — Wire each service (concrete classes, not interfaces)

Each service takes the HttpClient and the base URL:

val serviceModule = module {

      single { LoanService(httpClient = get(), baseUrl = BASE_URL) }
      single { BeneficiaryService(httpClient = get(), baseUrl = BASE_URL) }
      single { AuthService(httpClient = get(), baseUrl = BASE_URL) }
      // one line per service
}

No DataManager. Each service is registered individually.
  
---
Step 3 — Wire repositories (depend on their service directly)

val repositoryModule = module {

      single<LoanRepository> {
          LoanRepositoryImpl(
              service      = get<LoanService>(),
              ioDispatcher = get(named("IO"))
          )
      }
  
      single<BeneficiaryRepository> {
          BeneficiaryRepositoryImpl(
              service      = get<BeneficiaryService>(),
              ioDispatcher = get(named("IO"))
          )
      }
      // one block per repository
}

Each repository only gets the service it actually calls — not a god object holding all services.

  ---
Step 4 — Wire dispatchers

val dispatchersModule = module {
single(named("IO"))         { Dispatchers.IO }
single(named("Default"))    { Dispatchers.Default }
single(named("Main"))       { Dispatchers.Main }
}

  ---
Step 5 — Wire ViewModels

val viewModelModule = module {
viewModelOf(::LoanViewModel)
viewModelOf(::BeneficiaryViewModel)
viewModelOf(::HomeViewModel)
}

viewModelOf uses constructor injection automatically — Koin resolves the repository from the graph.

  ---
Step 6 — Start Koin in Application.onCreate()

class MyApp : Application() {
override fun onCreate() {
super.onCreate()

          startKoin {
              androidContext(this@MyApp)
              modules(
                  dispatchersModule,
                  networkModule,
                  serviceModule,
                  repositoryModule,
                  viewModelModule,
              )
          }
      }
}

Order doesn't matter to Koin (it resolves lazily), but listing them bottom-up (dependencies first) makes it readable.

  ---
The full dependency chain

HttpClient
↓
LoanService(httpClient)
↓
LoanRepositoryImpl(service, ioDispatcher)
↓
LoanViewModel(repository)

Every layer gets exactly what it needs — nothing more. That's why DataManager doesn't belong in this setup.





