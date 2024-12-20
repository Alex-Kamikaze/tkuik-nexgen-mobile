package tkuik.alexkarav.tkuikstudent.data.remote.models

import com.google.gson.annotations.SerializedName

data class GroupRemoteModel(
    val id: Int,
    @SerializedName("group_name")
    val groupName: String,
    @SerializedName("group_course")
    val groupCourse: Int
)
