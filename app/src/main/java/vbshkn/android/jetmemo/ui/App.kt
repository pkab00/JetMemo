package vbshkn.android.jetmemo.ui

import android.app.Application
import vbshkn.android.jetmemo.data.MainDB
import vbshkn.android.jetmemo.data.UnitRepository

class App: Application() {
    val database by lazy { MainDB.createDB(this) } // singleton-объект ДБ
    val unitRepository by lazy { UnitRepository.getInstance(database.unitDao()) }

}