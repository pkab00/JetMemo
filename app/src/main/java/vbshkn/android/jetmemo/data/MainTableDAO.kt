package vbshkn.android.jetmemo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import vbshkn.android.jetmemo.logic.Word

@Dao
interface MainTableDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWord(entity: WordEntity)

    @Delete
    suspend fun deleteWord(entity: WordEntity)

    @Update
    suspend fun updateWord(entity: WordEntity)

    @Query("SELECT * FROM MAIN_WORDS_TABLE") // Flow является корутиной, которая может использоваться как state
    fun selectAll(): Flow<List<Word>> // и самостоятельно обновляется при любых изменениях в ДБ
}