package vbshkn.android.jetmemo.model

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController


/**
 * Модель для управления UnitEndScreen.
 * @param navController объект для навигации между экранами
 * @param total количество пройденых упражнений
 * @param mistakes количество ошибок
 * @param timeString затраченное время в формате строки MM:SS
 */
class LearningEndScreenModel (
    private val navController: NavController,
    val total: Int,
    val mistakes: Int,
    val timeString: String
) : ViewModel() {
    /**
     * Навигация обратно на UnitScreen при выходе из тренажёра.
     */
    fun exit() {
        navController.popBackStack()
    }
}