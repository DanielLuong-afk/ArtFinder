package com.group2.artfinder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ArtFinderColorScheme = darkColorScheme(
    primary              = Sage,
    onPrimary            = ForestBlack,
    primaryContainer     = SageDim,
    onPrimaryContainer   = SageLight,
    secondary            = SageLight,
    onSecondary          = ForestBlack,
    background           = ForestBlack,
    onBackground         = Ivory,
    surface              = ForestSurface,
    onSurface            = Ivory,
    surfaceVariant       = ForestCard,
    onSurfaceVariant     = Stone,
    error                = CrimsonRed,
    onError              = Ivory,
)

@Composable
fun ArtFinderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ArtFinderColorScheme,
        typography  = Typography,
        content     = content
    )
}