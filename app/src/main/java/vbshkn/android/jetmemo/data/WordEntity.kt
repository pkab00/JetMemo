package vbshkn.android.jetmemo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MAIN_WORDS_TABLE")
data class WordEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val original: String,
    val translation: String,
    var learned: Boolean = false
)