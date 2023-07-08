package ru.lanik.wlaudio.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class WLAudioSize {
    Small, Medium, Big,
}

@Composable
fun WLAudioTheme(
    textSize: WLAudioSize = WLAudioSize.Medium,
    paddingSize: WLAudioSize = WLAudioSize.Medium,
    corners: WLAudioCorners = WLAudioCorners.Rounded,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    val typography = WLAudioTypography(
        heading = TextStyle(
            fontSize = when (textSize) {
                WLAudioSize.Small -> 24.sp
                WLAudioSize.Medium -> 28.sp
                WLAudioSize.Big -> 32.sp
            },
            fontWeight = FontWeight.Bold,
        ),
        body = TextStyle(
            fontSize = when (textSize) {
                WLAudioSize.Small -> 14.sp
                WLAudioSize.Medium -> 16.sp
                WLAudioSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Normal,
        ),
        toolbar = TextStyle(
            fontSize = when (textSize) {
                WLAudioSize.Small -> 14.sp
                WLAudioSize.Medium -> 16.sp
                WLAudioSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Medium,
        ),
        caption = TextStyle(
            fontSize = when (textSize) {
                WLAudioSize.Small -> 10.sp
                WLAudioSize.Medium -> 12.sp
                WLAudioSize.Big -> 14.sp
            },
        ),
    )

    val shapes = WLAudioShape(
        generalPadding = when (paddingSize) {
            WLAudioSize.Small -> 12.dp
            WLAudioSize.Medium -> 16.dp
            WLAudioSize.Big -> 20.dp
        },
        cornersStyle = when (corners) {
            WLAudioCorners.Flat -> RoundedCornerShape(0.dp)
            WLAudioCorners.Rounded -> RoundedCornerShape(8.dp)
        },
    )
    CompositionLocalProvider(
        LocalWLAudioColors provides colorScheme,
        LocalWLAudioTypography provides typography,
        LocalWLAudioShape provides shapes,
        content = content,
    )
}

object WLAudioTheme {
    val colors: WLAudioColors
        @Composable
        get() = LocalWLAudioColors.current

    val typography: WLAudioTypography
        @Composable
        get() = LocalWLAudioTypography.current

    val shapes: WLAudioShape
        @Composable
        get() = LocalWLAudioShape.current
}