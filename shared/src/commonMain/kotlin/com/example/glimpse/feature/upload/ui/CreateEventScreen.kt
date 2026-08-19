package com.example.glimpse.feature.upload.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.glimpse.designsystem.*
import com.example.glimpse.designsystem.components.GlimpseChip
import com.example.glimpse.designsystem.components.GlimpseInputTextField
import com.example.glimpse.designsystem.components.GlimpsePrimaryButton
import com.example.glimpse.feature.upload.model.SelectedPhoto
import com.example.glimpse.feature.upload.viewmodel.CreateEventUiState
import com.example.glimpse.feature.upload.viewmodel.CreateEventViewModel
import glimpse.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateEventRoute(
    onBack: () -> Unit,
    onUploadRequest: (String, List<SelectedPhoto>) -> Unit,
    viewModel: CreateEventViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val picker = rememberPhotoPicker(viewModel::addPhotos, viewModel::showError)

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbar.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        CreateEventScreen(
            state = state,
            onEventNameChange = viewModel::updateEventName,
            onExpirationDaysChange = viewModel::updateExpirationDays,
            onAddPhotos = picker::launch,
            onRemovePhoto = viewModel::removePhoto,
            onBack = onBack,
            onUpload = {
                viewModel.createUpload {
                    onUploadRequest(state.eventName.trim(), state.photos)
                }
            },
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
fun CreateEventScreen(
    state: CreateEventUiState,
    onEventNameChange: (String) -> Unit,
    onExpirationDaysChange: (Int) -> Unit,
    onAddPhotos: () -> Unit,
    onRemovePhoto: (String) -> Unit,
    onBack: () -> Unit,
    onUpload: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            Modifier.widthIn(max = 560.dp).fillMaxSize().imePadding()
                .verticalScroll(rememberScrollState()).padding(horizontal = GlimpseDp.dp16),
        ) {
            Row(
                Modifier.fillMaxWidth().padding(top = GlimpseDp.dp16),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(GlimpseIcons.ArrowBack),
                        contentDescription = "Back",
                    )
                }
                Spacer(Modifier.size(GlimpseDp.dp8))
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(Res.string.create_event_title),
                        style = GlimpseTextStyles.headingH1,
                        fontSize = GlimpseSp.sp20,
                    )
                    Text(
                        text = stringResource(Res.string.create_event_subtitle),
                        style = GlimpseTextStyles.labelMedium,
                        fontSize = GlimpseSp.sp10
                    )
                }

            }

            Spacer(Modifier.height(GlimpseDp.dp32))
            Text(stringResource(Res.string.create_event_event_name), style = GlimpseTextStyles.headingH2, fontSize = GlimpseSp.sp11)
            Spacer(Modifier.height(GlimpseDp.dp8))
            GlimpseInputTextField(state.eventName, onEventNameChange)
            Spacer(Modifier.height(GlimpseDp.dp4))
            Spacer(Modifier.height(GlimpseDp.dp24))
            Text("Link expires after", style = GlimpseTextStyles.overline)
            Spacer(Modifier.height(GlimpseDp.dp8))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(GlimpseDp.dp10),
            ) {
                items(
                    items = EXPIRATION_OPTIONS,
                    key = { it },
                ) { days ->
                    GlimpseChip(
                        text = "$days days",
                        selected = state.expirationDays == days,
                        onClick = { onExpirationDaysChange(days) },
                    )
                }
            }
            Spacer(Modifier.height(GlimpseDp.dp24))
            AddPhotosCard(onAddPhotos)

            if (state.photos.isNotEmpty()) {
                Spacer(Modifier.height(GlimpseDp.dp24))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        pluralStringResource(
                            Res.plurals.create_event_photos_selected,
                            state.photos.size,
                            state.photos.size,
                        ),
                        style = GlimpseTextStyles.headingH1,
                        fontSize = GlimpseSp.sp16,
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        formatBytes(state.totalBytes),
                        style = GlimpseTextStyles.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.height(GlimpseDp.dp8))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(GlimpseDp.dp12),
                ) {
                    items(
                        items = state.photos,
                        key = SelectedPhoto::id,
                    ) { photo ->
                        PhotoCard(photo) {
                            onRemovePhoto(photo.id)
                        }
                    }
                }
            }



            Spacer(Modifier.height(GlimpseDp.dp24))
            GlimpsePrimaryButton(
                text = if (state.isUploading) {
                    stringResource(Res.string.create_event_uploading)
                } else {
                    stringResource(Res.string.create_event_upload_button, state.photos.size)
                },
                onClick = onUpload,
                isLoading = state.isUploading,
                enabled = state.canUpload,
            )
            Spacer(Modifier.height(GlimpseDp.dp32))
        }
    }
}

