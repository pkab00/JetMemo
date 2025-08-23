package vbshkn.android.jetmemo.ui

import android.app.Application
import vbshkn.android.jetmemo.data.MainDB
import vbshkn.android.jetmemo.data.HomeRepository
import vbshkn.android.jetmemo.data.LearningRepository
import vbshkn.android.jetmemo.data.UnitRepository

/**
 * Кастомный подкласс Application.
 * Хранит объект базы данных, а также все необходимые репозитории.
 * Все объекты используют ленивую инициализацию.
 */
class App: Application() {
    private val database by lazy { MainDB.createDB(this) }

    val homeRepository by lazy { HomeRepository.getInstance(database.unitDao()) }
    val unitRepository by lazy { UnitRepository.getInstance(database.wordDao(), database.relationsDao()) }
    val learningRepository by lazy { LearningRepository.getInstance(database.wordDao(), database.relationsDao()) }
}