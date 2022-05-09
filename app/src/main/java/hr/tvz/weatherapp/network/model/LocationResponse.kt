package hr.tvz.weatherapp.network.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
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

) {
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

data class LocationAndCityData(
    var time: String,
    var title: String,
    var woeid: Long,
    var latt_long: String,
    var timezone: String,
    var favorite: Boolean,
    var crrnPos: Int,
    var id: Long,
    var weather_state_name: String,
    var weather_state_abbr: String,
    var wind_direction_compass: String,
    var created: String,
    var applicable_date: String,
    var min_temp: Double,
    var max_temp: Double,
    var the_temp: Double,
    var wind_speed: Double,
    var wind_direction: Double,
    var air_pressure: Double,
    var humidity: Double,
    var visibility: Double,
    var predictability: Int
) {
    constructor() : this("", "", 0, "", "", false, 0, 0, "", "", "", "", "", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0)
}
