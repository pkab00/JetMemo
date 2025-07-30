package vbshkn.android.jetmemo.ui.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
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
import vbshkn.android.jetmemo.ui.theme.VividBlue

enum class InputLimit(val limit: Int){
    NONE(-1),
    UNIT_NAME(40),
    WORD(30)
}

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
                    text = ""
                },
                containerColor = Color.White,
                title = {
                    Text(
                        text = title,
                        color = HeaderBlueGrey,
                        fontFamily = FontFamily(Font(R.font.nunito_semibold))
                    )
                },
                text = {
                    OutlinedTextField(
                        value = text,
                        textStyle = TextStyle(
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.nunito_semibold))
                        ),
                        onValueChange = { if(it.length <= maxLength || maxLength <= 0) text = it },
                        colors = TextFieldDefaults.colors(
                            cursorColor = Color.White,
                            focusedIndicatorColor = VividBlue,
                            unfocusedIndicatorColor = Color.White,
                            focusedContainerColor = InputAreaFocused,
                            unfocusedContainerColor = InputAreaUnfocused
                        ),
                        visualTransformation = if(maxLength > 0){
                            if(text.length >= maxLength){
                                VisualTransformation { original ->
                                    TransformedText(
                                        AnnotatedString(original.text.take(maxLength)),
                                        OffsetMapping.Identity
                                    )
                                }
                            } else VisualTransformation.None
                        } else VisualTransformation.None,
                        label = {
                            if(maxLength > 0){
                                Text(
                                    text = "${text.length}/$maxLength",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.rubik_regular)),
                                    modifier = Modifier.background(VividBlue)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onConfirm(text)
                            onDismiss()
                            text = ""
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = VividBlue,
                            containerColor = Color.White
                        ),
                        border = BorderStroke(width = 1.dp, color = VividBlue)
                    ) { Text(stringResource(R.string.ok)) }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onDismiss()
                            text = ""
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = VividBlue,
                            containerColor = Color.White
                        ),
                        border = BorderStroke(width = 1.dp, color = VividBlue)
                    ) { Text(stringResource(R.string.dismiss)) }
                }
            )
        }