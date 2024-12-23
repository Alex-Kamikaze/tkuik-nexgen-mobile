package tkuik.alexkarav.tkuikstudent

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.impl.WorkManagerImpl
import dagger.hilt.android.HiltAndroidApp
import tkuik.alexkarav.tkuikstudent.domain.repo.TimetableRepositoryImpl
import tkuik.alexkarav.tkuikstudent.domain.workers.TimetableUpdateWorker
import tkuik.alexkarav.tkuikstudent.domain.workers.TimetableWidgetUpdateWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class TimetableApplication: Application(), Configuration.Provider {

    @Inject lateinit var hiltWorkerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        val timetableUpdateWorkerRequest = PeriodicWorkRequestBuilder<TimetableUpdateWorker>(
            12, TimeUnit.HOURS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .setRequiresStorageNotLow(true)
                    .build()
            ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "UpdateTimetable",
                ExistingPeriodicWorkPolicy.KEEP,
                timetableUpdateWorkerRequest
        )

        val timetableWidgetUpdateWorkerRequest = OneTimeWorkRequestBuilder<TimetableWidgetUpdateWorker>().build()

        WorkManager.getInstance(this).enqueue(
            timetableWidgetUpdateWorkerRequest
        )
    }


}