package hr.tvz.weatherapp

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.tvz.weatherapp.database.CityDatabase
import hr.tvz.weatherapp.helpers.CityInfoHelper
import hr.tvz.weatherapp.network.Network
import hr.tvz.weatherapp.network.model.CityData
import hr.tvz.weatherapp.network.model.LocationResponse
import hr.tvz.weatherapp.network.model.LocationSearchResponse
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class MainActivityViewModel : ViewModel() {

    val listOfLocations = MutableLiveData<ArrayList<LocationSearchResponse>>()


    fun getLocationList(name: String){
        viewModelScope.launch {
            safeCall(call = {
                listOfLocations.value = Network().getService().locationSearch(name)
            })
        }
    }

    val cityData = MutableLiveData<LocationResponse>()

    fun getSpecificCityData(id: Int){
        viewModelScope.launch {
            safeCall(call = {
                cityData.value = Network().getService().getLocation(id)
            })
        }
    }


    val cityDataSpecificDate = MutableLiveData<ArrayList<CityData>>()

    fun getCityDataSpecificDate(id: Int, date: String){
        viewModelScope.launch {
            safeCall(call = {
                cityDataSpecificDate.value = Network().getService().getLocationDay(id, date)
           })
        }
    }

  /*  val cityInfoData : ArrayList<CityInfoHelper> =
        with(CityInfoHelper("", "", "")) {getCityInfoList(cityData.value!!.consolidated_weather)}
*/


    protected suspend fun <T : Any> safeCall(call: suspend () -> T): T?{
        try {
            return call()
        } catch (e: Throwable) {
            println(e.message)
        }
        return null
    }

    fun insertRecentCity(context: Context, city: LocationResponse){
        viewModelScope.launch {
            CityDatabase.getDatabase(context)?.getCityDao()?.insertCity(city)
        }
    }

    fun insertFavoriteCity(context: Context, city: LocationResponse){
        city.favorite = true

        viewModelScope.launch {
            CityDatabase.getDatabase(context)?.getCityDao()?.insertCity(city)
        }
    }

    fun removeFavoriteCity(context: Context, city: LocationResponse){
        city.favorite = false

        viewModelScope.launch {
            CityDatabase.getDatabase(context)?.getCityDao()?.insertCity(city)
        }
    }

    val favoriteCitiesFromDB = MutableLiveData<ArrayList<LocationResponse>>()

    fun getFavoriteCitiesFromDB(context: Context){
        viewModelScope.launch {
            favoriteCitiesFromDB.value = CityDatabase.getDatabase(context)?.getCityDao()?.getAllFavoritesSorted() as ArrayList<LocationResponse>
        }
    }

    val recentCitiesFromDB = MutableLiveData<ArrayList<LocationResponse>>()


    fun getRecentCitiesFromDB(context: Context){
        viewModelScope.launch {
            recentCitiesFromDB.value = CityDatabase.getDatabase(context)?.getCityDao()?.getAllRecentCities() as ArrayList<LocationResponse>
        }
    }

}
