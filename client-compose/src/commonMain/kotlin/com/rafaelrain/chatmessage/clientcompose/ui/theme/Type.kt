package com.rafaelrain.chatmessage.clientcompose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import com.rafaelrain.chatmessage.clientcompose.ui.theme.font.InterFontFamily
import com.rafaelrain.chatmessage.clientcompose.ui.theme.font.OutfitFontFamily

val BaseFontFamily: FontFamily
    @Composable
    get() = InterFontFamily()

val DisplayFontFamily: FontFamily
    @Composable
    get() = OutfitFontFamily()

// Default Material 3 typography values
val baseline = Typography()

@Composable
fun AppTypography() =
    Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = DisplayFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = DisplayFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = DisplayFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = DisplayFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = DisplayFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = DisplayFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = DisplayFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = DisplayFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = DisplayFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = BaseFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = BaseFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = BaseFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = BaseFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = BaseFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = BaseFontFamily),
    )
