package hr.tvz.weatherapp.helpers

import hr.tvz.weatherapp.enums.Languages
import hr.tvz.weatherapp.network.model.LocationResponse
import java.util.ArrayList

class NamesFromEnumsHelper {
    fun getNames(list: List<Languages>): ArrayList<String> {
        val names = ArrayList<String>()
        for (item in list) {
            names.add(item.languages)
        }

        return names
    }

    fun getCityNames(list: ArrayList<LocationResponse>): ArrayList<String> {
        val result: ArrayList<String> = arrayListOf()

        for (item in list) {
            result.add(item.title)
        }

        return result
    }
}
