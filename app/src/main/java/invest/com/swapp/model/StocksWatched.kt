package invest.com.swapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stocks_watched_table")
class StocksWatched (
    @PrimaryKey
    @SerializedName("symbol")
    @ColumnInfo(name = "symbol") val symbol:String,

    @SerializedName("sell_price")
    @ColumnInfo(name = "sell_price")var sell_price:Float,

    @SerializedName("buy_price")
    @ColumnInfo(name = "buy_price")var buy_price:Float,


    val price:String = "",
    val percent_change:String = "",
    val name:String = ""
//,
//        foreignKeys = arrayOf(ForeignKey(entity = Stock2::class, parentColumns = arrayOf("symbol"), childColumns = arrayOf("symbol"), onDelete = ForeignKey.CASCADE))
//    @ForeignKey(entity = Stock2::class,parentColumns = arrayOf("symbol"), childColumns = arrayOf("symbol"), onDelete = NO_ACTION)
){}
