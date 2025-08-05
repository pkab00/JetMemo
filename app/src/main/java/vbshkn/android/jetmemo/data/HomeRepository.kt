package vbshkn.android.jetmemo.data

import kotlinx.coroutines.flow.Flow
import kotlin.concurrent.Volatile

/**
 * Repository - промежуточный слой абстракции между DB и ViewModel.
 *
 * Репозиторий запрашивает только необходимые ему для работы Dao.
 * Он инкапсулирует процессы работы с данными.
 * ViewModel для отображения данных использует ТОЛЬКО функционал Repository.
 */
class HomeRepository(private val unitDao: UnitDao) {
    fun getUnits(): Flow<List<UnitEntity>> {
        return unitDao.getAll()
    }

    suspend fun insertUnit(name: String) {
        unitDao.insertUnit(UnitEntity(name = name))
    }

    suspend fun deleteUnit(unit: UnitEntity) {
        unitDao.deleteUnit(unit)
    }

    suspend fun editUnit(unit: UnitEntity) {
        unitDao.updateUnit(unit)
    }

    companion object {
        @Volatile
        var INSTANCE: HomeRepository? = null

        fun getInstance(unitDao: UnitDao): HomeRepository {
            return INSTANCE ?: synchronized(this) {
                val repo = HomeRepository(unitDao)
                INSTANCE = repo
                repo
            }
        }
    }
}