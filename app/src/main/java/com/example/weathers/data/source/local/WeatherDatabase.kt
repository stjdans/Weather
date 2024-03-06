package com.example.weathers.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Location::class, RealTime::class, UltraForecast::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun weatherDao(): WeatherDao


    companion object {
        private var INSTANCE: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            return synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context = context.applicationContext,
                        klass = WeatherDatabase::class.java,
                        "weather.db"
                    )
                        .addCallback(initDataCallback(context))
//                        .fallbackToDestructiveMigration()
                        .build()
                }

                return INSTANCE!!
            }
        }

        private fun initDataCallback(context: Context): RoomDatabase.Callback {
            return object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    context.assets.open("location_20210401.csv").use {
                        it.bufferedReader().readLines().map(::toArray).forEach { args ->
                            db.execSQL(
                                sql = "INSERT INTO location (code, adress1, adress2, adress3, " +
                                        "dx, dy, longitude1, longitude2, longitude3, " +
                                        "latitude1, latitude2, latitude3, longitude4_100, latitude4_100, level) " +
                                        "VALUES (?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?,?)",
                                bindArgs = args
                            )
                        }
                    }

                }
            }
        }

        private fun toArray(cvs: String): Array<Any> {
            val split = cvs.split(",")
            val level = when {
                split[2].isEmpty() && split[3].isEmpty() -> 1
                split[3].isEmpty() -> 2
                else -> 3
            }
            return arrayOf(
                split[0], split[1], split[2], split[3], split[4].toInt(),
                split[5].toInt(), split[6].toInt(), split[7].toInt(), split[8].toFloat(), split[9].toInt(),
                split[10].toInt(), split[11].toFloat(), split[12].toDouble(), split[13].toDouble(),
                level
            )
        }
    }


}