package vbshkn.android.jetmemo.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.model.LearningEndScreenModel
import vbshkn.android.jetmemo.ui.theme.OptionTextGrey
import vbshkn.android.jetmemo.ui.theme.VividBlue

@Composable
fun LearningEndScreen(model: LearningEndScreenModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBlock(Modifier.weight(1f), model)
        BottomBlock(model)
    }
}

@Composable
fun StatisticsBlock(model: LearningEndScreenModel) {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = OptionTextGrey
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 50.dp,
                end = 50.dp,
                //top = 75.dp,
                //bottom = 75.dp
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            StatisticsItem("${stringResource(R.string.statistics_time)} ${model.timeString}")
            StatisticsItem("${stringResource(R.string.statistics_total)} ${model.total}")
            StatisticsItem("${stringResource(R.string.statistics_mistakes)} ${model.mistakes}")
        }
    }
}

@Composable
fun StatisticsItem(txt: String) {
    Text(
        text = txt,
        fontSize = 24.sp,
        fontFamily = FontFamily(Font(R.font.rubik_regular))
    )
}

@Composable
fun TopBlock(
    modifier: Modifier,
    model: LearningEndScreenModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(50.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .padding(top = 100.dp)
        ){
            Text(
                text = stringResource(R.string.training_completed),
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontSize = 36.sp,
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            )
        }
        StatisticsBlock(model)
    }
}

@Composable
fun BottomBlock(model: LearningEndScreenModel) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { model.exit() },
            colors = ButtonDefaults.buttonColors(
                containerColor = VividBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 50.dp,
                    end = 50.dp,
                    top = 75.dp,
                    bottom = 75.dp
                )
                .height(50.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.ok),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_regular))
                )
            }
        }
    }
}