package tkuik.alexkarav.tkuikstudent.utils

import android.util.Log
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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

fun checkCurrentTimeInInterval(startTime: String, endTime: String): Boolean {
    val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss")
    val startTimeFormatted = LocalTime.parse(startTime, timeFormat)
    val endTimeFormatted = LocalTime.parse(endTime, timeFormat)
    val currentTime = LocalTime.now()
    val result = currentTime.isAfter(startTimeFormatted).and(currentTime.isBefore(endTimeFormatted))
    Log.d("API", "${currentTime.isAfter(startTimeFormatted)} ${currentTime.isAfter(endTimeFormatted)}")
    Log.d("API","Идет сравнение $startTime и $endTime с ${currentTime.format(timeFormat)}. Результат - $result")
    return result
}