package hr.tvz.weatherapp.model

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import hr.tvz.weatherapp.MainActivityViewModel
import hr.tvz.weatherapp.adapter.ChosenCityInfoAdapter
import hr.tvz.weatherapp.adapter.TodaysWeatherAdapter
import hr.tvz.weatherapp.databinding.ChosenCityBinding
import hr.tvz.weatherapp.helpers.CityInfoHelper
import hr.tvz.weatherapp.network.model.CityData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


const val EXTRA_CITY = "EXTRA_CITY"
const val FAVORITE = "FAVORITE"

class ChosenCity : AppCompatActivity(){

    private lateinit var binding: ChosenCityBinding
    private val viewModel: MainActivityViewModel by viewModels()


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ChosenCityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val chosenCity = intent.getIntExtra(EXTRA_CITY, 0)
        var isItFavorite = intent.getBooleanExtra(FAVORITE, false)
        var unfavorited : Boolean = false
        val date = getCurrentDateTime()
        val dateInStringS = date.toString("yyyy/MM/dd")


        binding.todaysWeatherRecycler.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

        viewModel.getCityDataSpecificDate(chosenCity, dateInStringS)
        viewModel.getSpecificCityData(chosenCity)

        viewModel.cityDataSpecificDate.observe(this){
            val adapter = TodaysWeatherAdapter(applicationContext, it, null, true)
            binding.todaysWeatherRecycler.adapter = adapter

        }

        binding.next5DaysWeatherRecycler.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.cityInfoRecycler.layoutManager = GridLayoutManager(applicationContext, 3)

        if(isItFavorite){
            val resourceID = applicationContext.resources.getIdentifier(
                "ic_icons_android_ic_star_1",
                "drawable",
                applicationContext.packageName
            )
            binding.favoriteIcon.setImageResource(resourceID)
        } else{
                val resourceID = applicationContext.resources.getIdentifier(
                    "ic_icons_android_ic_star_0",
                    "drawable",
                    applicationContext.packageName
                )
                binding.favoriteIcon.setImageResource(resourceID)
            }



        viewModel.cityData.observe(this){
            val adapter = TodaysWeatherAdapter(applicationContext, viewModel.cityDataSpecificDate.value, it.consolidated_weather, false)
            binding.next5DaysWeatherRecycler.adapter = adapter

            val adapter2 = ChosenCityInfoAdapter(applicationContext, viewModel.cityDataSpecificDate.value)
            binding.cityInfoRecycler.adapter = adapter2

            binding.toolbarTitle.text = viewModel.cityData.value?.title

            binding.favoriteIcon.setOnClickListener {

                if (!isItFavorite) {
                    viewModel.insertFavoriteCity(applicationContext, viewModel.cityData.value!!)
                    val resourceID = applicationContext.resources.getIdentifier(
                        "ic_icons_android_ic_star_1",
                        "drawable",
                        applicationContext.packageName
                    )
                    binding.favoriteIcon.setImageResource(resourceID)
                    unfavorited = false

                } else{
                    viewModel.removeFavoriteCity(applicationContext, viewModel.cityData.value!!)
                    val resourceID = applicationContext.resources.getIdentifier(
                        "ic_icons_android_ic_star_0",
                        "drawable",
                        applicationContext.packageName
                    )
                    binding.favoriteIcon.setImageResource(resourceID)

                    unfavorited = true
                }


            }
            viewModel.insertRecentCity(this, viewModel.cityData.value!!)

            if(!unfavorited and isItFavorite){
               // viewModel.insertFavoriteCity(applicationContext, viewModel.cityData.value!!)
                it.favorite = true
            }

        }



        //u bazu doda locationsearchresponse







        }


    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

}