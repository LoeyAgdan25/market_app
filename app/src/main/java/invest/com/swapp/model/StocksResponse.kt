package invest.com.swapp.model

import com.google.gson.annotations.SerializedName

class StocksResponse {

    @SerializedName("stocks")
    var stocks: List<Stock2>? = null
}