package hr.tvz.weatherapp.helpers

import hr.tvz.weatherapp.network.model.CityData
import hr.tvz.weatherapp.network.model.LocationAndCityData
import hr.tvz.weatherapp.network.model.LocationResponse

class MappingHelper {
    fun mapToLocationResponseList(list: ArrayList<LocationAndCityData>): ArrayList<LocationResponse> {

        val locationResponse = arrayListOf<LocationResponse>()
        for (locationAndCityData in list) {
            val newData = LocationResponse(
                arrayListOf(),
                locationAndCityData.time,
                locationAndCityData.title,
                locationAndCityData.woeid,
                locationAndCityData.latt_long,
                locationAndCityData.timezone,
                locationAndCityData.favorite,
                locationAndCityData.crrnPos
            )
            locationResponse.add(newData)
        }

        return locationResponse
    }

    fun mapToLocationResponse(locationAndCityData: LocationAndCityData): LocationResponse {
        val newData = LocationResponse(
            arrayListOf(), locationAndCityData.time, locationAndCityData.title, locationAndCityData.woeid,
            locationAndCityData.latt_long, locationAndCityData.timezone, locationAndCityData.favorite, locationAndCityData.crrnPos
        )

        return newData
    }

    fun mapToLocationAndCityData(specificData: ArrayList<CityData>, locationResponse: LocationResponse): LocationAndCityData {
        val info = specificData[0]
        val newData = LocationAndCityData(
            locationResponse.time, locationResponse.title, locationResponse.woeid, locationResponse.latt_long, locationResponse.timezone,
            locationResponse.favorite, locationResponse.crrnPos, info.id, info.weather_state_name, info.weather_state_abbr, info.wind_direction_compass, info.created,
            info.applicable_date, info.min_temp, info.max_temp, info.the_temp, info.wind_speed, info.wind_direction,
            info.air_pressure, info.humidity, info.visibility, info.predictability
        )

        return newData
    }
}
