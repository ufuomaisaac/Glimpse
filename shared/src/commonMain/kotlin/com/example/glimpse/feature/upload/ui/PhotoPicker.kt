package com.example.glimpse.feature.upload.ui

import androidx.compose.runtime.Composable
import com.example.glimpse.feature.upload.model.SelectedPhoto

fun interface PhotoPickerLauncher {
    fun launch()
}

@Composable
expect fun rememberPhotoPicker(
    onPhotosPicked: (List<SelectedPhoto>) -> Unit,
    onError: (String) -> Unit,
): PhotoPickerLauncher
