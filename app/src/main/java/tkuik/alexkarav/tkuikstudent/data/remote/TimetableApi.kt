package tkuik.alexkarav.tkuikstudent.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import tkuik.alexkarav.tkuikstudent.data.remote.models.AuthRequestModel
import tkuik.alexkarav.tkuikstudent.data.remote.models.AuthResponseModel
import tkuik.alexkarav.tkuikstudent.data.remote.models.GroupRemoteModel
import tkuik.alexkarav.tkuikstudent.data.remote.models.TimetableRemoteModel


interface TimetableApi {

    @POST("/api/token")
    suspend fun authorizeUser(@Body credentials: AuthRequestModel): AuthResponseModel


    @GET("/api/timetable/timetable-for-group/{group_id}")
    suspend fun getTimetable(@Path("group_id") groupId: Int): List<TimetableRemoteModel>

    @GET("/api/timetable/all-groups")
    suspend fun getAllGroups(): List<GroupRemoteModel>

    companion object {
        const val BASE_URL = "https://c175-45-136-246-144.ngrok-free.app"
    }
}