package ru.lanik.wlaudio.ui.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.lanik.wlaudio.R
import ru.lanik.wlaudio.model.DropdownMenuModel
import ru.lanik.wlaudio.model.MenuItemModel
import ru.lanik.wlaudio.ui.helper.MenuItem
import ru.lanik.wlaudio.ui.helper.StyledTopScreenBar
import ru.lanik.wlaudio.ui.theme.WLAudioCorners
import ru.lanik.wlaudio.ui.theme.WLAudioSize
import ru.lanik.wlaudio.ui.theme.WLAudioTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
) {
    val viewState by viewModel.settingsViewState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(WLAudioTheme.colors.primaryBackground),
    ) {
        StyledTopScreenBar(
            scrollBehavior = scrollBehavior,
            isLoading = false,
            showActionButton = false,
            navIcon = {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = WLAudioTheme.colors.tintColor,
                )
            },
            onNavClick = { viewModel.onNavigateBack() },
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(id = R.string.settings_fragment_name).uppercase(),
                    color = WLAudioTheme.colors.primaryText,
                    style = WLAudioTheme.typography.body,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(WLAudioTheme.shapes.generalPadding),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.settings_action_enable_dart_theme),
                color = WLAudioTheme.colors.primaryText,
                style = WLAudioTheme.typography.body,
            )

            Switch(
                checked = viewState.isDark,
                onCheckedChange = {
                    viewModel.onSettingsChange(
                        viewState.copy(
                            isDark = it,
                        ),
                    )
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = WLAudioTheme.colors.tintColor,
                    uncheckedThumbColor = WLAudioTheme.colors.secondaryText,
                ),
            )
        }

        Divider(
            modifier = Modifier.padding(start = WLAudioTheme.shapes.generalPadding),
            thickness = 0.5.dp,
            color = WLAudioTheme.colors.secondaryText.copy(
                alpha = 0.3f,
            ),
        )

        MenuItem(
            model = MenuItemModel(
                title = stringResource(id = R.string.settings_action_text_size),
                currentIndex = when (viewState.textSize) {
                    WLAudioSize.Small -> 0
                    WLAudioSize.Medium -> 1
                    WLAudioSize.Big -> 2
                },
                dropdownMenuModel = DropdownMenuModel(
                    values = listOf(
                        stringResource(id = R.string.settings_option_size_small),
                        stringResource(id = R.string.settings_option_size_medium),
                        stringResource(id = R.string.settings_option_size_big),
                    ),
                ),
            ),
            onItemSelected = {
                val settingsNew = viewState.copy(
                    textSize = when (it) {
                        0 -> WLAudioSize.Small
                        1 -> WLAudioSize.Medium
                        2 -> WLAudioSize.Big
                        else -> throw NotImplementedError("No valid value for this $it")
                    },
                )
                viewModel.onSettingsChange(settingsNew)
            },
        )

        MenuItem(
            model = MenuItemModel(
                title = stringResource(id = R.string.settings_action_padding_size),
                currentIndex = when (viewState.paddingSize) {
                    WLAudioSize.Small -> 0
                    WLAudioSize.Medium -> 1
                    WLAudioSize.Big -> 2
                },
                dropdownMenuModel = DropdownMenuModel(
                    values = listOf(
                        stringResource(id = R.string.settings_option_size_small),
                        stringResource(id = R.string.settings_option_size_medium),
                        stringResource(id = R.string.settings_option_size_big),
                    ),
                ),
            ),
            onItemSelected = {
                val settingsNew = viewState.copy(
                    paddingSize = when (it) {
                        0 -> WLAudioSize.Small
                        1 -> WLAudioSize.Medium
                        2 -> WLAudioSize.Big
                        else -> throw NotImplementedError("No valid value for this $it")
                    },
                )
                viewModel.onSettingsChange(settingsNew)
            },
        )

        MenuItem(
            model = MenuItemModel(
                title = stringResource(id = R.string.settings_action_cornel_style),
                currentIndex = when (viewState.cornerStyle) {
                    WLAudioCorners.Rounded -> 0
                    WLAudioCorners.Flat -> 1
                },
                dropdownMenuModel = DropdownMenuModel(
                    values = listOf(
                        stringResource(id = R.string.settings_option_shape_rounded),
                        stringResource(id = R.string.settings_option_shape_flat),
                    ),
                ),
            ),
            onItemSelected = {
                val settingsNew = viewState.copy(
                    cornerStyle = when (it) {
                        0 -> WLAudioCorners.Rounded
                        1 -> WLAudioCorners.Flat
                        else -> throw NotImplementedError("No valid value for this $it")
                    },
                )
                viewModel.onSettingsChange(settingsNew)
            },
        )
    }
}