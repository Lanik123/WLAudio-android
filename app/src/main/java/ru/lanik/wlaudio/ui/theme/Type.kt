package ru.lanik.wlaudio.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

data class WLAudioTypography(
    val heading: TextStyle,
    val body: TextStyle,
    val toolbar: TextStyle,
    val caption: TextStyle,
)

val LocalWLAudioTypography = staticCompositionLocalOf<WLAudioTypography> {
    error("No typography provided")
}
