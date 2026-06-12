package com.group2.artfinder.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily   = FontFamily.Serif,
        fontWeight   = FontWeight.Bold,
        fontSize     = 48.sp,
        lineHeight   = 56.sp,
        letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily   = FontFamily.Serif,
        fontWeight   = FontWeight.Bold,
        fontSize     = 36.sp,
        lineHeight   = 44.sp,
        letterSpacing = (-0.3).sp
    ),
    headlineLarge = TextStyle(
        fontFamily   = FontFamily.Serif,
        fontWeight   = FontWeight.Bold,
        fontSize     = 28.sp,
        lineHeight   = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily   = FontFamily.Serif,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 22.sp,
        lineHeight   = 30.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily   = FontFamily.Serif,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 18.sp,
        lineHeight   = 26.sp,
    ),
    titleMedium = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 16.sp,
        lineHeight   = 24.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Normal,
        fontSize     = 16.sp,
        lineHeight   = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Normal,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Normal,
        fontSize     = 12.sp,
        lineHeight   = 16.sp,
        letterSpacing = 0.2.sp
    ),
    labelSmall = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Medium,
        fontSize     = 10.sp,
        lineHeight   = 14.sp,
        letterSpacing = 1.sp
    )
)
