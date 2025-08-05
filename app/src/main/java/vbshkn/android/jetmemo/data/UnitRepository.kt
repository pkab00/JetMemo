package vbshkn.android.jetmemo.data

import kotlinx.coroutines.flow.Flow
import kotlin.concurrent.Volatile

class UnitRepository(
    private val wordDao: WordDao,
    private val relationsDao: RelationsDao
) {

    fun getAllFromUnit(id: Int): Flow<List<WordEntity>> {
        return if (id == -1) wordDao.getAll() else relationsDao.getWordsInUnit(id)
    }

    suspend fun addWord(
        word: WordEntity,
        unitID: Int
    ) {
        wordDao.insertWord(word) // если такое слово уже есть - будет пропущено
        relationsDao.insertRelation(RelationsEntity(word.id, unitID))
    }

    suspend fun editWord(word: WordEntity) {
        wordDao.updateWord(word)
    }

    suspend fun deleteWordFromUnit(
        word: WordEntity,
        unitID: Int
    ) {
        relationsDao.deleteRelation(RelationsEntity(word.id, unitID))
    }

    suspend fun deleteWordCompletely(word: WordEntity) {
        wordDao.deleteWord(word)
    }

    companion object {
        @Volatile
        var INSTANCE: UnitRepository? = null

        fun getInstance(
            wordDao: WordDao,
            relationsDao: RelationsDao
        ): UnitRepository {
            return INSTANCE ?: synchronized(this) {
                val repo = UnitRepository(wordDao, relationsDao)
                INSTANCE = repo
                repo
            }
        }
    }
}