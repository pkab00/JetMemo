package vbshkn.android.jetmemo.data

import kotlinx.coroutines.flow.Flow
import kotlin.concurrent.Volatile

/**
 * Промежуточный слой абстракции между HomeScreenModel и базой данных Room.
 * @param unitDao - объект для работы с базой юнитов
 */
class HomeRepository(private val unitDao: UnitDao) {
    fun getUnits(): Flow<List<UnitEntity>> {
        return unitDao.getAll()
    }

    /**
     * Добавление нового юнита в базу данных.
     * @param name название юнита
     */
    suspend fun insertUnit(name: String) {
        unitDao.insertUnit(UnitEntity(name = name))
    }

    /**
     * Удаление юнита из базы данных.
     * @param unit объект юнита
     */
    suspend fun deleteUnit(unit: UnitEntity) {
        unitDao.deleteUnit(unit)
    }
    /**
     * Обновление информации о юните (обычно - названия) в базе данных.
     * @param unit объект юнита
     */
    suspend fun editUnit(unit: UnitEntity) {
        unitDao.updateUnit(unit)
    }

    companion object {
        @Volatile
        var INSTANCE: HomeRepository? = null

        /**
         * Создание и/или запрос singleton-объекта репозитория.
         */
        fun getInstance(unitDao: UnitDao): HomeRepository {
            return INSTANCE ?: synchronized(this) {
                val repo = HomeRepository(unitDao)
                INSTANCE = repo
                repo
            }
        }
    }
}