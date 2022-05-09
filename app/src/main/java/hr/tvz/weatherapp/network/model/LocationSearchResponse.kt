package hr.tvz.weatherapp.network.model

data class LocationSearchResponse(
    val title: String,
    val location_type: String,
    val woeid: Int,
    val latt_long: String
)
