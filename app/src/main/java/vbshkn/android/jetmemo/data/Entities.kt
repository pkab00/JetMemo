package vbshkn.android.jetmemo.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "main_words_table")
data class WordEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val original: String,
    val translation: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "units_table")
data class UnitEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    var counter: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "relations_table",
    primaryKeys = ["wordID", "unitID"],
    indices = [Index("wordID"), Index("unitID")],
    foreignKeys = [
        ForeignKey(
            entity = WordEntity::class,
            parentColumns = ["id"],
            childColumns = ["wordID"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
         ForeignKey(
             entity = UnitEntity::class,
             parentColumns = ["id"],
             childColumns = ["unitID"],
             onUpdate = ForeignKey.CASCADE,
             onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RelationsEntity(
    val wordID: Int,
    val unitID: Int,
    val createdAt: Long = System.currentTimeMillis()
)