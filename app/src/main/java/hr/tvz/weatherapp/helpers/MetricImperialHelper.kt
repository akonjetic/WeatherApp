package hr.tvz.weatherapp.helpers

import hr.tvz.weatherapp.MainActivity
import hr.tvz.weatherapp.network.model.CityData

class MetricImperialHelper {

    fun getCityDataMetric(cityData: CityData): ArrayList<String> {

        val minMax: String =
            cityData.min_temp.toInt().toString() + "°" + "/" + cityData.max_temp.toInt()
                .toString() + "°"
        val wind: String =
            cityData.wind_speed.toInt().toString() + " km/h (" + cityData.wind_direction_compass + ")"

        return arrayListOf(
            minMax,
            wind,
            cityData.humidity.toInt().toString() + "%",
            cityData.air_pressure.toInt().toString() + " hPa",
            cityData.visibility.toInt().toString() + " km",
            cityData.predictability.toString() + "%"
        )
    }

    fun getCityDataImperial(cityData: CityData): ArrayList<String> {

        val min_temp = (cityData.min_temp * 1.8) + 32
        val max_temp = (cityData.max_temp * 1.8) + 32
        val wind_speed = cityData.wind_speed / 1.609344
        val air_pressure = cityData.air_pressure * 0.0145038
        val visibility = cityData.visibility * 0.621371

        val minMax: String =
            min_temp.toInt().toString() + "°F" + "/" + max_temp.toInt()
                .toString() + "°F"
        val wind: String =
            wind_speed.toInt().toString() + " mph (" + cityData.wind_direction_compass + ")"

        return arrayListOf(
            minMax,
            wind,
            cityData.humidity.toInt().toString() + "%",
            air_pressure.toInt().toString() + " psi",
            visibility.toInt().toString() + " mi",
            cityData.predictability.toString() + "%"
        )
    }

    fun getTempConverted(temperature: Double): String {
        val resultString: String = if (MainActivity.metrics) {
            temperature.toInt().toString() + "°C"
        } else {
            ((temperature * 1.8) + 32).toInt().toString() + "°F"
        }
        return resultString
    }
}
