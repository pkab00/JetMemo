package vbshkn.android.jetmemo.ui.screen

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.logic.Answer
import vbshkn.android.jetmemo.logic.Exercise
import vbshkn.android.jetmemo.model.LearnScreenModel
import vbshkn.android.jetmemo.model.sub.ApproveTranslationSubModel
import vbshkn.android.jetmemo.model.sub.MatchPairsSubModel
import vbshkn.android.jetmemo.ui.theme.CorrectGreen
import vbshkn.android.jetmemo.ui.theme.OptionTextGrey
import vbshkn.android.jetmemo.ui.theme.WrongRed

object ExerciseViews {
    @Composable
    fun RightOptionView(model: LearnScreenModel) {
        Text("TODO")
    }

    @Composable
    fun MatchPairsView(model: LearnScreenModel) {
        val subModel = model.currentSubModel as? MatchPairsSubModel ?: return
        val leftColumnStates by subModel.leftColumnStates.collectAsState()
        val rightColumnStates by subModel.rightColumnStates.collectAsState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(10.dp)
                ) {
                    for (i in subModel.leftColumnWords.indices) {
                        PairCardItem(
                            text = subModel.leftColumnWords[i],
                            state = leftColumnStates[i]
                        ) { subModel.onLeftClicked(i) }
                    }
                }
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(10.dp)
                ) {
                    for (i in subModel.rightColumnWords.indices) {
                        PairCardItem(
                            text = subModel.rightColumnWords[i],
                            state = rightColumnStates[i]
                        ) { subModel.onRightClicked(i) }
                    }
                }
            }
        }
    }

    @Composable
    fun ApproveTranslationView(model: LearnScreenModel) {
        val subModel = model.currentSubModel as? ApproveTranslationSubModel ?: return
        val exercise by model.currentExercise.collectAsState()
        val word = (exercise as Exercise.ApproveTranslationExercise).givenWord

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
                .border(0.5.dp, Color.Black)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = word.original,
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    modifier = Modifier.padding(bottom = 20.dp),
                )
                Text(
                    text = word.translation,
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                    modifier = Modifier.padding(bottom = 50.dp),
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_checkmark),
                    contentDescription = "",
                    tint = CorrectGreen,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            if (subModel.isClickable(0)) subModel.onClicked(true)
                        }
                )
                Icon(
                    painter = painterResource(R.drawable.ic_cross),
                    contentDescription = "",
                    tint = WrongRed,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            if (subModel.isClickable(1)) subModel.onClicked(false)
                        }
                )
            }
        }
    }

    /// OTHER COMPOSABLE ///

    @Composable
    private fun PairCardItem(
        text: String,
        state: LearnScreenModel.ElementState,
        onClick: () -> Unit
    ) {
        Button(
            onClick = { if (state.clickable) onClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = state.color
            ),
            modifier = Modifier
                .width(150.dp)
                .border(0.4.dp, state.color)
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.nunito_regular))
            )
        }
    }

    @Composable
    fun OptionItem(
        number: Int,
        text: String,
        state: LearnScreenModel.ElementState,
        onClick: () -> Unit
    ) {
        Button(
            onClick = { if (state.clickable) onClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = state.color
            ),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp)
                .border(
                    width = 1.5.dp,
                    color = state.color,
                    shape = RoundedCornerShape(30.dp))
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Button(
                    onClick = { if (state.clickable) onClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = state.color,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .size(50.dp)
                ) {
                    Text(
                        text = number.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_semibold))
                    )
                }
                Spacer(Modifier.width(25.dp))
                Text(
                    text = text,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_regular))
                )
            }
        }
    }
}