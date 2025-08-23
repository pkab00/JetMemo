package vbshkn.android.jetmemo.data

import kotlinx.coroutines.flow.Flow
import kotlin.concurrent.Volatile

/**
 * Промежуточный слой абстракции между UnitScreenModel и базой данных Room.
 * @param wordDao объект для работы с таблицей слов
 * @param relationsDao объект для работы с таблицей связей
 */
class UnitRepository(
    private val wordDao: WordDao,
    private val relationsDao: RelationsDao
) {
    /**
     * Запрос FLow со словами, содержащимися в юните.
     * Если id юнита равен -1, использует wordDao для обращения к общей таблице слов.
     * @param id идентификатор юнита
     * @return Flow со списком слов
     */
    fun getAllFromUnit(id: Int): Flow<List<WordEntity>> {
        return if (id == -1) wordDao.getAll() else relationsDao.getWordsInUnit(id)
    }

    /**
     * Добавление слова в юнит.
     * @param word объект слова
     * @param unitID идентификатор юнита
     */
    suspend fun addWord(
        word: WordEntity,
        unitID: Int
    ) {
        // создаём слово только если такого ещё не существует
        if (!wordDao.hasWord(word.original, word.translation)) {
            wordDao.insertWord(word)
        }
        // если добавляем не в общий список слов
        if(unitID != -1){
            // запрашиваем идентификатор слова и создаём связь
            val newID = wordDao.getWordID(word.original, word.translation)
            relationsDao.insertRelation(RelationsEntity(newID, unitID))
        }
    }

    /**
     * Изменение слова.
     * @param word слово, которое должно быть изменено
     */
    suspend fun editWord(word: WordEntity) {
        wordDao.updateWord(word)
    }

    /**
     * Удаление слова из юнита.
     * @param word слово, которое должно быть удалено
     * @param unitID идентификатор юнита
     */
    suspend fun deleteWordFromUnit(
        word: WordEntity,
        unitID: Int
    ) {
        relationsDao.deleteRelation(RelationsEntity(word.id, unitID))
    }

    /**
     * Полдное удаление слова из общей базы с уничтожением всех связей.
     * @param word слово, которое должно быть удалено
     */
    suspend fun deleteWordCompletely(word: WordEntity) {
        wordDao.deleteWord(word)
    }

    /**
     * Запрос списка юнитов, в которые может быть добавлено данное слово.
     * @param word объект слова
     * @return список юнитов
     */
    suspend fun getUnitsToAddTo(word: WordEntity): List<UnitEntity>{
        return relationsDao.getUnitsWithNoWord(word.id)
    }

    /**
     * Создание и/или запрос singleton-объекта репозитория.
     */
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