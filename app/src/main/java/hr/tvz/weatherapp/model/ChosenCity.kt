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
import hr.tvz.weatherapp.helpers.FormatAndDesignHelper
import hr.tvz.weatherapp.helpers.MetricImperialHelper
import java.util.*

const val EXTRA_CITY = "EXTRA_CITY"
const val FAVORITE = "FAVORITE"

class ChosenCity : AppCompatActivity() {

    private lateinit var binding: ChosenCityBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private val photoUrl = "https://www.metaweather.com/static/img/weather/ico/"

    private val todayAdapter by lazy { TodaysWeatherAdapter(this, arrayListOf(), true) }
    private val nextFiveAdapter by lazy { TodaysWeatherAdapter(this, arrayListOf(), true) }
    private val cityInfoAdapter by lazy { ChosenCityInfoAdapter(this, arrayListOf()) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ChosenCityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val chosenCity = intent.getIntExtra(EXTRA_CITY, 0)
        var isItFavorite = intent.getBooleanExtra(FAVORITE, false)

        var unfavorited = false

        viewModel.getCityDataSpecificDate(chosenCity, FormatAndDesignHelper().getDateInString())
        viewModel.getSpecificCityData(chosenCity)

        binding.todaysWeatherRecycler.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.todaysWeatherRecycler.adapter = todayAdapter

        // vrijeme po satima
        viewModel.cityDataSpecificDate.observe(this) {
            todayAdapter.updateAdapterData(it, true)
        }

        binding.next5DaysWeatherRecycler.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.cityInfoRecycler.layoutManager = GridLayoutManager(applicationContext, 3)

        binding.favoriteIcon.isActivated = isItFavorite

        binding.next5DaysWeatherRecycler.adapter = nextFiveAdapter
        binding.cityInfoRecycler.adapter = cityInfoAdapter

        // next 5 days i info o gradu
        viewModel.cityData.observe(this) {

            nextFiveAdapter.updateAdapterData(it.consolidated_weather, false)

            cityInfoAdapter.updateInformation(it.consolidated_weather)

            binding.toolbarTitle.text = viewModel.cityData.value?.title

            binding.dayDate.text = viewModel.cityData.value?.time?.let { it1 ->
                FormatAndDesignHelper().getDateAndDayFormatted(
                    it1
                )
            }

            binding.timeHour.text = FormatAndDesignHelper().getTimeFromString(viewModel.cityData.value?.time!!, viewModel.cityData.value?.timezone!!)

            binding.weatherDesc.text = viewModel.cityData.value?.consolidated_weather!![0].weather_state_name
            binding.tempBig.text = MetricImperialHelper().getTempConverted(viewModel.cityData.value?.consolidated_weather!![0].the_temp)
            binding.weatherIcon.load(photoUrl + viewModel.cityData.value?.consolidated_weather!![0].weather_state_abbr + ".ico")

            // jel ima neki ljepsi nacin
            binding.favoriteIcon.setOnClickListener {

                if (!isItFavorite) {
                    viewModel.insertFavoriteCity(applicationContext, viewModel.cityData.value!!)
                    binding.favoriteIcon.isActivated = true
                    unfavorited = false
                } else {
                    viewModel.removeFavoriteCity(applicationContext, viewModel.cityData.value!!)
                    binding.favoriteIcon.isActivated = false

                    unfavorited = true
                }
            }
            viewModel.insertRecentCity(this, viewModel.cityData.value!!)

            if (!unfavorited and isItFavorite) {
                it.favorite = true
            }
        }

        binding.backArrow.setOnClickListener {
            finish()
        }
    }
}
