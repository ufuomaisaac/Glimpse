package com.example.glimpse.feature.upload.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.example.glimpse.feature.upload.model.SelectedPhoto

@Composable
actual fun rememberPhotoPicker(
    onPhotosPicked: (List<SelectedPhoto>) -> Unit,
    onError: (String) -> Unit,
): PhotoPickerLauncher {
    val currentOnError = rememberUpdatedState(onError)
    return remember {
        PhotoPickerLauncher { currentOnError.value("Photo selection is not available on iOS yet") }
    }
}
