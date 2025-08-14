package vbshkn.android.jetmemo.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.Volatile

class LearnRepository(
    private val wordDao: WordDao,
    private val relationsDao: RelationsDao
) {
    fun getWordsInUnit(unitID: Int): List<WordEntity> {
        // возвращаем первое значение из списка, таким образом избавляясь от flow
        return if (unitID == -1) runBlocking { wordDao.getAll().first() }
        else runBlocking { relationsDao.getWordsInUnit(unitID).first() }
    }

    companion object {
        @Volatile
        private var INSTANCE: LearnRepository? = null

        fun getInstance(
            wordDao: WordDao,
            relationsDao: RelationsDao
        ): LearnRepository {
            return INSTANCE ?: synchronized(this) {
                val repo = LearnRepository(wordDao, relationsDao)
                INSTANCE = repo
                repo
            }
        }
    }
}