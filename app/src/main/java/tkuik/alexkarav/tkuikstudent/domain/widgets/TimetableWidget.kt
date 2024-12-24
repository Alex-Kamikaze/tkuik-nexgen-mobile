package tkuik.alexkarav.tkuikstudent.domain.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import tkuik.alexkarav.tkuikstudent.R
import tkuik.alexkarav.tkuikstudent.domain.workers.TimetableWidgetUpdateWorker
import tkuik.alexkarav.tkuikstudent.ui.activities.MainActivity

class TimetableWidget: GlanceAppWidget() {

    companion object {
        val currentLessonKey = stringPreferencesKey("current_pair")
        val currentCabinetKey = stringPreferencesKey("current_cabinet")
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @Composable
    fun Content() {
        val currentLesson = currentState(key = currentLessonKey) ?: "Не найдено"
        val currentCabinet = currentState(key = currentCabinetKey) ?: "Не найдено"
        GlanceTheme {
            Scaffold(
                titleBar = { TitleBar(startIcon = ImageProvider(R.drawable.time_icon), title = "Текущая пара", textColor = GlanceTheme.colors.onSurface) },
                modifier = GlanceModifier.fillMaxSize().clickable(actionStartActivity<MainActivity>()),
                backgroundColor = GlanceTheme.colors.widgetBackground
            ) {
                Column(modifier = GlanceModifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalAlignment = Alignment.CenterVertically) {
                    Text("Текущая пара: $currentLesson", style = TextStyle(color = GlanceTheme.colors.onSurface))
                    Text("Кабинет: $currentCabinet", style = TextStyle(color = GlanceTheme.colors.onSurface))

                    Button("Обновить", onClick = actionRunCallback<UpdateTimetableWidgetAction>())
                }
            }
        }
    }
}

class TimetableWidgetReceiver(override val glanceAppWidget: GlanceAppWidget = TimetableWidget()) : GlanceAppWidgetReceiver()

class UpdateTimetableWidgetAction: ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val updateWorkerRequest = OneTimeWorkRequestBuilder<TimetableWidgetUpdateWorker>().build()
        WorkManager.getInstance(context).enqueue(updateWorkerRequest)
        TimetableWidget().updateAll(context)
    }

}