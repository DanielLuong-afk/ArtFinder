package com.group2.artfinder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ArtFinderColorScheme = darkColorScheme(
    primary              = Gold,
    onPrimary            = MuseumBlack,
    primaryContainer     = GoldDim,
    onPrimaryContainer   = GoldLight,
    secondary            = GoldLight,
    onSecondary          = MuseumBlack,
    background           = MuseumBlack,
    onBackground         = OffWhite,
    surface              = MuseumSurface,
    onSurface            = OffWhite,
    surfaceVariant       = MuseumCard,
    onSurfaceVariant     = Muted,
    error                = ErrorRed,
    onError              = OffWhite,
)

@Composable
fun ArtFinderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ArtFinderColorScheme,
        typography  = Typography,
        content     = content
    )
}
