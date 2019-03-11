package invest.com.swapp

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.regions.Regions

class AppHelper{

    var context: Context? = null
    companion object {

        //lateinit var appHelper:AppHelper
        var appHelper:AppHelper? = null
        var userPool:CognitoUserPool? = null
        var user:String? = null


        val userPoolId = UtilityHelper.CognitoUserPool.USERPOOL_ID
        val clientId = UtilityHelper.CognitoUserPool.CLIENT_ID
        val clientSecret = UtilityHelper.CognitoUserPool.CLIENT_SECRET
        val cognitoRegion = Regions.US_EAST_2


        var currSession:CognitoUserSession? = null
        var userDetails:CognitoUserDetails? = null
        var newDevice:CognitoDevice? = null

        fun init(context: Context){
            if (appHelper != null && userPool != null){
                return
            }

            if(userPool == null){
                userPool = CognitoUserPool(context, AppHelper.userPoolId,AppHelper.clientId,AppHelper.clientSecret, cognitoRegion)
            }
        }

    }

    constructor(context:Context){

        if (appHelper != null && userPool != null){
            return
        }

        if(userPool == null){
            userPool = CognitoUserPool(context, AppHelper.userPoolId,AppHelper.clientId,AppHelper.clientSecret, cognitoRegion)
        }
    }

    init {
        appHelper = this
    }


}