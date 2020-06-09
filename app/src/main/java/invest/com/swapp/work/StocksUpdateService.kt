package invest.com.swapp.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class StocksUpdateService: Service() {

    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false
    val CHANNEL_ID = "ForegroundServiceChannel"

    //todo:- do data update the db and save...
    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this,"service started",Toast.LENGTH_LONG).show()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            when(action){
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
            }
        } else {
        }

        Log.d("_service","service should started...")

        return START_NOT_STICKY
    }



    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"service destroy",Toast.LENGTH_LONG).show()

    }

    private fun startService(){

        createNotificationChannel()

        if(isServiceStarted) return

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText("running in background updates")
//                .setSmallIcon(R.drawable.ic_stat_name)
//                .setContentIntent(pendingIntent)
                .build()


        startForeground(1, notification)

        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)

        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                    newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                        acquire()
                    }
                }

        //todo:if 1-5 do data update
        //todo:else get update from server any push notification for ads.!!!

        GlobalScope.launch(Dispatchers.IO) {
            while (isServiceStarted){
                launch(Dispatchers.IO){
                    doDataUpdate()
                }
                delay(1000)
            }
        }

    }

    private fun doDataUpdate(){
        Log.d("_service", "do data update...")
    }

    private fun stopService(){
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            Log.d("_service", e.message)
        }
        isServiceStarted = false
        setServiceState(this, ServiceState.STOPPED)
        Log.d("_service", "service stopped")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}

enum class Actions {
    START,
    STOP
}

enum class ServiceState {
    STARTED,
    STOPPED,
}

private const val name = "SERVICE_KEY"
private const val key = "SERVICE_STATE"

fun setServiceState(context: Context, state: ServiceState) {
    val sharedPrefs = getPreferences(context)
    sharedPrefs.edit().let {
        it.putString(key, state.name)
        it.apply()
    }
}

fun getServiceState(context: Context): ServiceState {
    val sharedPrefs = getPreferences(context)
    val value = sharedPrefs.getString(key, ServiceState.STOPPED.name)
    return ServiceState.valueOf(value)
}

private fun getPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(name, 0)
}
