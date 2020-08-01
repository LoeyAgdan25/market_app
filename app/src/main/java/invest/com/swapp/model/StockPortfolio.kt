package invest.com.swapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stock_portfolio_table")
class StockPortfolio (
    @PrimaryKey
    @SerializedName("symbol")
    @ColumnInfo(name = "symbol") val symbol:String,

    @SerializedName("total_amount")
    @ColumnInfo(name = "total_amount")var total_amount:Float,

    @SerializedName("total_shares")
    @ColumnInfo(name = "total_shares")var total_shares:Float,

    @SerializedName("trans_type")
    @ColumnInfo(name = "trans_type")var trans_type:Int,

    //todo:- how to get this average price?
    @SerializedName("average_price")
    @ColumnInfo(name = "average_price")var average_price:Float,

    //update with current price in database...
    val price:String = "",
    val percent_change:String = ""
    //todo:- get the current price...

    ){}



