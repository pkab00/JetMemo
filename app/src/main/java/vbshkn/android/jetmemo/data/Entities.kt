package vbshkn.android.jetmemo.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Объект слова, возвращаемый базой данных.
 * Ассоциируется с таблицей main_words_table.
 * @param id уникальный идентификатор, генерируется автоматически
 * @param original слово
 * @param translation перевод слова
 * @param createdAt время создания в миллисекундах
 */
@Entity(tableName = "main_words_table")
data class WordEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val original: String,
    val translation: String,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Объект юнита, возвращаемый базой данных.
 * Ассоциируется с таблицей units_table.
 * @param id уникальный идентификатор, генерируется автоматически
 * @param name название юнита
 * @param counter счётчик слов (в данный момент не используется)
 * @param createdAt время создания в миллисекундах
 */
@Entity(tableName = "units_table")
data class UnitEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var counter: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
) {
    override fun toString(): String {
        return name
    }
}

/**
 * Объект связи, возвращаемый базой данных.
 * Содержит пару значений "юнит-слово".
 * ID юнита и слова используются как foreign key. В случае удаления юнита или слова,
 * информация о связях с их участием также удаляется.
 * Ассоциируется с таблицей relations_table.
 * @param unitID идентификатор юнита
 * @param wordID идентификатор слова
 * @param createdAt время создания в миллисекундах
 */
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