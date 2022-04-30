package hr.tvz.weatherapp

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    protected suspend fun <T : Any> safeCall(call: suspend () -> T): T?{
        try {
            return call()
        } catch (e: Throwable) {
            println(e.message)
        }
        return null
    }

}
