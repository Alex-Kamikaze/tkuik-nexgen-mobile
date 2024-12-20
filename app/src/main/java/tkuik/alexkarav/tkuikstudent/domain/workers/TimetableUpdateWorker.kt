package tkuik.alexkarav.tkuikstudent.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import tkuik.alexkarav.tkuikstudent.domain.repo.TimetableRepositoryImpl
import javax.inject.Inject

@HiltWorker
class TimetableUpdateWorker @Inject constructor(appContext: Context, workerParameters: WorkerParameters, private val repo: TimetableRepositoryImpl): CoroutineWorker(appContext, workerParameters)  {
    override suspend fun doWork(): Result {
        try {
            repo.updateTimetable()
            return Result.success()
        }
        catch(e: Exception) {
            return Result.failure()
        }
    }
}