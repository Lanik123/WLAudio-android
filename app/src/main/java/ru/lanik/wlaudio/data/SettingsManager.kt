package ru.lanik.wlaudio.data

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.lanik.wlaudio.model.SettingsModel

class SettingsManager(
    private val settingsDataStore: DataStore<SettingsModel>,
) {
    init {
        settingsDataStore.data.onEmpty {
            settingsDataStore.updateData { SettingsModel() }
        }
    }

    fun updateData(newModel: SettingsModel) = runBlocking {
        settingsDataStore.updateData { newModel }
    }

    fun getSettingsModel(): SettingsModel? = runBlocking {
        return@runBlocking settingsDataStore.data.singleOrNull()
    }

    fun toMutableStateFlow(
        coroutineScope: CoroutineScope,
    ): MutableStateFlow<SettingsModel> {
        val settingsStateFlow: MutableStateFlow<SettingsModel> by lazy {
            val data = MutableStateFlow(SettingsModel())
            coroutineScope.launch {
                settingsDataStore.data.collect { newValue ->
                    data.value = newValue
                }
            }
            return@lazy data
        }
        return settingsStateFlow
    }

    @Composable
    fun collectAsState(): State<SettingsModel> {
        return settingsDataStore.data.collectAsState(
            initial = SettingsModel(
                isDark = isSystemInDarkTheme(),
            ),
        )
    }
}