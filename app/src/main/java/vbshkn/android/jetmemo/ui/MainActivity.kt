package vbshkn.android.jetmemo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import vbshkn.android.jetmemo.model.HomeScreenModel
import vbshkn.android.jetmemo.model.HomeScreenModelFactory
import vbshkn.android.jetmemo.model.LearningEndScreenModel
import vbshkn.android.jetmemo.model.LearningEndScreenModelFactory
import vbshkn.android.jetmemo.model.LearningScreenModel
import vbshkn.android.jetmemo.model.LearningScreenModelFactory
import vbshkn.android.jetmemo.model.UnitScreenModel
import vbshkn.android.jetmemo.model.UnitScreenModelFactory
import vbshkn.android.jetmemo.ui.screen.HomeScreen
import vbshkn.android.jetmemo.ui.screen.LearningEndScreen
import vbshkn.android.jetmemo.ui.screen.LearningScreen
import vbshkn.android.jetmemo.ui.screen.UnitScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as App

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Router.HomeRoute,
                enterTransition = { enterAnimation(400) },
                exitTransition = { exitAnimation(400) }
            ) {
                composable<Router.HomeRoute> {
                    val viewModel: HomeScreenModel = viewModel(
                        factory = HomeScreenModelFactory(app.homeRepository)
                    )
                    HomeScreen(
                        viewModel = viewModel,
                        controller = navController
                    )
                }
                composable<Router.UnitRoute> {
                    // данные из data class'а принимаем здесь
                    val data = it.toRoute<Router.UnitRoute>()
                    val viewModel: UnitScreenModel = viewModel(
                        factory = UnitScreenModelFactory(app.unitRepository, data.id),
                        key = "unit_${data.id}"
                    )
                    UnitScreen(
                        name = data.name,
                        viewModel = viewModel,
                        controller = navController,
                    )
                }
                composable<Router.LearningRoute> {
                    val data = it.toRoute<Router.LearningRoute>()
                    val viewModel: LearningScreenModel = viewModel(
                        factory = LearningScreenModelFactory(app.learningRepository, navController, data.id)
                    )
                    LearningScreen(
                        model = viewModel
                    )
                }
                composable<Router.LearningEndRoute> {
                    val data = it.toRoute<Router.LearningEndRoute>()

                    val viewModel: LearningEndScreenModel = viewModel(
                        factory = LearningEndScreenModelFactory(
                            navController = navController,
                            total = data.total,
                            mistakes = data.mistakes,
                            timeString = data.timeString,
                        )
                    )
                    LearningEndScreen(
                        model = viewModel
                    )
                }
            }
        }
    }
}

fun enterAnimation(duration: Int): EnterTransition {
    return fadeIn(
        animationSpec = tween(300, easing = LinearEasing)
    ) + slideIn(
        animationSpec = tween(400, easing = LinearEasing),
        initialOffset = { IntOffset(400, 100) }
    )
}

fun exitAnimation(duration: Int): ExitTransition {
    return fadeOut(
        animationSpec = tween(300, easing = LinearEasing)
    ) + slideOut(
        animationSpec = tween(400, easing = LinearEasing),
        targetOffset = { IntOffset(400, 100) }
    )
}