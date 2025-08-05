package vbshkn.android.jetmemo.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.data.WordEntity
import vbshkn.android.jetmemo.model.UnitScreenModel
import vbshkn.android.jetmemo.ui.Router
import vbshkn.android.jetmemo.ui.dialog.DoubleTextInputDialog
import vbshkn.android.jetmemo.ui.theme.MaterialWhite
import vbshkn.android.jetmemo.ui.theme.OptionTextGrey
import vbshkn.android.jetmemo.ui.theme.VividBlue

@Composable
fun UnitScreen(
    name: String,
    viewModel: UnitScreenModel,
    controller: NavController
) {
    val words by viewModel.unitWords.collectAsState()
    val isEmpty = words.isEmpty()

    DialogHost(viewModel)

    Scaffold(
        topBar = {
            TopBar(
                title = name,
                onAdd = { viewModel.showDialog(UnitScreenModel.DialogState.AddWordDialog) },
                onNavigate = {
                    controller.navigate(Router.HomeRoute) {
                        popUpTo(0)
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                show = !isEmpty,
                onLearn = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialWhite)
        ) {
            WordList(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onAdd: () -> Unit,
    onNavigate: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 22.sp,
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
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable { onAdd() }
            )
        },
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_home),
                contentDescription = "",
                tint = MaterialWhite,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .clickable { onNavigate() }
            )
        }
    )
}

@Composable
fun BottomBar(
    show: Boolean,
    onLearn: () -> Unit
) {
    BottomAppBar(
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(if (show) VividBlue else MaterialWhite)
        ) {
            if (show) {
                Button(
                    onClick = { onLearn() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialWhite,
                        contentColor = VividBlue
                    ),
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 30.dp,
                            end = 30.dp,
                            top = 15.dp,
                            bottom = 15.dp
                        )
                ) {
                    Text(
                        text = stringResource(R.string.statr_learning).uppercase(),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.rubik_regular))
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyUnitFiller() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.message_empty_unit),
            color = OptionTextGrey,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WordList(viewModel: UnitScreenModel) {
    val words by viewModel.unitWords.collectAsState()

    if (words.isNotEmpty()) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MaterialWhite)
                .fillMaxSize()
                .padding(10.dp)
        ) {
            items(words) { word ->
                WordItem(word)
            }
        }
    } else EmptyUnitFiller()
}

@Composable
fun WordItem(word: WordEntity) {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialWhite,
            contentColor = VividBlue
        ),
        contentPadding = PaddingValues(5.dp),
        shape = RectangleShape,
        border = BorderStroke(0.5.dp, VividBlue),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(MaterialWhite)
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 5.dp,
                    bottom = 5.dp
                )
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = word.original,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                    modifier = Modifier
                        .background(MaterialWhite)
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = word.translation,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    color = OptionTextGrey,
                    modifier = Modifier
                        .background(MaterialWhite)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_more),
                    contentDescription = "",
                    tint = VividBlue
                )
            }
        }
    }
}

@Composable
private fun DialogHost(model: UnitScreenModel){
    when(val dialogState = model.dialogState){
        is UnitScreenModel.DialogState.AddWordDialog -> {
            DoubleTextInputDialog(
                onConfirm = {str1, str2 ->
                    model.addWord(WordEntity(original = str1, translation = str2))
                },
                onDismiss = { model.dismissDialog() },
                title = stringResource(R.string.add_new_word),
                firstInitialValue = stringResource(R.string.placeholder_original_word),
                secondInitialValue = stringResource(R.string.placeholder_translated_word)
            )
        }
        is UnitScreenModel.DialogState.None -> Unit
    }
}