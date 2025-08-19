package vbshkn.android.jetmemo.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.logic.Exercise
import vbshkn.android.jetmemo.model.LearnScreenModel
import vbshkn.android.jetmemo.ui.theme.CorrectGreen
import vbshkn.android.jetmemo.ui.theme.MaterialWhite
import vbshkn.android.jetmemo.ui.theme.VividBlue
import vbshkn.android.jetmemo.ui.theme.WrongRed

@Composable
fun LearnScreen(
    model: LearnScreenModel
) {
    Scaffold(
        bottomBar = { BottomBar(model.showBottomBar, model) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialWhite)
        ) {
            MainArea(model)
        }
    }
}

@Composable
fun MainArea(
    model: LearnScreenModel
) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxSize()
    ) {
        CloseButtonArea(model)
        ExerciseArea(model, Modifier.weight(1f))
        SkipButtonArea(model)
    }
}

@Composable
fun CloseButtonArea(
    model: LearnScreenModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_close),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(10.dp)
                .clickable { model.exit() }
        )
    }
}

@Composable
fun SkipButtonArea(
    model: LearnScreenModel
) {
    val show = model.showSkipButton
    if (show) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.White)
        ) {
            Button(
                onClick = { model.nextExercise() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = VividBlue,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .height(50.dp)
                    .padding(horizontal = 25.dp)
                    .fillMaxWidth(0.8f)
            ) {
                Text(
                    text = stringResource(R.string.button_skip),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    show: Boolean,
    model: LearnScreenModel
) {
    val mainColor = if (model.bottomBarState) CorrectGreen else WrongRed
    val title =
        stringResource(if (model.bottomBarState) R.string.title_correct else R.string.title_wrong)
    val icon =
        painterResource(if (model.bottomBarState) R.drawable.ic_correct else R.drawable.ic_wrong)

    if (show) {
        BottomAppBar(
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.height(150.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(mainColor)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_semibold))
                    )
                }
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            if(model.currentExercise.value.done()){
                                model.nextExercise()
                            }
                            model.hideBottomBar()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = mainColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(
                                bottom = 10.dp,
                                start = 25.dp,
                                end = 25.dp
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.button_continue),
                            color = mainColor,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseArea(
    model: LearnScreenModel,
    modifier: Modifier
) {
    val ex by model.currentExercise.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        when (ex) {
            is Exercise.MatchPairsExercise -> {
                ExerciseViews.MatchPairsView(model)
            }

            is Exercise.RightOptionExercise -> {
                ExerciseViews.RightOptionView(model)
            }

            is Exercise.ApproveTranslationExercise -> {
                ExerciseViews.ApproveTranslationView(model)
            }

            else -> { model.exit() }
        }
    }
}