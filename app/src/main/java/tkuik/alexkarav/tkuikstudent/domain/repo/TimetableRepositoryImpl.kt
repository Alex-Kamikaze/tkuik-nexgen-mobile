package tkuik.alexkarav.tkuikstudent.domain.repo

import android.util.Log
import androidx.compose.runtime.key
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import tkuik.alexkarav.tkuikstudent.data.local.datastore.KeyStore
import tkuik.alexkarav.tkuikstudent.data.local.db.TimetableDao
import tkuik.alexkarav.tkuikstudent.data.local.db.TimetableDatabase
import tkuik.alexkarav.tkuikstudent.data.local.db.models.TimetableLocalModel
import tkuik.alexkarav.tkuikstudent.data.remote.TimetableApi
import tkuik.alexkarav.tkuikstudent.data.remote.models.AuthRequestModel
import tkuik.alexkarav.tkuikstudent.data.remote.models.GroupRemoteModel
import tkuik.alexkarav.tkuikstudent.data.repo.TimetableRepositoryInterface
import tkuik.alexkarav.tkuikstudent.utils.checkCurrentTimeInInterval
import tkuik.alexkarav.tkuikstudent.utils.getCurrentDayOfWeek
import java.time.LocalDateTime
import javax.inject.Inject

class TimetableRepositoryImpl @Inject constructor(private val db: TimetableDatabase, private val api: TimetableApi, private val keystore: KeyStore): TimetableRepositoryInterface {
    private val timetableDao: TimetableDao = db.getTimetableDao()

    override suspend fun updateTimetable(): List<TimetableLocalModel> {
        Log.d("API", "Вот тут скорее всего ебнется")
        val userGroup = keystore.getGroupId.first()
        try {
            val timetableForGroup = api.getTimetable(userGroup)
            timetableDao.clearCache()
            timetableForGroup.forEach { lesson ->
                timetableDao.insertTimetableEntity(
                    TimetableLocalModel(
                        dayOfWeek = lesson.day_of_week,
                        pairNumber = lesson.pair_number,
                        distantPair = lesson.distant_pair,
                        subjectName = lesson.subject_name,
                        teacherName = lesson.teacher_name,
                        cabinetNumber = lesson.cabinet_number,
                        pairBeginTime = lesson.pair_begin_time,
                        pairEndTime = lesson.pair_end_time,
                        denominatorOptions = lesson.denominator_options
                    )
                )
            }
        }
        catch(e: Exception) {
            Log.e("REFRESH_ERROR", e.message!!)
        }
        finally {
            return timetableDao.getTimetableForDay(getCurrentDayOfWeek())
        }
    }

    override suspend fun saveAuthToken(token: String) {
        keystore.setAuthToken(token)
    }

    override suspend fun getGroupList(): Result<List<GroupRemoteModel>> {
        return try {
            val data = api.getAllGroups()
            Result.success(data)
        }
        catch(e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAuthToken(): String {
        val tokenFlow = keystore.getAuthToken
        return tokenFlow.first()
    }

    override suspend fun authorizeUser(login: String, password: String, group: Int): Result<Unit> {
        return try {
            val token = api.authorizeUser(AuthRequestModel(login, password))
            keystore.setAuthToken(token.token)
            keystore.setUserGroup(group)
            Result.success(Unit)
        }
        catch(e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getUserGroup(): Int {
        val group = keystore.getGroupId
        return group.first()
    }

    override suspend fun getTimetableForDay(dayOfWeek: Int): List<TimetableLocalModel> {
        return timetableDao.getTimetableForDay(dayOfWeek)
    }

    suspend fun getTimetableLocally(): List<TimetableLocalModel> {
        val currentDayOfWeek = getCurrentDayOfWeek()
        return timetableDao.getTimetableForDay(currentDayOfWeek)
    }

    // TODO: доделать для работы с экраном профиля
    override suspend fun setProfileScreenInfo(login: String, groupName: String) {}
    override suspend fun setCurrentPairAndCabinetForWidget() {
        Log.d("API", "Обновление текущей пары и кабинета")
        val timetableForToday = getTimetableLocally()
        timetableForToday.forEach { lesson ->
            if(checkCurrentTimeInInterval(lesson.pairBeginTime, lesson.pairEndTime)) {
                Log.d("API", "Найдено совпадение, идет добавление в DataStore")
                keystore.setCurrentPairInfo(lesson.subjectName, lesson.cabinetNumber)
            }
        }
    }
}