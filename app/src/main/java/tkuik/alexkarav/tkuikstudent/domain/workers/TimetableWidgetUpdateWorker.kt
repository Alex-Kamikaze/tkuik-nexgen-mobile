package tkuik.alexkarav.tkuikstudent.domain.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import tkuik.alexkarav.tkuikstudent.domain.repo.TimetableRepositoryImpl
import javax.inject.Inject

@HiltWorker
class TimetableWidgetUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repo: TimetableRepositoryImpl
): CoroutineWorker(appContext, workerParameters)  {
    override suspend fun doWork(): Result {
        try {
            repo.setCurrentPairAndCabinetForWidget()
            return Result.success()
        }
        catch(e: Exception) {
            Log.e("WORKER_ERROR", e.toString())
            return Result.failure()
        }
    }
}