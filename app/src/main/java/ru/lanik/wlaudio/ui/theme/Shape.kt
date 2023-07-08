package ru.lanik.wlaudio.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

data class WLAudioShape(
    val generalPadding: Dp,
    val cornersStyle: Shape,
)

enum class WLAudioCorners {
    Flat, Rounded,
}

val LocalWLAudioShape = staticCompositionLocalOf<WLAudioShape> {
    error("No shapes provided")
}