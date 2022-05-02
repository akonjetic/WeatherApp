package hr.tvz.weatherapp.network.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "RecentCities")
data class LocationResponse(


    @Ignore var consolidated_weather: ArrayList<CityData>,
    var time: String,
    var title: String,
    @PrimaryKey
    var woeid: Long,
    var latt_long: String,
    var timezone: String,
    var favorite: Boolean,
    var crrnPos: Int

){
    constructor() : this(arrayListOf(), "", "", 0, "", "", false, 0)
}



data class CityData(
    val id: Long,
    val weather_state_name: String,
    val weather_state_abbr: String,
    val wind_direction_compass: String,
    val created: String,
    val applicable_date: String,
    val min_temp: Double,
    val max_temp: Double,
    val the_temp: Double,
    val wind_speed: Double,
    val wind_direction: Double,
    val air_pressure: Double,
    val humidity: Double,
    val visibility: Double,
    val predictability: Int
)
