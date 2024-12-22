package tkuik.alexkarav.tkuikstudent.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import tkuik.alexkarav.tkuikstudent.data.local.db.models.TimetableLocalModel
import tkuik.alexkarav.tkuikstudent.domain.TimetableViewModel
import tkuik.alexkarav.tkuikstudent.ui.components.TimetableElement
import tkuik.alexkarav.tkuikstudent.utils.UIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(modifier: Modifier = Modifier, timetableViewModel: TimetableViewModel) {
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    
    val coroutineScope = rememberCoroutineScope()

    val timetableItems by timetableViewModel.timetableData.collectAsState()

    when(timetableItems) {
        is UIState.Error -> {}
        UIState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(150.dp))
            }
        }
        UIState.Pending -> {}
        is UIState.Success<*> -> {
            val timetableItems = (timetableItems as UIState.Success<*>).data as List<TimetableLocalModel>
            PullToRefreshBox(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    coroutineScope.launch {
                        timetableViewModel.updateTimetable()
                        timetableViewModel.getTimetableForToday()
                        isRefreshing = false
                    }
                }
            ) {
                LazyColumn(modifier = modifier.fillMaxSize().padding(20.dp)) {
                    items(timetableItems.size) { lessonIndex ->
                        TimetableElement(
                            pairNum = timetableItems[lessonIndex].pairNumber,
                            pairName = timetableItems[lessonIndex].subjectName,
                            cabinet = timetableItems[lessonIndex].cabinetNumber,
                            teacherName = timetableItems[lessonIndex].teacherName
                        )
                    }
                }
            }
        }
    }


}