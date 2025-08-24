package vbshkn.android.jetmemo.ui.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.ui.theme.HeaderBlueGrey
import vbshkn.android.jetmemo.ui.theme.MaterialWhite
import vbshkn.android.jetmemo.ui.theme.OptionTextGrey
import vbshkn.android.jetmemo.ui.theme.VividBlue

/**
 * Кастомный диалог большого размера со списком элементов, которые можно выбирать при помощи чекбоксов.
 * @param onConfirm коллбэк при нажатии кнопки подтверждения
 * @param onDismiss коллбэк при попытке закрыть окно
 * @param title заголовок окна
 * @param initialSelection выбранные элементы по умолчанию
 * @param content список данных, отоюражаемых диалогом
 */
@Composable
fun MultiSelectionDialog(
    onConfirm: (selection: List<Any>) -> Unit,
    onDismiss: () -> Unit,
    title: String,
    initialSelection: List<Any> = emptyList(),
    content: List<Any> = emptyList()
) {
    var selectionList by remember(initialSelection) {
        mutableStateOf(initialSelection.toMutableList())
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        content = {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.Transparent,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    Modifier
                        .size(500.dp, 750.dp)
                        .background(MaterialWhite)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 22.sp,
                            color = HeaderBlueGrey,
                            fontFamily = FontFamily(Font(R.font.nunito_semibold))
                        )
                    }
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(10.dp)
                    ) {
                        items(content) { option ->
                            SelectionItem(
                                data = option,
                                onCheckOn = { selectionList.add(option) },
                                onCheckOff = { selectionList.remove(option) }
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        TextButton(
                            onClick = {
                                onConfirm(selectionList)
                                onDismiss()
                                selectionList = initialSelection.toMutableList()
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = VividBlue,
                                containerColor = MaterialWhite
                            ),
                            border = BorderStroke(width = 1.dp, color = VividBlue)
                        ) { Text(stringResource(R.string.ok)) }
                        TextButton(
                            onClick = {
                                onDismiss()
                                selectionList = initialSelection.toMutableList()
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = VividBlue,
                                containerColor = MaterialWhite
                            ),
                            border = BorderStroke(width = 1.dp, color = VividBlue)
                        ) { Text(stringResource(R.string.dismiss)) }
                    }
                }
            }
        }
    )
}

/**
 * Элемент списка с чекбоксом.
 * @param data объект, отображаемый элементом
 * @param onCheckOn коллбэк при активном чекбоксе
 * @param onCheckOff коллбэк при неактивном чекбоксе
 */
@Composable
private fun SelectionItem(
    data: Any,
    onCheckOn: () -> Unit,
    onCheckOff: () -> Unit
) {
    var checkedState by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        modifier = Modifier
            .background(MaterialWhite)
            .border(0.75.dp, if (checkedState) VividBlue else OptionTextGrey)
            .fillMaxWidth()
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = {
                checkedState = it
                if (checkedState) onCheckOn() else onCheckOff()
            },
            colors = CheckboxDefaults.colors(
                checkmarkColor = MaterialWhite,
                checkedColor = VividBlue
            ),
            modifier = Modifier.padding(end = 7.dp)
        )
        Text(
            text = data.toString(),
            color = if (checkedState) VividBlue else OptionTextGrey,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.rubik_regular)),
            modifier = Modifier.padding(end = 7.dp)
        )
    }
}