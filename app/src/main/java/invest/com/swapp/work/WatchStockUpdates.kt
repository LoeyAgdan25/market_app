package invest.com.swapp.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.*
import java.util.*

class WatchStockUpdates(appContext: Context, workerParameters: WorkerParameters):CoroutineWorker(appContext, workerParameters) {

    companion object{
        const val Progress = "Progress"
    }

    override suspend fun doWork(): Result = coroutineScope{
        //call repository to udpate database...
        Log.d("worker","update working... ${Calendar.getInstance().time}")
        Result.success()
    }

}