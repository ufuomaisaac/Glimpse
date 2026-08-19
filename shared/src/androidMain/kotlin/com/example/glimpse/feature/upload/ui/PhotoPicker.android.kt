package com.example.glimpse.feature.upload.ui

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.example.glimpse.feature.upload.model.SelectedPhoto

@Composable
actual fun rememberPhotoPicker(
    onPhotosPicked: (List<SelectedPhoto>) -> Unit,
    onError: (String) -> Unit,
): PhotoPickerLauncher {
    val context = LocalContext.current
    val currentOnPhotosPicked = rememberUpdatedState(onPhotosPicked)
    val currentOnError = rememberUpdatedState(onError)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(MAX_PHOTOS),
    ) { uris ->
        runCatching {
            uris.map { uri ->
                var name = "photo.jpg"
                var size = 0L

                context.contentResolver.query(
                    uri,
                    arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE),
                    null,
                    null,
                    null,
                )?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val nameColumn = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeColumn = cursor.getColumnIndex(OpenableColumns.SIZE)
                        if (nameColumn >= 0) name = cursor.getString(nameColumn) ?: name
                        if (sizeColumn >= 0 && !cursor.isNull(sizeColumn)) size = cursor.getLong(sizeColumn)
                    }
                }

                val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: error("Could not read $name")

                SelectedPhoto(
                    id = uri.toString(),
                    name = name,
                    sizeBytes = size.takeIf { it > 0 } ?: bytes.size.toLong(),
                    bytes = bytes,
                )
            }
        }.onSuccess(currentOnPhotosPicked.value)
            .onFailure { currentOnError.value(it.message ?: "Could not open the selected photos") }
    }

    return remember(launcher) {
        PhotoPickerLauncher {
            launcher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
        }
    }
}

private const val MAX_PHOTOS = 100
