package invest.com.swapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import invest.com.swapp.MasterActivity
import invest.com.swapp.R
import invest.com.swapp.model.StocksWatched
import invest.com.swapp.viewmodel.StocksViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class StocksUpdateService: LifecycleService(){

    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false
    val CHANNEL_ID = "ForegroundServiceChannel"
    var stockViewModel: StocksViewModel? = null


    //todo:- do data update the db and save...
    override fun onCreate() {
        super.onCreate()

    }

    private var mainIntent: Intent? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        mainIntent = intent
        if (intent != null) {
            val action = intent.action
            when(action){
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
            }
        } else {}

        Log.d("_service","service should started...")
        return START_NOT_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        //Toast.makeText(this,"service destroy",Toast.LENGTH_LONG).show()
    }

    private fun startService(){

        createNotificationChannel()

        if(isServiceStarted) return

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("PesoMetrics")
                .setContentText("Updating the market price...")
                .setSmallIcon(R.drawable.ic_trend_up)
//                .setContentIntent(pendingIntent) //todo:do the intent pending
                .build()
        startForeground(1, notification)

        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)

        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                    newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                        acquire()
                    }
                }

        //todo:else get update from server any push notification for ads.!!!
        //todo:- add when device is restarted
        val timeZone: TimeZone = TimeZone.getTimeZone("GMT+8:00")
        val c: Calendar = Calendar.getInstance(timeZone)
        val day = c.get(Calendar.DAY_OF_WEEK)
        val hour: Int = c.get(Calendar.HOUR_OF_DAY)

        Log.d("_timezoneValue", "day:$day hour: $hour")
        //todo:- check if database is empty to update once
        stockViewModel = StocksViewModel(application)
        //todo: set launch date inside view model
        if(day in 2..6) {
            if (hour in 8..17) {
                GlobalScope.launch(Dispatchers.IO) {
                    while (isServiceStarted) {
                        launch(Dispatchers.IO) {
                            stockViewModel!!.getStocks()
                        }
                        delay(60000)
                    }
                }
            }else{
                stopService()
            }
        }else{
            //check if database is empty
            //before stopping the service
            stopService()
        }

            stockViewModel!!.watchedStocks.observe(this, androidx.lifecycle.Observer {
                            for(watch: StocksWatched in it){
                                if(watch.buy_price == watch.price.toFloat()){
                                    addToNotifyList("${watch.symbol}:${watch.price}:buy")
                                }

                                if(watch.sell_price == watch.price.toFloat()){
                                    addToNotifyList("${watch.symbol}:${watch.price}:sell")
                                }
                            }
            })

    }

    var notifyList = ArrayList<String>()

    fun addToNotifyList(notifyString: String){
        //check if this is already in notification
        //ad if not
        if(!notifyList.contains(notifyString)){
            notifyList.add(notifyString)
            NotificationManagerCompat.from(this).apply {
                notify(notifyList.count()+1, createNotification("$notifyString"))
            }
        }
    }

    private fun createNotification(text:String): Notification {
        val notificationChannelId = "PRICE_CHANNEL"
        val arr = text.split(":").toTypedArray()

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                    notificationChannelId,
                    "Endless Service notifications channel",
                    NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = text
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent(this, MasterActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
                this,
                notificationChannelId
        ) else Notification.Builder(this)


        return builder
                .setContentTitle(arr[2].toUpperCase() + " ALERT for " + arr[0] )
                .setContentText("Stock at price PHP " + arr[1])
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_trend_up)
                .setTicker("")
                .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
                .build()
    }

    private fun groupNotification(c: Int){
        //val notification1 = createNotification("Symbol 1")
        //val notification2 = createNotification("Symbol 2")
        //val notification3 = createNotification("Symbol 3")

        if(c != 0) {

            //todo do group later...
            NotificationManagerCompat.from(this).apply {
                for (x in 1..c) {
                    notify(x, createNotification("symbol $x"))
                }
            }
        }
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
