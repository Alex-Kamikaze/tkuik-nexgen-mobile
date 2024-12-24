package tkuik.alexkarav.tkuikstudent.domain.repo

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tkuik.alexkarav.tkuikstudent.data.local.datastore.KeyStore
import tkuik.alexkarav.tkuikstudent.domain.widgets.TimetableWidget
import javax.inject.Inject

class GlanceWidgetRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    val keyStore: KeyStore,
    private val timetableRepositoryImpl: TimetableRepositoryImpl
    ) {


    fun updateWidgetState() {
        CoroutineScope(Dispatchers.IO).launch {
            val data = timetableRepositoryImpl.getCurrentPairAndCabinetForWidget()
            timetableRepositoryImpl.setCurrentPairAndCabinetForWidget(data)
            TimetableWidget().updateAll(context)
        }
    }
}