package vbshkn.android.jetmemo.ui.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.ui.theme.HeaderBlueGrey
import vbshkn.android.jetmemo.ui.theme.MaterialWhite
import vbshkn.android.jetmemo.ui.theme.VividBlue

/**
 * Кастомное диалоговое окно для вывода информационных текстовых сообщений.
 * @param onConfirm коллбэк при нажатии кнопки "ОК"
 * @param onDismiss коллбэк при попытке закрыть окно
 * @param title заголовок окна
 * @param message информационное сообщение
 */
@Composable
fun InfoDialog(
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
        }
    )
}