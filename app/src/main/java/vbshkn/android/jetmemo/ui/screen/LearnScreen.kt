package vbshkn.android.jetmemo.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.material.bottomappbar.BottomAppBar
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.logic.Exercise
import vbshkn.android.jetmemo.model.LearnScreenModel
import vbshkn.android.jetmemo.model.LearnScreenModelFactory
import vbshkn.android.jetmemo.ui.App
import vbshkn.android.jetmemo.ui.Router
import vbshkn.android.jetmemo.ui.theme.CorrectGreen
import vbshkn.android.jetmemo.ui.theme.MaterialWhite
import vbshkn.android.jetmemo.ui.theme.VividBlue
import vbshkn.android.jetmemo.ui.theme.WrongRed

@Composable
fun LearnScreen(
    model: LearnScreenModel,
    navController: NavController
) {
    val showBottomBar by remember { mutableStateOf(false) }
    val showSkipButton by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = { BottomBar(showBottomBar, model) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialWhite)
        ) {
            MainArea(model, navController)
        }
    }
}

@Composable
fun MainArea(
    model: LearnScreenModel,
    navController: NavController
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            CloseButtonArea(model, navController)
        }
    }
}

@Composable
fun CloseButtonArea(
    model: LearnScreenModel,
    navController: NavController
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_close),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(10.dp)
                .clickable { navController.popBackStack() }
        )
    }
}

@Composable
fun BottomBar(
    show: Boolean,
    model: LearnScreenModel
) {
    val mainColor = if (model.bottomPanelState) CorrectGreen else WrongRed
    val title =
        stringResource(if (model.bottomPanelState) R.string.title_correct else R.string.title_wrong)
    val icon =
        painterResource(if (model.bottomPanelState) R.drawable.ic_correct else R.drawable.ic_wrong)

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
                Spacer(Modifier
                    .fillMaxWidth()
                    .height(15.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { model.nextExercise() },
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
    model: LearnScreenModel
) {
    when (model.currentExercise) {
        is Exercise.MatchPairsQuestion -> {}
        is Exercise.IsCorrectTranslationQuestion -> {}
        is Exercise.RightOptionQuestion -> {}
        null -> {}
    }
}