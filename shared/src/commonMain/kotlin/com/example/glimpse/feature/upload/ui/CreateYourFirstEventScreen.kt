package com.example.glimpse.feature.upload.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.unit.dp
import com.example.glimpse.designsystem.GlimpseDp
import com.example.glimpse.designsystem.GlimpseSp
import com.example.glimpse.designsystem.GlimpseTextStyles
import com.example.glimpse.designsystem.GlimpseTheme
import com.example.glimpse.designsystem.components.GlimpsePrimaryButton
import com.example.glimpse.feature.upload.viewmodel.CreateFirstEventViewModel
import glimpse.shared.generated.resources.Res
import glimpse.shared.generated.resources.create_first_event_button
import glimpse.shared.generated.resources.create_first_event_image_description
import glimpse.shared.generated.resources.create_first_event_subtitle
import glimpse.shared.generated.resources.create_first_event_welcome
import glimpse.shared.generated.resources.first_event_birthday
import glimpse.shared.generated.resources.first_event_garden
import glimpse.shared.generated.resources.first_event_wedding
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateFirstEventRoute(
    onCreateEvent: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateFirstEventViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CreateYourFirstEventScreen(
        user = uiState.displayName,
        onCreateEvent = onCreateEvent,
        modifier = modifier,
    )
}

/** Stateless content shown after a new account authenticates for the first time. */
@Composable
fun CreateYourFirstEventScreen(
    user: String,
    onCreateEvent: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val images = listOf(
        Res.drawable.first_event_birthday,
        Res.drawable.first_event_wedding,
        Res.drawable.first_event_garden,
    )
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { images.size },
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 480.dp)
                .verticalScroll(rememberScrollState())
                .padding(
                    top = GlimpseDp.dp116,
                    bottom = GlimpseDp.dp24,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(Res.string.create_first_event_welcome, user),
                color = MaterialTheme.colorScheme.onBackground,
                style = GlimpseTextStyles.headingH3,
                fontSize = GlimpseSp.sp24,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = GlimpseDp.dp24),
            )

            Spacer(Modifier.height(GlimpseDp.dp8))

            Text(
                text = stringResource(Res.string.create_first_event_subtitle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = GlimpseTextStyles.labelMedium,
                fontSize = GlimpseSp.sp13,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = GlimpseDp.dp32),
            )

            Spacer(Modifier.height(GlimpseDp.dp32))

            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val focusedWidth = 248.dp
                val focusedHeight = 475.dp
                val sideWidth = 149.dp
                val sideHeight = 286.dp
                val pageSlotWidth = 180.dp
                val imageSpacing = GlimpseDp.dp16
                val pageSpacing = focusedWidth / 2 +
                    sideWidth / 2 +
                    imageSpacing -
                    pageSlotWidth
                val horizontalPadding = maxOf(0.dp, (maxWidth - pageSlotWidth) / 2)

                HorizontalPager(
                    state = pagerState,
                    pageSize = PageSize.Fixed(pageSlotWidth),
                    contentPadding = PaddingValues(horizontal = horizontalPadding),
                    pageSpacing = pageSpacing,
                    beyondViewportPageCount = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(focusedHeight),
                ) { page ->
                    val isFocused = page == pagerState.currentPage
                    val imageWidth = animateDpAsState(
                        targetValue = if (isFocused) focusedWidth else sideWidth,
                        label = "carouselImageWidth",
                    ).value
                    val imageHeight = animateDpAsState(
                        targetValue = if (isFocused) focusedHeight else sideHeight,
                        label = "carouselImageHeight",
                    ).value

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(images[page]),
                            contentDescription = stringResource(
                                Res.string.create_first_event_image_description,
                                page + 1,
                                images.size,
                            ),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .requiredWidth(imageWidth)
                                .requiredHeight(imageHeight)
                                .clip(RoundedCornerShape(GlimpseDp.dp20)),
                        )
                    }
                }
            }

            Spacer(Modifier.height(GlimpseDp.dp20))

            Row(
                horizontalArrangement = Arrangement.spacedBy(GlimpseDp.dp8),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(images.size) { page ->
                    val isSelected = page == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .height(GlimpseDp.dp8)
                            .width(if (isSelected) GlimpseDp.dp24 else GlimpseDp.dp8)
                            .background(
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.outlineVariant
                                },
                                shape = CircleShape,
                            ),
                    )
                }
            }

            Spacer(Modifier.height(GlimpseDp.dp32))

            GlimpsePrimaryButton(
                text = stringResource(Res.string.create_first_event_button),
                onClick = onCreateEvent,
                modifier = Modifier
                    .widthIn(max = 360.dp)
                    .padding(horizontal = GlimpseDp.dp16),
            )

            Spacer(Modifier.height(GlimpseDp.dp32))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun CreateYourFirstEventScreenPreview() {
    GlimpseTheme {
        CreateYourFirstEventScreen(
            user = "Taiwo",
            onCreateEvent = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun CreateYourFirstEventScreenDarkPreview() {
    GlimpseTheme(darkTheme = true) {
        CreateYourFirstEventScreen(
            user = "Taiwo",
            onCreateEvent = {},
        )
    }
}
