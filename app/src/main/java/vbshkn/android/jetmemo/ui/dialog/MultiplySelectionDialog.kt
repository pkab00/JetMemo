package vbshkn.android.jetmemo.ui.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.data.UnitEntity
import vbshkn.android.jetmemo.ui.theme.HeaderBlueGrey
import vbshkn.android.jetmemo.ui.theme.MaterialWhite
import vbshkn.android.jetmemo.ui.theme.OptionTextGrey
import vbshkn.android.jetmemo.ui.theme.VividBlue

@Composable
fun MultiplySelectionDialog(
    onConfirm: (List<Any>) -> Unit,
    onDismiss: () -> Unit,
    title: String,
    initialSelection: List<Any> = emptyList(),
    items: List<Any> = emptyList()
) {
    var selectionList by remember(initialSelection) {
        mutableStateOf(initialSelection.toMutableList())
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = MaterialWhite,
        title = {
            Text(
                text = title,
                color = HeaderBlueGrey,
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.nunito_semibold))
            )
        },
        text = {
            LazyColumn {
                items(selectionList) { option ->
                    SelectionItem(
                        data = option,
                        onCheckOn = { selectionList.add(option) },
                        onCheckOff = { selectionList.remove(option) }
                    )
                }
            }
        },
        confirmButton = {
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
        },
        dismissButton = {
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
    )
}

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
            .border(0.5.dp, OptionTextGrey)
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
            color = VividBlue,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.rubik_regular)),
            modifier = Modifier.padding(end = 7.dp)
        )
    }
}

//@Preview
//@Composable
//private fun Preview() {
//    val unitEntities = listOf(
//        UnitEntity(id = 1, name = "Урок 1"),
//        UnitEntity(id = 2, name = "Урок 2"),
//        UnitEntity(id = 3, name = "Урок 3")
//    )
//
//    MultiplySelectionDialog(
//        onConfirm = {},
//        onDismiss = {},
//        title = "Выберите уроки",
//        initialSelection = listOf(unitEntities[0]) // по умолчанию первый
//    )
//}