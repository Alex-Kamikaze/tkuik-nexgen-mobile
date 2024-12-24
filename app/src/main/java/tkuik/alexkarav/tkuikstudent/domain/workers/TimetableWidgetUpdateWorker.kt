package tkuik.alexkarav.tkuikstudent.domain.workers

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import tkuik.alexkarav.tkuikstudent.domain.repo.TimetableRepositoryImpl
import tkuik.alexkarav.tkuikstudent.domain.widgets.TimetableWidget
import tkuik.alexkarav.tkuikstudent.utils.Timetable
import javax.inject.Inject

@HiltWorker
class TimetableWidgetUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repo: TimetableRepositoryImpl
): CoroutineWorker(appContext, workerParameters)  {
    override suspend fun doWork(): Result {
        try {
            val widgetInfo = repo.getCurrentPairAndCabinetForWidget()
            GlanceAppWidgetManager(applicationContext).getGlanceIds(TimetableWidget::class.java)
                .forEach { glanceId ->
                    updateAppWidgetState(applicationContext, glanceId) { prefs ->
                        prefs[TimetableWidget.currentLessonKey] = widgetInfo?.currentPair ?: "Не найдено"
                        prefs[TimetableWidget.currentCabinetKey] = widgetInfo?.currentCabinet ?: "Не найден"
                    }
                }
            return Result.success()
        }
        catch(e: Exception) {
            Log.e("WORKER_ERROR", e.toString())
            return Result.failure()
        }
    }
}