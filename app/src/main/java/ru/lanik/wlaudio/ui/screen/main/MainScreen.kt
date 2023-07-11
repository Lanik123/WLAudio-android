package ru.lanik.wlaudio.ui.screen.main

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.lanik.wlaudio.R
import ru.lanik.wlaudio.ui.helper.CustomPaddingTextField
import ru.lanik.wlaudio.ui.helper.CustomTextFieldColors
import ru.lanik.wlaudio.ui.theme.WLAudioTheme

@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WLAudioTheme.colors.primaryBackground),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WLAudioTheme.shapes.generalPadding),
            horizontalArrangement = Arrangement.End,
        ) {
            FloatingActionButton(
                onClick = { viewModel.navigateTo(R.id.action_main_to_sett) },
                containerColor = WLAudioTheme.colors.secondaryBackground,
                shape = CircleShape,
                modifier = Modifier.size(36.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = null,
                    tint = WLAudioTheme.colors.tintColor,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                color = WLAudioTheme.colors.primaryText,
                style = WLAudioTheme.typography.heading,
                fontSize = 60.sp,
                softWrap = false,
            )
            if (true) {
                ClientScreen(
                    isConnected = false,
                    onConnectClick = {
                        return@ClientScreen false
                    },
                )
            } else {
                ServerScreen(
                    isStarted = false,
                    address = "",
                    onStartClick = {
                        return@ServerScreen false
                    },
                )
            }
        }
    }
}

@Composable
private fun ClientScreen(
    isConnected: Boolean,
    onConnectClick: (String) -> Boolean,
) {
    val icon = if (!isConnected) Icons.Rounded.PlayArrow else Icons.Rounded.Close
    val toastMessage = stringResource(id = R.string.main_toast_server)
    val ipAddress = remember { mutableStateOf("") }
    val port = remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StyledTextField(
                placeholderString = stringResource(id = R.string.main_text_field_ip),
                valueInput = ipAddress.value,
                onValueChanged = { ipAddress.value = it },
                readOnly = isConnected,
                modifier = Modifier.weight(2f),
            )

            Spacer(modifier = Modifier.width(8.dp))

            StyledTextField(
                placeholderString = stringResource(id = R.string.main_text_field_port),
                valueInput = port.value,
                onValueChanged = { port.value = it },
                readOnly = isConnected,
                modifier = Modifier.weight(1f),
            )
        }
        StatusBarRow(
            state = isConnected,
        )
    }

    Button(
        onClick = {
            if (onConnectClick("${ipAddress.value}:${port.value}")) {
                showToast(
                    context = context,
                    toastMessage = toastMessage,
                )
            }
        },
        colors = ButtonDefaults.buttonColors(WLAudioTheme.colors.controlColor),
        modifier = Modifier.size(50.dp),
        shape = WLAudioTheme.shapes.cornersStyle,
        contentPadding = PaddingValues(WLAudioTheme.shapes.generalPadding),
    ) {
        Icon(icon, contentDescription = null)
    }
}

@Composable
private fun ServerScreen(
    isStarted: Boolean,
    address: String,
    onStartClick: () -> Boolean,
) {
    val icon = if (!isStarted) Icons.Rounded.PlayArrow else Icons.Rounded.Close
    val toastMessage = stringResource(id = R.string.main_toast_server)
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = address,
            color = WLAudioTheme.colors.primaryText,
        )
        StatusBarRow(
            state = isStarted,
        )
    }

    Button(
        onClick = {
            if (onStartClick()) {
                showToast(
                    context = context,
                    toastMessage = toastMessage,
                )
            }
        },
        colors = ButtonDefaults.buttonColors(WLAudioTheme.colors.controlColor),
        modifier = Modifier.size(50.dp),
        shape = WLAudioTheme.shapes.cornersStyle,
        contentPadding = PaddingValues(WLAudioTheme.shapes.generalPadding),
    ) {
        Icon(icon, contentDescription = null)
    }
}

@Composable
private fun StyledTextField(
    placeholderString: String,
    valueInput: String,
    onValueChanged: (String) -> Unit,
    readOnly: Boolean,
    modifier: Modifier = Modifier,
) {
    CustomPaddingTextField(
        modifier = modifier,
        value = valueInput,
        placeholderValue = placeholderString,
        onValueChange = onValueChanged,
        readOnly = readOnly,
        colors = CustomTextFieldColors(
            textColor = WLAudioTheme.colors.primaryText,
            placeholderColor = WLAudioTheme.colors.primaryText,
            cursorColor = WLAudioTheme.colors.tintColor,
        ),
        textStyle = WLAudioTheme.typography.caption,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
        ),
    )
}

@Composable
private fun StatusBarRow(
    state: Boolean,
) {
    Row {
        Text(
            color = WLAudioTheme.colors.secondaryText,
            text = "${stringResource(id = R.string.main_text_status)}: ",
        )
        Text(
            color = WLAudioTheme.colors.secondaryText,
            text = if (state) {
                stringResource(id = R.string.main_text_state_active)
            } else {
                stringResource(id = R.string.main_text_state_not_active)
            },
        )
    }
}

private fun showToast(
    context: Context,
    toastMessage: String,
) {
    Toast.makeText(
        context,
        toastMessage,
        Toast.LENGTH_SHORT,
    ).show()
}