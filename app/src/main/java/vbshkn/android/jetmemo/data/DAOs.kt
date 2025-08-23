package vbshkn.android.jetmemo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO для взаимодействия с WordEntity.
 * Отвечает за добавление, удаление и изменение данных в общей базе слов.
 * @see WordEntity
 */
@Dao
interface WordDao {
    /**
     * Операция вставки.
     * @param entity слово для вставки
     * @return 1L если слово уже существует в базе.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWord(entity: WordEntity): Long

    /**
     * Операция удаления.
     * @param entity слово для удаления
     */
    @Delete
    suspend fun deleteWord(entity: WordEntity)

    /**
     * Операция изменения.
     * @param entity слово для удаления
     */
    @Update
    suspend fun updateWord(entity: WordEntity)

    /**
     * Запрос всех содержащихся в базе слов.
     * Список отсортирован по убыванию даты создания.
     * @return Flow со списком слов из таблицы main_words_table
     */
    @Query("SELECT * FROM main_words_table ORDER BY createdAt DESC")
    fun getAll(): Flow<List<WordEntity>>

    /**
     * Проверка наличия слова в базе по его содержанию.
     * @param original слово
     * @param translation перевод
     * @return true если слово существует, иначе false
     */
    @Query("""
           SELECT EXISTS
           (SELECT 1 FROM main_words_table
            WHERE original = :original
            AND translation = :translation)
           """)
    suspend fun hasWord(original: String, translation: String): Boolean

    /**
     * Получение ID слова по его содержанию.
     * @param original слово
     * @param translation перевод
     * @return идентификатор
     */
    @Query("""
           SELECT id FROM main_words_table
           WHERE original = :original
           AND translation = :translation
           """)
    suspend fun getWordID(original: String, translation: String): Int
}

/**
 * DAO для взаимодействия с UnitEntity.
 * Отвечает за добавление, удаление и изменение информации о юнитах в таблице units_table.
 * @see UnitEntity
 */
@Dao
interface UnitDao {
    /**
     * Операция вставки.
     * @param entity юнит для вставки
     * @return 1L если юнит уже существует в базе.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUnit(entity: UnitEntity): Long

    /**
     * Операция удаления.
     * @param entity юнит для удаления
     */
    @Delete
    suspend fun deleteUnit(entity: UnitEntity)

    /**
     * Операция изменения.
     * @param entity слово для изменения
     */
    @Update
    suspend fun updateUnit(entity: UnitEntity)

    /**
     * Запрос всех существующих.
     * Список отсортирован по убыванию даты создания.
     * @return Flow со списком юнитов из таблицы units_table
     */
    @Query("SELECT * FROM units_table ORDER BY createdAt DESC")
    fun getAll(): Flow<List<UnitEntity>>
}

/**
 * DAO для взаимодействия с RelationsEntity.
 * Отвечает за добавление, удаление и изменение информации о связях в таблице relations_table.
 * @see RelationsEntity
 */
@Dao
interface RelationsDao {
    /**
     * Операция вставки.
     * @param entity связь для вставки
     * @return 1L если связь уже существует в базе.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelation(entity: RelationsEntity): Long

    /**
     * Операция удаления.
     * @param entity связь для удаления
     */
    @Delete
    suspend fun deleteRelation(entity: RelationsEntity)

    /**
     * Запрос всех слов, пренадлежащих определённому юниту.
     * @param unitID идентификатор юнита
     * @return Flow со списком слов.
     */
    @Query(
        """
        SELECT * FROM main_words_table mwt
        JOIN relations_table rt ON rt.wordID = mwt.id
        WHERE rt.unitID = :unitID
        ORDER BY rt.createdAt DESC
        """
    )
    fun getWordsInUnit(unitID: Int): Flow<List<WordEntity>>

    /**
     * Запрос юнитов, в которых на данный момент отсутствует некоторое слово.
     * Иными словами, метод запрашивает список юнитов, в которые слово может быть добавлено.
     * @param wordID идентификатор слова
     * @return Flow со списком юнитов, доступных для добавления
     */
    @Query(
        """
        SELECT ut.*
        FROM units_table ut
        WHERE NOT EXISTS (
            SELECT 1
            FROM relations_table rt
            WHERE rt.wordID = :wordID
            AND rt.unitID = ut.id
        )
        """
    )
    suspend fun getUnitsWithNoWord(wordID: Int): List<UnitEntity>
}