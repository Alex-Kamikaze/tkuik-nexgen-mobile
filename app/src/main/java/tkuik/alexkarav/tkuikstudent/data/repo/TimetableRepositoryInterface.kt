package tkuik.alexkarav.tkuikstudent.data.repo

import kotlinx.coroutines.flow.Flow
import tkuik.alexkarav.tkuikstudent.data.local.db.models.TimetableLocalModel
import tkuik.alexkarav.tkuikstudent.data.remote.models.GroupRemoteModel

interface TimetableRepositoryInterface {
    suspend fun updateTimetable(): List<TimetableLocalModel>

    suspend fun saveAuthToken(token: String)

    suspend fun getGroupList(): Result<List<GroupRemoteModel>>

    suspend fun getAuthToken(): String

    suspend fun authorizeUser(login: String, password: String, group: Int): Result<Unit>

    suspend fun getUserGroup(): Int

    suspend fun getTimetableForDay(dayOfWeek: Int): List<TimetableLocalModel>

    suspend fun setProfileScreenInfo(login: String, groupName: String)

    suspend fun setCurrentPairAndCabinetForWidget()
}