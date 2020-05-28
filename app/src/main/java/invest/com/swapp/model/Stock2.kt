package invest.com.swapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stocks_table")
class Stock2 (@PrimaryKey
              @SerializedName("name")
              @ColumnInfo(name = "name") val name:String,

              @SerializedName("symbol")
              @ColumnInfo(name = "symbol") val symbol:String,

              @SerializedName("description")
              @ColumnInfo(name = "description") val description:String,

              @SerializedName("percent_change")
              @ColumnInfo(name = "percent_change") val percent_change: String,

              @SerializedName("volume")
              @ColumnInfo(name = "volume") val volume:String,

              @SerializedName("companyId")
              @ColumnInfo(name="companyId") val companyId: Int,

              @SerializedName("securityID")
              @ColumnInfo(name="securityID") val securityID: Int,

              @SerializedName("price")
              @ColumnInfo(name = "price") val price:String){
}