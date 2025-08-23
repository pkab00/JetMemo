package vbshkn.android.jetmemo.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.Volatile

/**
 * Промежуточный слой абстракции между LearningScreenModel и базой данных Room.
 * @param wordDao объект для работы с таблицей слов
 * @param relationsDao объект для работы с таблицей связей
 */
class LearningRepository(
    private val wordDao: WordDao,
    private val relationsDao: RelationsDao
) {
    /**
     * Запрос списка слов, содержащихся в юните.
     * Получаем только первое значение Flow, так как нет необходимости отслеживать
     * изменения списка.
     * @param unitID идентификатор юнита
     * @return список слов юнита
     */
    fun getWordsInUnit(unitID: Int): List<WordEntity> {
        // возвращаем первое значение из списка, таким образом избавляясь от flow
        return if (unitID == -1) runBlocking { wordDao.getAll().first() }
        else runBlocking { relationsDao.getWordsInUnit(unitID).first() }
    }

    companion object {
        @Volatile
        private var INSTANCE: LearningRepository? = null

        /**
         * Создание и/или запрос singleton-объекта репозитория.
         */
        fun getInstance(
            wordDao: WordDao,
            relationsDao: RelationsDao
        ): LearningRepository {
            return INSTANCE ?: synchronized(this) {
                val repo = LearningRepository(wordDao, relationsDao)
                INSTANCE = repo
                repo
            }
        }
    }
}