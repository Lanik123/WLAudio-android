package ru.lanik.wlaudio.model

import kotlinx.serialization.Serializable
import ru.lanik.wlaudio.ui.theme.WLAudioCorners
import ru.lanik.wlaudio.ui.theme.WLAudioSize

@Serializable
data class SettingsModel(
    val isDark: Boolean = true,
    val isClient: Boolean = false,
    val textSize: WLAudioSize = WLAudioSize.Medium,
    val paddingSize: WLAudioSize = WLAudioSize.Medium,
    val cornerStyle: WLAudioCorners = WLAudioCorners.Rounded,
)
