package hr.tvz.weatherapp

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.tvz.weatherapp.database.CityDatabase
import hr.tvz.weatherapp.helpers.FormatAndDesignHelper
import hr.tvz.weatherapp.helpers.MappingHelper
import hr.tvz.weatherapp.network.Network
import hr.tvz.weatherapp.network.model.CityData
import hr.tvz.weatherapp.network.model.LocationAndCityData
import hr.tvz.weatherapp.network.model.LocationResponse
import hr.tvz.weatherapp.network.model.LocationSearchResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MainActivityViewModel : ViewModel() {

    val listOfLocations = MutableLiveData<ArrayList<LocationSearchResponse>>()
    val cityData = MutableLiveData<LocationResponse>()
    val cityDataSpecificDate = MutableLiveData<ArrayList<CityData>>()
    val favoriteCitiesFromDB = MutableLiveData<ArrayList<LocationResponse>>()
    val favoriteCitiesDBAllData = MutableLiveData<ArrayList<LocationAndCityData>>()
    val recentCitiesFromDB = MutableLiveData<ArrayList<LocationResponse>>()
    val recentCitiesDBAllData = MutableLiveData<ArrayList<LocationAndCityData>>()

    fun getLocationList(name: String) {
        viewModelScope.launch {
            safeCall(call = {
                listOfLocations.value = Network().getService().locationSearch(name)
            })
        }
    }

    fun getSpecificCityData(id: Int) {
        viewModelScope.launch {
            safeCall(call = {
                cityData.value = Network().getService().getLocation(id)
            })
        }
    }

    fun getCityDataSpecificDate(id: Int, date: String) {
        viewModelScope.launch {
            safeCall(call = {
                cityDataSpecificDate.value = Network().getService().getLocationDay(id, date)
            })
        }
    }

    protected suspend fun <T : Any> safeCall(call: suspend () -> T): T? {
        try {
            return call()
        } catch (e: Throwable) {
            println(e.message)
        }
        return null
    }

    fun insertRecentCity(context: Context, city: LocationResponse) {
        viewModelScope.launch {
            CityDatabase.getDatabase(context)?.getCityDao()?.insertCity(city)
        }
    }

    fun insertFavoriteCity(context: Context, city: LocationResponse) {
        city.favorite = true

        viewModelScope.launch {
            CityDatabase.getDatabase(context)?.getCityDao()?.insertCity(city)
        }
    }

    fun removeFavoriteCity(context: Context, city: LocationResponse) {
        city.favorite = false

        viewModelScope.launch {
            CityDatabase.getDatabase(context)?.getCityDao()?.insertCity(city)
        }
    }

    fun getFavoriteCitiesFromDB(context: Context) {
        viewModelScope.launch {
            favoriteCitiesFromDB.value = CityDatabase.getDatabase(context)?.getCityDao()?.getAllFavoritesSorted() as ArrayList<LocationResponse>
        }
    }

    fun getFavoriteCitiesDBData(context: Context) {
        viewModelScope.launch {
            val locationList = CityDatabase.getDatabase(context)?.getCityDao()?.getAllFavoritesSorted() as ArrayList
            val resultList = arrayListOf<LocationAndCityData>()
            val asyncTasks = locationList.map { cities ->
                async {
                    Network().getService().getLocationDay(cities.woeid.toInt(), dateInStringS)
                }
            }
            val responses = asyncTasks.awaitAll()

            for (i in responses.indices) {
                val newCityData = MappingHelper().mapToLocationAndCityData(responses[i], locationList[i])
                resultList.add(newCityData)
            }

            favoriteCitiesDBAllData.value = resultList
        }
    }

    fun getRecentCitiesFromDB(context: Context) {
        viewModelScope.launch {
            recentCitiesFromDB.value = CityDatabase.getDatabase(context)?.getCityDao()?.getAllRecentCities() as ArrayList<LocationResponse>
        }
    }

    private val date = FormatAndDesignHelper().getCurrentDateTime()
    private val dateInStringS =
        with(FormatAndDesignHelper()) {
            date.toString("yyyy/MM/dd")
        }

    fun getRecentCitiesFromDBData(context: Context) {
        viewModelScope.launch {
            val locationList = CityDatabase.getDatabase(context)?.getCityDao()?.getAllRecentCities() as ArrayList
            val resultList = arrayListOf<LocationAndCityData>()
            val asyncTasks = locationList.map { cities ->
                async {
                    Network().getService().getLocationDay(cities.woeid.toInt(), dateInStringS)
                }
            }
            val responses = asyncTasks.awaitAll()

            for (i in responses.indices) {
                val newCityData = MappingHelper().mapToLocationAndCityData(responses[i], locationList[i])
                resultList.add(newCityData)
            }

            recentCitiesDBAllData.value = resultList
        }
    }

    fun removeFavoritesFromDB(context: Context) {
        viewModelScope.launch {
            val favorites = CityDatabase.getDatabase(context)?.getCityDao()?.getAllFavoritesSorted()
            for (item in favorites!!) {
                removeFavoriteCity(context, item)
            }
        }
    }

    fun removeRecentsFromDB(context: Context) {
        viewModelScope.launch {
            CityDatabase.getDatabase(context)?.getCityDao()?.clearAllRecentCities()
        }
    }
}
