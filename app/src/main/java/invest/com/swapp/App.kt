package invest.com.swapp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.work.*
import invest.com.swapp.service.*
import java.util.concurrent.TimeUnit

//import androidx.security.crypto.EncryptedSharedPreferences
//import androidx.security.crypto.MasterKeys

class App : Application() {

    companion object{
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("_start","application started...")

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val work = PeriodicWorkRequestBuilder<WatchStockUpdates>(15, TimeUnit.MINUTES).setConstraints(constraints).build()
        val workManager = WorkManager.getInstance(this)
        //workManager.enqueue(work)


        //check if there is internet connectivity
        if(invest.com.swapp.helper.ConnectivityManager().isConnectingToInternet(this)){
            actionOnService(Actions.START)
        }
    }



    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return

        Intent(this, StocksUpdateService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //log("Starting the service in >=26 Mode")
                startForegroundService(it)
                
                return
            }
            //log("Starting the service in < 26 Mode")
            startService(it)
        }
    }
}