@Composable
private fun AddPhotosCard(onClick: () -> Unit) {
    val border = MaterialTheme.colorScheme.primary
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(GlimpseDp.dp16))
            .drawBehind {
                drawRoundRect(
                    border,
                    cornerRadius = CornerRadius(GlimpseDp.dp16.toPx()),
                    style = Stroke(
                        GlimpseDp.dp2.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f)),
                    ),
                )
            }
            .clickable(onClick = onClick)
            .padding(vertical = GlimpseDp.dp32, horizontal = GlimpseDp.dp20),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier.size(GlimpseDp.dp48).clip(RoundedCornerShape(GlimpseDp.dp12))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painterResource(GlimpseIcons.UploadCloud),
                null,
                tint = AccentPrimary,
                modifier = Modifier.size(GlimpseDp.dp24),
            )
        }
        Spacer(Modifier.height(GlimpseDp.dp12))
        Text(
            stringResource(Res.string.create_event_add_photos),
            style = GlimpseTextStyles.headingH1,
            fontSize = GlimpseSp.sp16,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(GlimpseDp.dp4))
        Text(
            stringResource(Res.string.create_event_photo_limit),
            style = GlimpseTextStyles.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PhotoCard(photo: SelectedPhoto, onRemove: () -> Unit) {
    val thumbnail = remember(photo.bytes) {
        runCatching { photo.bytes.decodeToImageBitmap() }.getOrNull()
    }

    Column(
        Modifier.width(100.dp)
            .clip(RoundedCornerShape(GlimpseDp.dp16))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Box(
            Modifier.fillMaxWidth().height(100.dp)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            if (thumbnail != null) {
                Image(
                    bitmap = thumbnail,
                    contentDescription = photo.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Icon(
                    painterResource(GlimpseIcons.Images),
                    contentDescription = null,
                    modifier = Modifier.size(GlimpseDp.dp24),
                )
            }

            IconButton(
                onClick = onRemove,
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                Icon(
                    painterResource(GlimpseIcons.Close),
                    stringResource(Res.string.create_event_remove_photo, photo.name),
                )
            }
        }
        Column(Modifier.padding(GlimpseDp.dp12)) {
            Text(
                photo.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(GlimpseDp.dp4))
            Text(
                formatBytes(photo.sizeBytes),
                style = GlimpseTextStyles.legal,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun formatBytes(bytes: Long) = when {
    bytes >= 1_000_000 -> "${bytes / 1_000_000} MB"
    bytes >= 1_000 -> "${bytes / 1_000} KB"
    else -> "$bytes B"
}

private val EXPIRATION_OPTIONS = listOf(7, 14, 30)

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun CreateEventPreview() {
    GlimpseTheme {
        CreateEventScreen(
            state = CreateEventUiState(
                eventName = "Ada & Tobi's wedding",
                photos = listOf(
                    SelectedPhoto("1", "IMG_2048.jpg", 2_400_000, byteArrayOf()),
                    SelectedPhoto("2", "ceremony.jpg", 1_800_000, byteArrayOf()),
                ),
            ),
            onEventNameChange = {},
            onExpirationDaysChange = {},
            onAddPhotos = {},
            onRemovePhoto = {},
            onBack = {},
            onUpload = {},
        )
    }
}
