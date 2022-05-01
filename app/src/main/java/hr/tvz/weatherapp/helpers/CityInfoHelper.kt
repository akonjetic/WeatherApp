package hr.tvz.weatherapp.helpers

import android.content.Context
import android.content.res.Resources
import androidx.activity.viewModels
import hr.tvz.weatherapp.MainActivityViewModel
import hr.tvz.weatherapp.R
import hr.tvz.weatherapp.network.model.CityData

class CityInfoHelper(s: String, s1: String, i: String) {
    val dataName: String = ""
    val imageName: String = ""
    val dataValue: String = ""


    fun getCityInfoList (cityData : ArrayList<CityData>/*, context: Context*/) : ArrayList<CityInfoHelper>{
        val cityInfo : ArrayList<CityInfoHelper> = listOf(CityInfoHelper("", "", "")) as ArrayList<CityInfoHelper>

        val dataNames = Resources.getSystem().getStringArray(R.array.city_info_array)
        val imageNames = Resources.getSystem().getStringArray(R.array.info_images_names)

        for(i in 0..6){
            val cityInfo = CityInfoHelper(dataNames[i], imageNames[i], getCityData(cityData[0])[i])
        }

        return cityInfo
    }

    fun getCityData(cityData: CityData) : ArrayList<String>{

        val minMax : String = cityData.min_temp.toString()+ "°" + "/" + cityData.max_temp.toString() + "°"
        val wind : String = cityData.wind_speed.toString() + " km/h (" + cityData.wind_direction + ")"

        val values = arrayListOf(
            minMax,
            wind,
            cityData.humidity.toString() + "%",
            cityData.air_pressure.toString() + "hPa",
            cityData.visibility.toString() + " km",
            cityData.predictability.toString() + "%"
        )

        return values
    }







}
