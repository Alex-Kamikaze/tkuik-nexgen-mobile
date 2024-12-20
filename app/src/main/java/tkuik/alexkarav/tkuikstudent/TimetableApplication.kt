package tkuik.alexkarav.tkuikstudent

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.impl.WorkManagerImpl
import dagger.hilt.android.HiltAndroidApp
import tkuik.alexkarav.tkuikstudent.domain.repo.TimetableRepositoryImpl
import tkuik.alexkarav.tkuikstudent.domain.workers.TimetableUpdateWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class TimetableApplication: Application() {

    @Inject
    lateinit var repo: TimetableRepositoryImpl

    override fun onCreate() {
        super.onCreate()
        val timetableUpdateWorkerRequest = PeriodicWorkRequestBuilder<TimetableUpdateWorker>(
            12, TimeUnit.HOURS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresDeviceIdle(true)
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
    }
}