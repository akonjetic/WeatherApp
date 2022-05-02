package hr.tvz.weatherapp.network.model

import androidx.room.Entity
import androidx.room.PrimaryKey


data class LocationSearchResponse(
    val title: String,
    val location_type: String,
    val woeid: Int,
    val latt_long: String
)
