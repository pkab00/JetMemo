package vbshkn.android.jetmemo.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.logic.Answer
import vbshkn.android.jetmemo.logic.Exercise
import vbshkn.android.jetmemo.model.LearnScreenModel
import vbshkn.android.jetmemo.ui.theme.CorrectGreen
import vbshkn.android.jetmemo.ui.theme.WrongRed

object ExerciseViews {
    @Composable
    fun RightOptionView(model: LearnScreenModel) {

    }

    @Composable
    fun MatchPairsView(model: LearnScreenModel) {

    }

    @Composable
    fun IsCorrectTranslationView(model: LearnScreenModel) {
        val _ex = remember { mutableStateOf(model.currentExercise as Exercise.IsCorrectTranslationExercise) }
        val ex by _ex
        // DOES NOT UPDATE THE SCREEN
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
                    text = ex.givenWord.original,
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    modifier = Modifier.padding(bottom = 20.dp),
                )
                Text(
                    text = ex.givenWord.translation,
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
                            if(model.stateAt(0)?.clickable == true){
                                val result = model.checkAnswer(Answer.YesNo(true))
                                model.showBottomBar(result)
                                model.stateAt(0)!!.clickable = false
                            }
                        }
                )
                Icon(
                    painter = painterResource(R.drawable.ic_cross),
                    contentDescription = "",
                    tint = WrongRed,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            if(model.stateAt(1)?.clickable == true){
                                val result = model.checkAnswer(Answer.YesNo(false))
                                model.showBottomBar(result)
                                model.stateAt(1)!!.clickable = false
                            }
                        }
                )
            }
        }
    }
}