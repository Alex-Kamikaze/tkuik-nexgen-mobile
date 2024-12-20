package tkuik.alexkarav.tkuikstudent.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tkuik.alexkarav.tkuikstudent.data.local.db.models.TimetableLocalModel
import tkuik.alexkarav.tkuikstudent.domain.repo.TimetableRepositoryImpl
import tkuik.alexkarav.tkuikstudent.utils.getCurrentDayOfWeek
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel @Inject constructor(private val repo: TimetableRepositoryImpl): ViewModel() {
    private val _timetableData = MutableStateFlow<List<TimetableLocalModel>>(listOf())
    val timetableData = _timetableData.asStateFlow()

    init {
        Log.d("TIMETABLE", "Entering timetable viewmodel")
        viewModelScope.launch {
            repo.updateTimetable()
            getTimetableForToday()
        }
    }

    fun getTimetableForToday() {
        val dayOfWeek = getCurrentDayOfWeek()
        viewModelScope.launch {
            val timetable = repo.getTimetableForDay(dayOfWeek)
            _timetableData.value = timetable
        }
    }

    fun updateTimetable() {
        viewModelScope.launch {
            repo.updateTimetable()
            getTimetableForToday()
        }
    }
}