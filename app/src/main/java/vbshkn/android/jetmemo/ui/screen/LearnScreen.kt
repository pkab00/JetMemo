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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import vbshkn.android.jetmemo.ui.theme.CorrectGreen
import vbshkn.android.jetmemo.ui.theme.MaterialWhite
import vbshkn.android.jetmemo.ui.theme.VividBlue
import vbshkn.android.jetmemo.ui.theme.WrongRed

@Composable
fun LearnScreen(
    model: LearnScreenModel,
    navController: NavController
) {
    Scaffold(
        bottomBar = { BottomBar(true, model) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialWhite)
        ) {
            MainExerciseArea(model)
        }
    }
}

@Composable
fun BottomBar(
    show: Boolean,
    model: LearnScreenModel
) {
    val mainColor = if (model.bottomPanelState) CorrectGreen else WrongRed
    if (show) {
        BottomAppBar(
            contentPadding = PaddingValues(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(mainColor)
            ) { }
        }
    }
}

@Composable
fun MainExerciseArea(
    model: LearnScreenModel
) {
    when (model.currentExercise) {
        is Exercise.MatchPairsQuestion -> {}
        is Exercise.IsCorrectTranslationQuestion -> {}
        is Exercise.RightOptionQuestion -> {}
        null -> {}
    }
}