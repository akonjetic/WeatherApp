package hr.tvz.weatherapp.database

import androidx.room.*
import hr.tvz.weatherapp.network.model.LocationResponse

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: LocationResponse)

    @Delete
    suspend fun deleteCity(city: LocationResponse)

    @Query("SELECT * FROM RecentCities")
    suspend fun getAllRecentCities(): List<LocationResponse>

    @Query("SELECT * FROM RecentCities WHERE favorite = 1")
    suspend fun getAllFavorites(): List<LocationResponse>

    @Query("SELECT * FROM RecentCities WHERE favorite = 1 ORDER BY crrnPos ASC")
    suspend fun getAllFavoritesSorted(): List<LocationResponse>


}