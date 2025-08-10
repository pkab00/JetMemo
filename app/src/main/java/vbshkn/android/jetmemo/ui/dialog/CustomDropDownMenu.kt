package vbshkn.android.jetmemo.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.ui.theme.MaterialWhite
import vbshkn.android.jetmemo.ui.theme.OptionTextGrey

data class MenuAction(
    val title: String,
    val onClick: () -> Unit
)

@Composable
fun CustomDropDownMenu(
    show: Boolean,
    onDismiss: () -> Unit,
    offset: IntOffset,
    actions: List<MenuAction>
) {
    if (show) {
        Box(Modifier.offset{ offset }){
            DropdownMenu(
                expanded = show,
                onDismissRequest = { onDismiss() },
                modifier = Modifier
                    .background(MaterialWhite)
            ) {
                actions.forEach { action ->
                    CustomMenuItem(action)
                }
            }
        }
    }
}

@Composable
private fun CustomMenuItem(action: MenuAction){
    DropdownMenuItem(
        text = {
            Text(
                text = action.title,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.rubik_regular)),
                color = OptionTextGrey
            )
        },
        onClick = { action.onClick() }
    )
}

//@Preview
//@Composable
//private fun Preview(){
//    var show by remember { mutableStateOf(true) }
//
//    val items = listOf(
//        MenuAction("First"){},
//        MenuAction("Second"){},
//        MenuAction("Third"){}
//    )
//    CustomDropDownMenu(
//        show = show,
//        onDismiss = { show = false },
//        actions = items,
//        offset = DpOffset.Zero
//    )
//}