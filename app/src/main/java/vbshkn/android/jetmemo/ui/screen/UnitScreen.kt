package vbshkn.android.jetmemo.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.data.WordEntity
import vbshkn.android.jetmemo.ui.Router
import vbshkn.android.jetmemo.ui.theme.MaterialWhite
import vbshkn.android.jetmemo.ui.theme.OptionTextGrey
import vbshkn.android.jetmemo.ui.theme.VividBlue

@Composable
fun UnitScreen(
    id: Int,
    wordList: List<WordEntity>,
    controller: NavController
) {
    Scaffold(
        topBar = {
            TopBar(
                id = id,
                onAdd = {},
                onNavigate = {
                    controller.navigate(Router.HomeRoute){
                    popUpTo(0)
                }}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialWhite)
        ) {
            if(wordList.isEmpty()){
                EmptyUnitFiller()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    id: Int,
    onAdd: () -> Unit,
    onNavigate: () -> Unit
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "ID: $id",
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
fun EmptyUnitFiller(){
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