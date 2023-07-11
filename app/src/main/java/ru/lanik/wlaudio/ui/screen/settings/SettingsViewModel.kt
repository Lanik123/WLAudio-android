package ru.lanik.wlaudio.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.lanik.wlaudio.data.SettingsManager
import ru.lanik.wlaudio.model.SettingsModel

class SettingsViewModel(
    private val settingsManager: SettingsManager,
    private val navController: NavController,
) : ViewModel() {
    private val _settingsViewState: MutableStateFlow<SettingsModel> = settingsManager
        .toMutableStateFlow(
            coroutineScope = viewModelScope,
        )
    val settingsViewState: StateFlow<SettingsModel> = _settingsViewState.asStateFlow()

    fun onSettingsChange(newSettingsModel: SettingsModel) {
        settingsManager.updateData(newSettingsModel)
    }

    fun onNavigateBack() {
        navController.navigateUp()
    }
}