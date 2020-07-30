package invest.com.swapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stock_portfolio")
class StockPortfolio (

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @ColumnInfo(name = "id") val id: Int,

    @SerializedName("code")
    @ColumnInfo(name = "code") val code:String,

    @SerializedName("total_amount")
    @ColumnInfo(name = "total_amount")var total_amount:Float,

    @SerializedName("total_shares")
    @ColumnInfo(name = "total_shares")var total_shares:Float,

    @SerializedName("trans_type")
    @ColumnInfo(name = "trans_type")var trans_type:Int,

    //todo:- how to get this average price?
    @SerializedName("average_price")
    @ColumnInfo(name = "average_price")var average_price:Float

    //todo:- get the current price...

    ){}



