package invest.com.swapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stock_trades")
class StockTrade (
        @PrimaryKey
        @SerializedName("code")
        @ColumnInfo(name = "code") val code:String,

        @SerializedName("type")
        @ColumnInfo(name = "type")var type:Int,

        @SerializedName("current_price")
        @ColumnInfo(name = "current_price")var current_price:Float,

        @SerializedName("buy_price")
        @ColumnInfo(name = "buy_price")var buy_price:Float,

        @SerializedName("total_amount")
        @ColumnInfo(name = "total_amount")var total_amount:Float,

        @SerializedName("net_amount")
        @ColumnInfo(name = "net_amount")var net_amount:Float,


        @SerializedName("shares")
        @ColumnInfo(name = "shares")var shares:Float,

        @SerializedName("commission")
        @ColumnInfo(name = "commission")var commission:Float,

        @SerializedName("tax")
        @ColumnInfo(name = "tax")var tax:Float,

        @SerializedName("others")
        @ColumnInfo(name = "others")var others:Float,

        val price:String = "",
        val percent_change:String = "",
        val name:String = ""
//,
//        foreignKeys = arrayOf(ForeignKey(entity = Stock2::class, parentColumns = arrayOf("symbol"), childColumns = arrayOf("symbol"), onDelete = ForeignKey.CASCADE))
//    @ForeignKey(entity = Stock2::class,parentColumns = arrayOf("symbol"), childColumns = arrayOf("symbol"), onDelete = NO_ACTION)
){}