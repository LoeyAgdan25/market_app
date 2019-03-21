package invest.com.swapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DBHelper(contxt:Context): ManagedSQLiteOpenHelper(contxt,"SwappDB", null,1){

    //Singleton
    companion object {
        private var instance: DBHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DBHelper{
            if(instance == null){
                instance = DBHelper(ctx.applicationContext)
            }

            return instance!!
        }
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0!!.createTable("tblWatched",true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "symbol" to TEXT,
                "name" to TEXT,
                "currency" to TEXT,
                "amount" to TEXT,
                "volume" to TEXT,
                "status" to TEXT
                )

        p0!!.createTable("tblInvestment",true,"id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "price" to REAL,
                "stocks" to INTEGER,
                "amount" to REAL,
                "bcharge" to REAL,
                "tax" to REAL,
                "total" to REAL,
                "date" to TEXT
                )
    }


    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.dropTable("tblWatched",true)
    }
}

val Context.database: DBHelper get() = DBHelper.getInstance(applicationContext)