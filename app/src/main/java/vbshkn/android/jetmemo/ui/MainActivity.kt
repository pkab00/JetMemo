package vbshkn.android.jetmemo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import vbshkn.android.jetmemo.model.HomeScreenModel
import vbshkn.android.jetmemo.model.HomeScreenModelFactory
import vbshkn.android.jetmemo.model.UnitScreenModel
import vbshkn.android.jetmemo.model.UnitScreenModelFactory
import vbshkn.android.jetmemo.ui.screen.HomeScreen
import vbshkn.android.jetmemo.ui.screen.UnitScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val store = this.viewModelStore
        val app = application as App

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            var viewModel: ViewModel? = null

            NavHost(
                navController = navController,
                startDestination = Router.HomeRoute,
                enterTransition = { enterAnimation(300) },
                exitTransition = { exitAnimation(300) }
            ) {
                composable<Router.HomeRoute> {
                    viewModel = ViewModelProvider(
                        store,
                        HomeScreenModelFactory(app.homeRepository),
                    )[HomeScreenModel::class.java]
                    HomeScreen(
                        viewModel = viewModel as HomeScreenModel,
                        controller = navController
                    )
                }
                composable<Router.UnitRoute> {
                    // данные из data class'а принимаем здесь
                    val data = it.toRoute<Router.UnitRoute>()
                    viewModel = ViewModelProvider(
                        store,
                        UnitScreenModelFactory(app.unitRepository, data.id)
                    )[UnitScreenModel::class.java]
                    UnitScreen(
                        name = data.name,
                        viewModel = viewModel as UnitScreenModel,
                        controller = navController,
                    )
                }
            }
        }
    }
}

fun enterAnimation(duration: Int): EnterTransition {
    return fadeIn(
        animationSpec = tween(duration, easing = EaseOut)
    ) + slideInHorizontally(
        animationSpec = tween(duration, easing = EaseOut),
    )
}

fun exitAnimation(duration: Int): ExitTransition {
    return fadeOut(
        animationSpec = tween(duration, easing = EaseIn)
    ) + slideOutHorizontally(
        animationSpec = tween(duration, easing = EaseIn)
    )
}