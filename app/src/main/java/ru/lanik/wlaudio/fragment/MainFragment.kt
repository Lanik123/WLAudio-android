package ru.lanik.wlaudio.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lanik.wlaudio.data.SettingsManager
import ru.lanik.wlaudio.ui.screen.main.MainScreen
import ru.lanik.wlaudio.ui.screen.main.MainViewModel
import ru.lanik.wlaudio.ui.screen.main.MainViewModelFactory
import ru.lanik.wlaudio.ui.theme.SetNavigationBarColor
import ru.lanik.wlaudio.ui.theme.SetStatusBarColor
import ru.lanik.wlaudio.ui.theme.WLAudioTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = viewModelFactory.getViewModel(
            navController = findNavController(),
        )
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
                    MainScreen(
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}