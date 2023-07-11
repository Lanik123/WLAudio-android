package ru.lanik.wlaudio.ui.screen.settings

import androidx.navigation.NavController
import ru.lanik.wlaudio.data.SettingsManager

class SettingsViewModelFactory {
    fun getViewModel(
        navController: NavController,
        settingsManager: SettingsManager,
    ): SettingsViewModel {
        return SettingsViewModel(
            navController = navController,
            settingsManager = settingsManager,
        )
    }
}