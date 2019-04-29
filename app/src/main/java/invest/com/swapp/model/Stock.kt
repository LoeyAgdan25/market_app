package invest.com.swapp.model

import android.os.Parcelable
import java.util.*

data class Stock(val name: String, val symbol:String, val description: String, val percent:String, val volume:String, val price:String)