package vbshkn.android.jetmemo.ui.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.ui.theme.HeaderBlueGrey
import vbshkn.android.jetmemo.ui.theme.InputAreaFocused
import vbshkn.android.jetmemo.ui.theme.InputAreaUnfocused
import vbshkn.android.jetmemo.ui.theme.MaterialWhite
import vbshkn.android.jetmemo.ui.theme.VividBlue

/**
 * Enum с позможными ограничениями на длину вводаю
 * @param limit максимально возможная длина строки
 */
enum class InputLimit(val limit: Int) {
    NONE(-1),
    UNIT_NAME(40),
    WORD(30)
}

/**
 * Дилаоговое окно для ввода текста (одна строка).
 * @param onConfirm коллбэк при подтверждении ввода
 * @param onDismiss коллбэк при попытке закрыть окно
 * @param title загловок окна
 * @param initialValue изначально отображаемый текст
 * @param inputLimit ограничение на ввод текста
 */
@Composable
fun TextInputDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    title: String,
    initialValue: String = "",
    inputLimit: InputLimit = InputLimit.NONE
) {
    var text by remember { mutableStateOf(initialValue) }
    val maxLength = inputLimit.limit

    AlertDialog(
        onDismissRequest = {
            onDismiss()
            text = initialValue
        },
        containerColor = MaterialWhite,
        title = {
            Text(
                text = title,
                fontSize = 22.sp,
                color = HeaderBlueGrey,
                fontFamily = FontFamily(Font(R.font.nunito_semibold))
            )
        },
        text = {
            CustomTextField(text, maxLength) { text = it }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(text)
                    onDismiss()
                    text = initialValue
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
                onClick = {
                    onDismiss()
                    text = initialValue
                },
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
 * Дилаоговое окно для ввода текста (две строки).
 * @param onConfirm коллбэк при подтверждении ввода
 * @param onDismiss коллбэк при попытке закрыть окно
 * @param title загловок окна
 * @param firstInitialValue изначально отображаемый текст (первое поле)
 * @param secondInitialValue изначально отображаемый текст (второе поле)
 * @param inputLimit ограничение на ввод текста
 */
@Composable
fun DoubleTextInputDialog(
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit,
    title: String,
    firstInitialValue: String = "",
    secondInitialValue: String = "",
    inputLimit: InputLimit = InputLimit.NONE
) {
    var firstText by remember { mutableStateOf(firstInitialValue) }
    var secondText by remember { mutableStateOf(secondInitialValue) }
    val maxLength = inputLimit.limit

    AlertDialog(
        onDismissRequest = {
            onDismiss()
            firstText = firstInitialValue
            secondText = secondInitialValue
        },
        containerColor = MaterialWhite,
        title = {
            Text(
                text = title,
                fontSize = 22.sp,
                color = HeaderBlueGrey,
                fontFamily = FontFamily(Font(R.font.nunito_semibold))
            )
        },
        text = {
            Column {
                CustomTextField(firstText, maxLength) { firstText = it }
                Spacer(Modifier.height(10.dp))
                CustomTextField(secondText, maxLength) { secondText = it }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(firstText, secondText)
                    onDismiss()
                    firstText = firstInitialValue
                    secondText = secondInitialValue
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
                onClick = {
                    onDismiss()
                    firstText = firstInitialValue
                    secondText = secondInitialValue
                },
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
 * Кастомное текстовое поле, используемое диалоговыми окнами.
 * @param text вводимый текст
 * @param maxLength максимальная длина ввода
 * @param onValueChange коллбэк при изменении введённого значения
 */
@Composable
private fun CustomTextField(
    text: String,
    maxLength: Int,
    onValueChange: (String) -> Unit
) {
    var cleanInitial by remember { mutableStateOf(true) }

    OutlinedTextField(
        value = text,
        textStyle = TextStyle(
            color = MaterialWhite,
            fontFamily = FontFamily(Font(R.font.nunito_semibold))
        ),
        onValueChange = { newText ->
            if (maxLength <= 0 || newText.length <= maxLength) {
                onValueChange(newText)
            }
        },
        colors = TextFieldDefaults.colors(
            cursorColor = MaterialWhite,
            focusedIndicatorColor = VividBlue,
            unfocusedIndicatorColor = MaterialWhite,
            focusedContainerColor = InputAreaFocused,
            unfocusedContainerColor = InputAreaUnfocused
        ),
        visualTransformation = if (maxLength > 0) {
            if (text.length >= maxLength) {
                VisualTransformation { original ->
                    TransformedText(
                        AnnotatedString(original.text.take(maxLength)),
                        OffsetMapping.Identity
                    )
                }
            } else VisualTransformation.None
        } else VisualTransformation.None,
        label = {
            if (maxLength > 0) {
                Text(
                    text = "${text.length}/$maxLength",
                    color = MaterialWhite,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.rubik_regular)),
                    modifier = Modifier.background(VividBlue)
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                // очищаем поле от плейсхолдера при первом наведении фокуса
                if(focusState.isFocused && cleanInitial) {
                    onValueChange("")
                    cleanInitial = false
                }
            }
    )
}