package vbshkn.android.jetmemo.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.data.UnitEntity
import vbshkn.android.jetmemo.model.HomeScreenModel
import vbshkn.android.jetmemo.model.HomeScreenModel.*
import vbshkn.android.jetmemo.model.SortMode
import vbshkn.android.jetmemo.ui.Router
import vbshkn.android.jetmemo.ui.dialog.ConfirmDialog
import vbshkn.android.jetmemo.ui.dialog.InfoDialog
import vbshkn.android.jetmemo.ui.dialog.InputLimit
import vbshkn.android.jetmemo.ui.dialog.TextInputDialog
import vbshkn.android.jetmemo.ui.theme.MaterialWhite
import vbshkn.android.jetmemo.ui.theme.VividBlue

@Composable
fun HomeScreen(
    viewModel: HomeScreenModel,
    controller: NavController
) {
    var editModeOn by remember { mutableStateOf(false) }
    var sortMode by remember { mutableStateOf(SortMode.NEW_TO_OLD) }

    DialogHost(viewModel)

    Scaffold(
        topBar = {
            TopBar(
                onAddClicked = { viewModel.showDialog(DialogState.AddUnitDialog) },
                onAboutClicked = { viewModel.showDialog(DialogState.AboutDialog) }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialWhite)
        ) {
            TitleArea(
                onEdit = { editModeOn = !editModeOn },
                onSort = {
                    when (sortMode) {
                        SortMode.NEW_TO_OLD -> sortMode = SortMode.OLD_TO_NEW
                        SortMode.OLD_TO_NEW -> sortMode = SortMode.NEW_TO_OLD
                    }
                },
                editMode = editModeOn,
                sortMode = sortMode
            )
            UnitList(
                viewModel = viewModel,
                controller = controller,
                editMode = editModeOn,
                sortMode = sortMode
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onAddClicked: () -> Unit,
    onAboutClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = VividBlue,
            titleContentColor = MaterialWhite,
            actionIconContentColor = MaterialWhite
        ),
        actions = {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = stringResource(R.string.add_new_unit),
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable(
                        onClick = onAddClicked
                    )
            )
            Spacer(Modifier.width(22.dp))
            Icon(
                painter = painterResource(R.drawable.ic_info),
                contentDescription = stringResource(R.string.about_app),
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable(
                        onClick = onAboutClicked
                    )
            )
        }
    )
}

@Composable
fun TitleArea(
    onEdit: () -> Unit,
    onSort: () -> Unit,
    sortMode: SortMode,
    editMode: Boolean
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.your_units),
                color = VividBlue,
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, top = 5.dp)
                    .background(MaterialWhite)
            )
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.weight(0.5f)
        ) {
            Icon(
                painter = painterResource(
                    if (sortMode == SortMode.OLD_TO_NEW) R.drawable.ic_arrow_up
                    else R.drawable.ic_arrow_down
                ),
                contentDescription = "",
                tint = MaterialWhite,
                modifier = Modifier
                    .padding(end = 15.dp, top = 5.dp)
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(VividBlue)
                    .clickable { onSort() }
            )
            Icon(
                painter = painterResource(
                    if (editMode) R.drawable.ic_edit_mode_off
                    else R.drawable.ic_edit_mode_on
                ),
                contentDescription = "",
                tint = MaterialWhite,
                modifier = Modifier
                    .padding(top = 5.dp, end = 5.dp)
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(VividBlue)
                    .clickable { onEdit() }
            )
        }
    }
}

@Composable
fun UnitList(
    viewModel: HomeScreenModel,
    controller: NavController,
    editMode: Boolean,
    sortMode: SortMode
) {
    val units by viewModel.allUnits.collectAsState()
    val sortedUnits =
        if (sortMode == SortMode.NEW_TO_OLD) units.sortedBy { it.createdAt }
        else units.sortedByDescending { it.createdAt }
    val allWordsUnit = UnitEntity(id = -1, name = stringResource(R.string.all_words))
    val allUnits = remember(sortedUnits) { listOf(allWordsUnit) + sortedUnits }


    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 10.dp)
            .background(MaterialWhite)
            .fillMaxSize()
    ) {
        items(allUnits) { unit ->
            UnitButton(
                unit = unit,
                isEditable = editMode,
                onClick = { controller.navigate(Router.UnitRoute(unit.id, unit.name)) },
                onEdit = { viewModel.showDialog(DialogState.EditUnitDialog(unit)) },
                onDelete = { viewModel.showDialog(DialogState.DeleteUnitDialog(unit)) }
            )
        }
    }
}

@Composable
fun UnitButton(
    unit: UnitEntity,
    isEditable: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = VividBlue,
            contentColor = MaterialWhite,
        ),
        border = BorderStroke(1.dp, VividBlue),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(
                        if (unit.name == stringResource(R.string.all_words))
                            R.drawable.ic_default_folder
                        else R.drawable.ic_folder
                    ),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = unit.name,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_semibold))
                )
            }
            if (isEditable && unit.name != stringResource(R.string.all_words)) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(0.4f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .clickable(onClick = { onEdit() })
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "",
                        modifier = Modifier
                            .padding()
                            .clickable(onClick = { onDelete() })
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogHost(viewModel: HomeScreenModel) {
    when (val state = viewModel.dialogState) {
        is DialogState.AboutDialog -> {
            InfoDialog(
                onConfirm = {},
                onDismiss = { viewModel.dismissDialog() },
                title = stringResource(R.string.title_about),
                message = stringResource(R.string.about_app)
            )
        }

        is DialogState.AddUnitDialog -> {
            TextInputDialog(
                onDismiss = { viewModel.dismissDialog() },
                onConfirm = { unitName -> viewModel.addUnit(unitName) },
                title = stringResource(R.string.add_new_unit),
                inputLimit = InputLimit.UNIT_NAME
            )
        }

        is DialogState.EditUnitDialog -> {
            TextInputDialog(
                onDismiss = { viewModel.dismissDialog() },
                onConfirm = { newName -> viewModel.editUnit(state.unit.copy(name = newName)) },
                title = stringResource(R.string.edit_unit),
                initialValue = state.unit.name,
                inputLimit = InputLimit.UNIT_NAME
            )
        }

        is DialogState.DeleteUnitDialog -> {
            ConfirmDialog(
                onDismiss = { viewModel.dismissDialog() },
                onConfirm = { viewModel.deleteUnit(state.unit) },
                title = stringResource(R.string.delete_unit),
                message = stringResource(R.string.cant_be_canceled)
            )
        }

        is DialogState.None -> Unit
    }
}