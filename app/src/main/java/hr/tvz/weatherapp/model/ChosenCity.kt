package hr.tvz.weatherapp.model

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import hr.tvz.weatherapp.MainActivityViewModel
import hr.tvz.weatherapp.adapter.ChosenCityInfoAdapter
import hr.tvz.weatherapp.adapter.TodaysWeatherAdapter
import hr.tvz.weatherapp.databinding.ChosenCityBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


const val EXTRA_CITY = "EXTRA_CITY"
const val FAVORITE = "FAVORITE"

class ChosenCity : AppCompatActivity(){

    private lateinit var binding: ChosenCityBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private val photoUrl = "https://www.metaweather.com/static/img/weather/ico/"


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ChosenCityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val chosenCity = intent.getIntExtra(EXTRA_CITY, 0)
        var isItFavorite = intent.getBooleanExtra(FAVORITE, false)

        var unfavorited = false

        //date-time helper neki
        val date = getCurrentDateTime()
        val dateInStringS = date.toString("yyyy/MM/dd")



        viewModel.getCityDataSpecificDate(chosenCity, dateInStringS)
        viewModel.getSpecificCityData(chosenCity)

        binding.todaysWeatherRecycler.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

        //vrijeme po satima
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





        //next 5 days i info o gradu
        viewModel.cityData.observe(this){
            val adapter = TodaysWeatherAdapter(applicationContext, viewModel.cityDataSpecificDate.value!!, it.consolidated_weather, false)
            binding.next5DaysWeatherRecycler.adapter = adapter

            val adapter2 = ChosenCityInfoAdapter(applicationContext, viewModel.cityDataSpecificDate.value!!)
            binding.cityInfoRecycler.adapter = adapter2


            //treba probat dodat title u toolbar, ne u textview
            binding.toolbarTitle.text = viewModel.cityData.value?.title


            //neki date-time helper
            val dateWithDay =  LocalDate.parse(viewModel.cityData.value?.time?.subSequence(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val dateWithDayFormatted = dateWithDay.format(DateTimeFormatter.ofPattern("E, MMM dd"))
            println(dateWithDayFormatted)

            binding.dayDate.text = dateWithDayFormatted

            val currentTimeHour = viewModel.cityData.value?.time?.subSequence(11, 13).toString().toInt()
            val currentTimeMin = viewModel.cityData.value?.time?.subSequence(14, 16).toString()
            val timeFinal = currentTimeHour.toString() + ":" + currentTimeMin + " (" + viewModel.cityData.value?.timezone +")"

            binding.timeHour.text = timeFinal
            binding.weatherDesc.text = viewModel.cityData.value?.consolidated_weather!![0].weather_state_name
            binding.tempBig.text = viewModel.cityData.value?.consolidated_weather!![0].the_temp.toInt().toString() + "°C"
            binding.weatherIcon.load(photoUrl + viewModel.cityData.value?.consolidated_weather!![0].weather_state_abbr + ".ico")



            //jel ima neki ljepsi nacin
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
                it.favorite = true
            }

        }

        binding.backArrow.setOnClickListener {
            finish()
        }

        }


    //neki date-time helper
    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

}