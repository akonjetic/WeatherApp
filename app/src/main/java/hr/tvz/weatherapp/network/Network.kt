package hr.tvz.weatherapp.network

import hr.tvz.weatherapp.network.model.WeatherApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Network {

    private val service: WeatherApiService
    val baseUrl = "https://www.metaweather.com/"

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        val httpClient = OkHttpClient.Builder().addInterceptor(interceptor)
        val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build()).build()

        service = retrofit.create(WeatherApiService::class.java)
    }

    fun getService(): WeatherApiService = service
}