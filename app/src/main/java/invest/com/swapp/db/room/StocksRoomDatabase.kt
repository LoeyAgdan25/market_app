package invest.com.swapp.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import invest.com.swapp.model.Stock2
import invest.com.swapp.model.StockTrade
import invest.com.swapp.model.StocksWatched
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Stock2::class, StocksWatched::class, StockTrade::class), version = 11, exportSchema = false)
abstract class StocksRoomDatabase : RoomDatabase(){

    abstract fun stockDao(): StocksDao
    abstract fun watchDao(): WatchedDao
    abstract fun tradeDao(): TradeDao

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback(){
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let {
                database ->
                scope.launch {
                    var stockDao = database.stockDao()
                    var watchedDao = database.watchDao()
                    var tradeDao = database.tradeDao()
                    //loop things to insert here
                    //initialise database
                    //todo:- get data from database...
                    //stockDao.insert(Stock2("XYz Company","XYZ", "descirption","1","100","2"))
                }
            }
        }
    }

    //singleton
    companion object{
        @Volatile
        private var INSTANCE: StocksRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): StocksRoomDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, StocksRoomDatabase::class.java,"babylon_db")
                        .fallbackToDestructiveMigration().addCallback(DatabaseCallback(scope)).build()

                INSTANCE = instance
                instance
            }

        }
    }

}