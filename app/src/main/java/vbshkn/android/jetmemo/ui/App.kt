package vbshkn.android.jetmemo.ui

import android.app.Application
import vbshkn.android.jetmemo.data.MainDB

class App: Application() {
    val database by lazy { MainDB.createDB(this) } // singleton-объект ДБ

}