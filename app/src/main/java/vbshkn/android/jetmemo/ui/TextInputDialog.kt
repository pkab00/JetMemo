package vbshkn.android.jetmemo.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.ui.theme.HeaderBlueGrey
import vbshkn.android.jetmemo.ui.theme.InputTextBlue
import vbshkn.android.jetmemo.ui.theme.PlaceholderFocused
import vbshkn.android.jetmemo.ui.theme.VividBlue

// TODO: Сделать нормальную цветовую тему

@Composable
fun TextInputDialog(
    isOpen: Boolean,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    title: String,
    initialValue: String = ""
) {
    var text by remember { mutableStateOf(initialValue) }

    if(isOpen){
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = title,
                    color = HeaderBlueGrey,
                    fontFamily = FontFamily(Font(R.font.nunito_semibold))
                )
            },
            text = {
                TextField(
                    value = text,
                    textStyle = TextStyle(
                        color = InputTextBlue,
                        fontFamily = FontFamily(Font(R.font.nunito_semibold))
                    ),
                    onValueChange = { text = it },
                    colors = TextFieldDefaults.colors(
                        cursorColor = VividBlue,
                        focusedIndicatorColor = VividBlue,
                        unfocusedIndicatorColor = Color.White,
                        focusedPlaceholderColor = PlaceholderFocused
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(text)
                        onDismiss()
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
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = VividBlue,
                        containerColor = Color.White
                    ),
                    border = BorderStroke(width = 1.dp, color = VividBlue)
                ) { Text(stringResource(R.string.dismiss)) }
            }
        )
    }
}