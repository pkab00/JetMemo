package vbshkn.android.jetmemo.ui

import android.app.Application
import vbshkn.android.jetmemo.data.MainDB
import vbshkn.android.jetmemo.data.HomeRepository
import vbshkn.android.jetmemo.data.LearnRepository
import vbshkn.android.jetmemo.data.UnitRepository

class App: Application() {
    private val database by lazy { MainDB.createDB(this) } // singleton-объект ДБ
    val homeRepository by lazy { HomeRepository.getInstance(database.unitDao()) }
    val unitRepository by lazy { UnitRepository.getInstance(database.wordDao(), database.relationsDao()) }
    val learnRepository by lazy { LearnRepository.getInstance(database.wordDao(), database.relationsDao()) }
}