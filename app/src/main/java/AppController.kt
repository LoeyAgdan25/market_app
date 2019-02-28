
import android.app.Application
import android.content.Context
import android.util.Log
import org.jetbrains.annotations.NotNull
import com.amazonaws.mobileconnectors.cognitoidentityprovider.*
import com.amazonaws.regions.Regions

class AppController: Application(){

    var up: CognitoUserPool? = null

    companion object {
        const val APP_NAME = "swapp"


        }

    init {

    }

    override fun onCreate() {
        super.onCreate()
        up = CognitoUserPool(baseContext, UtilityHelper.CognitoUserPool.USERPOOL_ID, UtilityHelper.CognitoUserPool.CLIENT_ID, UtilityHelper.CognitoUserPool.CLIENT_SECRET, Regions.US_EAST_2)

        Log.d("_AppDelegate call","app delegate has been called..")
    }

    fun getUserPool(): CognitoUserPool? {
        return up
    }

}