package ru.lanik.wlaudio.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lanik.wlaudio.data.SettingsManager
import ru.lanik.wlaudio.ui.screen.settings.SettingsScreen
import ru.lanik.wlaudio.ui.screen.settings.SettingsViewModelFactory
import ru.lanik.wlaudio.ui.theme.SetNavigationBarColor
import ru.lanik.wlaudio.ui.theme.SetStatusBarColor
import ru.lanik.wlaudio.ui.theme.WLAudioTheme
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: SettingsViewModelFactory

    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val settingsState = settingsManager.collectAsState()
                WLAudioTheme(
                    textSize = settingsState.value.textSize,
                    paddingSize = settingsState.value.paddingSize,
                    corners = settingsState.value.cornerStyle,
                    darkTheme = settingsState.value.isDark,
                ) {
                    SetStatusBarColor(
                        color = WLAudioTheme.colors.primaryBackground,
                        isDarkMode = settingsState.value.isDark,
                    )
                    SetNavigationBarColor(
                        color = WLAudioTheme.colors.primaryBackground,
                        isDarkMode = settingsState.value.isDark,
                    )
                    SettingsScreen(
                        viewModel = viewModelFactory.getViewModel(
                            navController = findNavController(),
                            settingsManager = settingsManager,
                        ),
                    )
                }
            }
        }
    }
}