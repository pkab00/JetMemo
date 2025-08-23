package vbshkn.android.jetmemo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * База данных Room, используемая приложением.
 * Содержит три таблицы для слов, юнитов и описания отношений между ними.
 * @see WordEntity
 * @see UnitEntity
 * @see RelationsEntity
 */
@Database(
    entities = [WordEntity::class, UnitEntity::class, RelationsEntity::class],
    version = 1
)
abstract class MainDB : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun unitDao(): UnitDao
    abstract fun relationsDao(): RelationsDao

    companion object {
        /**
         * Создание объекта для взаимодействия с базой данных.
         * @param context контекст приложения (запрашивается в App)
         * @return объект ДБ
         */
        fun createDB(context: Context): MainDB {
            return Room.databaseBuilder(
                context = context,
                klass = MainDB::class.java,
                name = "main.db"
            ).build()
        }
    }
}