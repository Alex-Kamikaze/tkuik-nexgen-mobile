package tkuik.alexkarav.tkuikstudent.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import tkuik.alexkarav.tkuikstudent.domain.TimetableViewModel
import tkuik.alexkarav.tkuikstudent.ui.components.TimetableElement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(modifier: Modifier = Modifier, timetableViewModel: TimetableViewModel) {
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    
    val coroutineScope = rememberCoroutineScope()

    val timetableItems by timetableViewModel.timetableData.collectAsState()

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
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(timetableItems.size) { lessonIndex ->
                TimetableElement(
                    pairNum = timetableItems[lessonIndex].pairNumber,
                    pairName = timetableItems[lessonIndex].subjectName,
                    cabinet = timetableItems[lessonIndex].cabinetNumber
                )
            }
        }
    }
}