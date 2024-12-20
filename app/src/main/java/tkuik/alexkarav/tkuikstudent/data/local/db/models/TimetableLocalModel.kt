package tkuik.alexkarav.tkuikstudent.data.local.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimetableLocalModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayOfWeek: Int,
    val pairNumber: Int,
    val distantPair: Boolean,
    val subjectName: String,
    val teacherName: String,
    val cabinetNumber: String,
    val pairBeginTime: String,
    val pairEndTime: String,
    val denominatorOptions: String
)