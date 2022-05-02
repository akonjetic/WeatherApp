package hr.tvz.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.tvz.weatherapp.network.model.LocationResponse
import hr.tvz.weatherapp.network.model.LocationSearchResponse

@Database(entities = [LocationResponse::class], version = 4, exportSchema = false)
abstract class CityDatabase: RoomDatabase() {
    abstract fun getCityDao(): CityDao

    companion object{
        private var instance: CityDatabase? = null

        fun getDatabase(context: Context): CityDatabase?{
            if(instance == null){
                instance = buildDatabase(context)
            }
            return instance
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                CityDatabase::class.java,
                "CityDatabase"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}