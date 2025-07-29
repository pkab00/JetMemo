package vbshkn.android.jetmemo.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.data.UnitEntity
import vbshkn.android.jetmemo.model.MainActivityViewModel
import vbshkn.android.jetmemo.model.MainActivityViewModelFactory
import vbshkn.android.jetmemo.ui.App
import vbshkn.android.jetmemo.ui.TextInputDialog
import vbshkn.android.jetmemo.ui.theme.VividBlue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as App
        val repository = app.unitRepository
        val viewModel = ViewModelProvider(
            this,
            MainActivityViewModelFactory(repository),
        ) [MainActivityViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            MainScreen(viewModel)
        }
    }
}

@Composable
fun MainScreen(viewModel: MainActivityViewModel, ){
    var showAddDialog by remember { mutableStateOf(false) }
    var editModeOn by remember { mutableStateOf(false) }

    TextInputDialog(
        isOpen = showAddDialog,
        onDismiss = {showAddDialog = false},
        onConfirm = { unitName -> viewModel.addUnit(unitName)},
        title = stringResource(R.string.add_new_unit)
    )

    Scaffold(
        topBar = {
            TopBar(
                onAddClicked = { showAddDialog = true }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            YourUnitsLabel({ editModeOn = !editModeOn }, editModeOn)
            UnitList(viewModel, editModeOn)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onAddClicked: () -> Unit
)
{
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
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
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
                        onClick = {}
                    )
            )
        }
    )
}

@Composable
fun YourUnitsLabel(
    onEditClicked: () -> Unit,
    editMode: Boolean
){
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ){
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
                    .background(Color.White)
            )
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.weight(0.5f)
        ) {
            Icon(
                painter = painterResource(
                    if(editMode) R.drawable.ic_edit_mode_off
                    else R.drawable.ic_edit_mode_on
                ),
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .padding(end = 5.dp, top = 5.dp)
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(VividBlue)
                    .clickable { onEditClicked() }
            )
        }
    }
}

@Composable
fun UnitList(
    viewModel: MainActivityViewModel,
    editMode: Boolean
){
    val units by viewModel.allUnits.collectAsState()
    val allWordsUnit = UnitEntity(id = -1, name = stringResource(R.string.all_words))
    val allUnits = remember(units) { listOf(allWordsUnit) + units }

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 10.dp)
            .background(Color.White)
            .fillMaxSize()
    ) {
        items(allUnits) {
            unit -> UnitButton(unit.name, editMode)
        }
    }
}

@Composable
fun UnitButton(
    text: String,
    isEditable: Boolean
){
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(
            containerColor = VividBlue,
            contentColor = Color.White,
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
                        if(text == stringResource(R.string.all_words))
                            R.drawable.ic_default_folder
                        else R.drawable.ic_folder
                    ),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_semibold))
                )
            }
            if(isEditable && text != stringResource(R.string.all_words)){
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(0.4f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .clickable(onClick = {})
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "",
                        modifier = Modifier
                            .padding()
                            .clickable(onClick = {})
                    )
                }
            }
        }
    }
}