package invest.com.swapp.work

import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.*
import java.lang.Runnable

class WatchStockUpdates(appContext: Context, workerParameters: WorkerParameters):CoroutineWorker(appContext, workerParameters) {


    companion object{
        const val Progress = "Progress"
    }

    override suspend fun doWork(): Result = coroutineScope{

        //get data from database and watch changes
        //send notification if match with the request
        //get join query check if the value is equals
        //syncronise only

        val firstUpdate = workDataOf(Progress to 0)
        //send to notification

        Log.d("_work","hello work manager started")

        //run timer task here...
        val handler = Handler()
        val runnable = Runnable {
            Log.d("_work","work is working...")
        }

        val timer: Job = scroll(2000,5000){
            handler.post(runnable)
        }
        timer.start()

        Result.success()
    }

    private inline fun scroll(delayMillis: Long = 0, repeatMillis: Long = 0, crossinline action: () -> Unit) = GlobalScope.launch {
        delay(delayMillis)
        if (repeatMillis > 0) {
            while (true) {
                action()
                delay(repeatMillis)
            }
        } else {
            action()
        }
    }
}