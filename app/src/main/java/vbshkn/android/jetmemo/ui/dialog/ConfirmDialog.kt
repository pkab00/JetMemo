package vbshkn.android.jetmemo.ui.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.ui.theme.HeaderBlueGrey
import vbshkn.android.jetmemo.ui.theme.MaterialWhite
import vbshkn.android.jetmemo.ui.theme.VividBlue

/**
 * Диалоговое окно подтверждения действия.
 * @param onConfirm коллбэк при нажатии кнопки "ОК"
 * @param onDismiss коллбэк при нажатии кнопки "Отмена"
 * @param title заголовок окна
 * @param message информационное сообщение
 */
@Composable
fun ConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    message: String
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = MaterialWhite,
        title = {
            Text(
                text = title,
                color = HeaderBlueGrey,
                fontFamily = FontFamily(Font(R.font.nunito_semibold))
            )
        },
        text = {
            Text(
                text = message,
                color = HeaderBlueGrey,
                fontFamily = FontFamily((Font(R.font.nunito_regular)))
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = VividBlue,
                    containerColor = MaterialWhite
                ),
                border = BorderStroke(width = 1.dp, color = VividBlue)
            ) { Text(stringResource(R.string.ok)) }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    contentColor = VividBlue,
                    containerColor = MaterialWhite
                ),
                border = BorderStroke(width = 1.dp, color = VividBlue)
            ) { Text(stringResource(R.string.dismiss)) }
        }
    )
}

/**
 * Диалоговое окно подтверждение с чекбоксом.
 * Чекбокс позволяет подключить/отключить какую-то дополнительную опцию при выборе.
 *
 * @param onCheckboxOn коллбэк при нажатии кнопки "ОК" (чекбокс активен)
 * @param onCheckboxOff коллбэк при нажатии кнопки "ОК" (чекбокс не активен)
 * @param onDismiss коллбэк при нажатии кнопки "Отмена"
 * @param title заголовок окна
 * @param message информационное сообщение
 */
@Composable
fun CheckboxConfirmDialog(
    onCheckboxOn: () -> Unit,
    onCheckboxOff: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    message: String
) {
    var checkedState by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = MaterialWhite,
        title = {
            Text(
                text = title,
                color = HeaderBlueGrey,
                fontFamily = FontFamily(Font(R.font.nunito_semibold))
            )
        },
        text = {
            Row {
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = { checkedState = it },
                    colors = CheckboxDefaults.colors(
                        checkmarkColor = MaterialWhite,
                        checkedColor = VividBlue
                    ),
                    modifier = Modifier.padding(end = 7.dp)
                )
                Text(
                    text = message,
                    color = HeaderBlueGrey,
                    fontFamily = FontFamily((Font(R.font.nunito_regular)))
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if(checkedState) onCheckboxOn() else onCheckboxOff()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = VividBlue,
                    containerColor = MaterialWhite
                ),
                border = BorderStroke(width = 1.dp, color = VividBlue)
            ) { Text(stringResource(R.string.ok)) }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    contentColor = VividBlue,
                    containerColor = MaterialWhite
                ),
                border = BorderStroke(width = 1.dp, color = VividBlue)
            ) { Text(stringResource(R.string.dismiss)) }
        }
    )
}