package invest.com.swapp.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.*

class WatchStockUpdates(appContext: Context, workerParameters: WorkerParameters):CoroutineWorker(appContext, workerParameters) {

    companion object{
        const val Progress = "Progress"
    }

    override suspend fun doWork(): Result = coroutineScope{
        //call repository to udpate database...
        Result.success()
    }
}