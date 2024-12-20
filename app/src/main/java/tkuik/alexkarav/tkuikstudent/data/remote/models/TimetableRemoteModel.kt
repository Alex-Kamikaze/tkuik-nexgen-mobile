package tkuik.alexkarav.tkuikstudent.data.remote.models

data class TimetableRemoteModel(
    val day_of_week: Int,
    val pair_number: Int,
    val distant_pair: Boolean,
    val subject_name: String,
    val teacher_name: String,
    val cabinet_number: String,
    val pair_begin_time: String,
    val pair_end_time: String,
    val denominator_options: String
)
