package vbshkn.android.jetmemo.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlin.concurrent.Volatile

/**
 * Repository - промежуточный слой абстракции между DB и ViewModel.
 *
 * Репозиторий запрашивает только необходимые ему для работы Dao.
 * Он инкапсулирует процессы работы с данными.
 * ViewModel для отображения данных использует ТОЛЬКО функционал Repository.
 */
class UnitRepository(private val unitDao: UnitDao) {
    fun getUnits(): Flow<List<UnitEntity>>{
        return unitDao.getAll()
    }

    suspend fun insertUnit(name: String){
        unitDao.insertUnit(UnitEntity(name = name))
    }

    suspend fun deleteUnit(unit: UnitEntity){
        unitDao.deleteUnit(unit)
    }

    companion object{
        @Volatile
        var INSTANCE: UnitRepository? = null

        fun getInstance(unitDao: UnitDao): UnitRepository{
            return INSTANCE ?: synchronized(this) {
                val repo = UnitRepository(unitDao)
                INSTANCE = repo
                repo
            }
        }
    }
}