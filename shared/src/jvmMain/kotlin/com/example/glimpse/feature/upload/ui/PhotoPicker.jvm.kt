package com.example.glimpse.feature.upload.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.example.glimpse.feature.upload.model.SelectedPhoto
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
actual fun rememberPhotoPicker(
    onPhotosPicked: (List<SelectedPhoto>) -> Unit,
    onError: (String) -> Unit,
): PhotoPickerLauncher {
    val currentOnPhotosPicked = rememberUpdatedState(onPhotosPicked)
    val currentOnError = rememberUpdatedState(onError)
    return remember {
        PhotoPickerLauncher {
            runCatching {
                val chooser = JFileChooser().apply {
                    isMultiSelectionEnabled = true
                    fileFilter = FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "webp")
                }
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    chooser.selectedFiles.map(File::toSelectedPhoto)
                } else {
                    emptyList()
                }
            }.onSuccess { if (it.isNotEmpty()) currentOnPhotosPicked.value(it) }
                .onFailure { currentOnError.value(it.message ?: "Could not open the selected photos") }
        }
    }
}

private fun File.toSelectedPhoto() = SelectedPhoto(
    id = absolutePath,
    name = name,
    sizeBytes = length(),
    bytes = readBytes(),
)
