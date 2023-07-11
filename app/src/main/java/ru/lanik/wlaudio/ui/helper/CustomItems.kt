package ru.lanik.wlaudio.ui.helper

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ru.lanik.wlaudio.R
import ru.lanik.wlaudio.model.DropdownMenuModel
import ru.lanik.wlaudio.model.MenuItemModel
import ru.lanik.wlaudio.ui.theme.WLAudioTheme

@Composable
fun MenuItem(
    model: MenuItemModel,
    onItemSelected: (Int) -> Unit = {},
) {
    val isDropdownOpen = remember { mutableStateOf(false) }
    val currentPosition = remember { mutableStateOf(model.currentIndex) }

    Column {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                Modifier
                    .clickable {
                        isDropdownOpen.value = true
                    }
                    .padding(WLAudioTheme.shapes.generalPadding)
                    .background(WLAudioTheme.colors.primaryBackground),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = WLAudioTheme.shapes.generalPadding),
                    text = model.title,
                    style = WLAudioTheme.typography.body,
                    color = WLAudioTheme.colors.primaryText,
                )

                Text(
                    text = model.dropdownMenuModel.values[currentPosition.value],
                    style = WLAudioTheme.typography.body,
                    color = WLAudioTheme.colors.secondaryText,
                )

                Icon(
                    modifier = Modifier
                        .padding(start = WLAudioTheme.shapes.generalPadding / 4)
                        .size(18.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.ic_baseline_arrow),
                    contentDescription = "Arrow",
                    tint = WLAudioTheme.colors.secondaryText,
                )
            }

            Divider(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.BottomStart),
                thickness = 0.5.dp,
                color = WLAudioTheme.colors.secondaryText.copy(
                    alpha = 0.3f,
                ),
            )
        }
        DropdownMenuItem(
            model = model.dropdownMenuModel,
            modifier = Modifier.fillMaxWidth(),
            isDropdownOpen = isDropdownOpen.value,
            onItemClick = {
                currentPosition.value = it
                isDropdownOpen.value = false
                onItemSelected(it)
            },
            onDismiss = {
                isDropdownOpen.value = false
            },
        )
    }
}

@Composable
fun DropdownMenuItem(
    model: DropdownMenuModel?,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    backgroundColor: Color = WLAudioTheme.colors.primaryBackground,
    isDropdownOpen: Boolean = false,
    onItemClick: (Int) -> Unit = {},
    onDismiss: () -> Unit = {},
    additionalContent: @Composable () -> Unit = {},
) {
    DropdownMenu(
        expanded = isDropdownOpen,
        onDismissRequest = {
            onDismiss()
        },
        modifier = modifier
            .background(backgroundColor),
        offset = offset,
    ) {
        model?.values?.forEachIndexed { index, value ->
            DropdownMenuItem(
                onClick = {
                    onItemClick(index)
                },
                text = {
                    Text(
                        text = value,
                        style = WLAudioTheme.typography.body,
                        color = WLAudioTheme.colors.primaryText,
                        textAlign = TextAlign.Center,
                        modifier = modifier,
                    )
                },
            )
        }
        additionalContent()
    }
}