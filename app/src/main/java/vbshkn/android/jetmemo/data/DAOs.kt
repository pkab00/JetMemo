package vbshkn.android.jetmemo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWord(entity: WordEntity): Long //1L - ignored

    @Delete
    suspend fun deleteWord(entity: WordEntity)

    @Update
    suspend fun updateWord(entity: WordEntity)

    @Query("SELECT * FROM main_words_table ORDER BY createdAt DESC") // Flow является корутиной, которая может использоваться как state
    fun getAll(): Flow<List<WordEntity>> // и самостоятельно обновляется при любых изменениях в ДБ

    @Query("SELECT EXISTS(SELECT 1 FROM main_words_table WHERE id = :id)")
    fun hasWord(id: Int): Boolean

    @Query("SELECT id FROM main_words_table WHERE (original = :original AND translation = :translation)")
    suspend fun getWordID(original: String, translation: String): Int
}

@Dao
interface UnitDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUnit(entity: UnitEntity): Long

    @Delete
    suspend fun deleteUnit(entity: UnitEntity)

    @Update
    suspend fun updateUnit(entity: UnitEntity)

    @Query("SELECT * FROM units_table ORDER BY createdAt DESC")
    fun getAll(): Flow<List<UnitEntity>>
}

@Dao
interface RelationsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelation(entity: RelationsEntity): Long

    @Delete
    suspend fun deleteRelation(entity: RelationsEntity)

    @Query(
        """
        SELECT * FROM main_words_table mwt
        JOIN relations_table rt ON rt.wordID = mwt.id
        WHERE rt.unitID = :unitID
        ORDER BY rt.createdAt DESC
        """
    )
    fun getWordsInUnit(unitID: Int): Flow<List<WordEntity>>

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
    fun getUnitsWithNoWord(wordID: Int): Flow<List<UnitEntity>>
}