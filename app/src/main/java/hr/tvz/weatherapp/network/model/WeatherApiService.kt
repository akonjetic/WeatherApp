package hr.tvz.weatherapp.network.model

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApiService {

    @GET("api/location/search")
    suspend fun locationSearch(@Query("query") query: String): ArrayList<LocationSearchResponse>

    @GET("api/location/{woeid}")
    suspend fun getLocation(@Path("woeid") woeid: Int): LocationResponse

    @GET("api/location/{woeid}/{date}")
    suspend fun getLocationDay(@Path("woeid") woeid: Int, @Path("date") date: String): ArrayList<CityData>


}