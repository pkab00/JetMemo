package vbshkn.android.jetmemo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import vbshkn.android.jetmemo.model.HomeScreenModel
import vbshkn.android.jetmemo.model.HomeScreenModelFactory
import vbshkn.android.jetmemo.model.LearnScreenModel
import vbshkn.android.jetmemo.model.LearnScreenModelFactory
import vbshkn.android.jetmemo.model.UnitScreenModel
import vbshkn.android.jetmemo.model.UnitScreenModelFactory
import vbshkn.android.jetmemo.ui.screen.HomeScreen
import vbshkn.android.jetmemo.ui.screen.LearnScreen
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
                composable<Router.LearnRoute> {
                    val data = it.toRoute<Router.LearnRoute>()
                    val viewModel: LearnScreenModel = viewModel(
                        factory = LearnScreenModelFactory(app.learnRepository, navController, data.id)
                    )
                    LearnScreen(
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