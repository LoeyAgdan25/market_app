package invest.com.swapp.listener

import androidx.lifecycle.LiveData
import invest.com.swapp.model.StocksWatched

interface WatchListener {
    fun onWatchedAction(stockWatched: StocksWatched)
    fun onSelectWatched(symbol:String)
}
