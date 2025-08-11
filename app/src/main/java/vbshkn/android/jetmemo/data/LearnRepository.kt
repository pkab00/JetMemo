package vbshkn.android.jetmemo.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.Volatile

class LearnRepository(private val relationsDao: RelationsDao) {
    fun getWordsInUnit(unitID: Int) : List<WordEntity> {
        // возвращаем первое значение из списка, таким образом избавляясь от flow
        return runBlocking { relationsDao.getWordsInUnit(unitID).first() }
    }

    companion object{
        @Volatile
        private var INSTANCE: LearnRepository? = null

        fun getInstance(relationsDao: RelationsDao): LearnRepository{
            return INSTANCE ?: synchronized(this){
                val repo = LearnRepository(relationsDao)
                INSTANCE = repo
                repo
            }
        }
    }
}