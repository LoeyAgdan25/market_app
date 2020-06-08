package invest.com.swapp.security

import android.content.Context
import invest.com.swapp.App.Companion.context

//import androidx.security.crypto.EncryptedSharedPreferences
//import androidx.security.crypto.MasterKeys


class SSharedPreferenceManager(context: Context) {

//security for sdk 23 and <
//    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
//    val preference = EncryptedSharedPreferences.create("PesoJournal",masterKeyAlias,context,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

    //companion object{
        val sharedPreferences = context.getSharedPreferences("PesoJournal", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
    //}

}