package tkuik.alexkarav.tkuikstudent.utils

import java.util.Calendar

fun getCurrentDayOfWeek(): Int {
    val calendar = Calendar.getInstance()
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    return if (dayOfWeek == Calendar.SUNDAY) {
        6
    } else {
        dayOfWeek - Calendar.MONDAY
    }
}