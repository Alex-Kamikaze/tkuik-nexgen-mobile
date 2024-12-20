package tkuik.alexkarav.tkuikstudent.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tkuik.alexkarav.tkuikstudent.data.local.db.models.TimetableLocalModel

@Dao
interface TimetableDao {

    @Query("SELECT * FROM TimetableLocalModel")
    suspend fun getAllTimetable(): List<TimetableLocalModel>

    @Insert
    suspend fun insertTimetableEntity(entity: TimetableLocalModel)

    @Update
    suspend fun updateTimetableEntity(entity: TimetableLocalModel)

    @Delete
    suspend fun deleteTimetableEntity(entity: TimetableLocalModel)

    @Query("DELETE FROM TimetableLocalModel")
    suspend fun clearCache()

    @Query("SELECT * FROM TimetableLocalModel WHERE dayOfWeek = :dayOfWeek ORDER BY pairNumber ASC")
    suspend fun getTimetableForDay(dayOfWeek: Int): List<TimetableLocalModel>

}