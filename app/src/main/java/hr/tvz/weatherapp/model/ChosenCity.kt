package hr.tvz.weatherapp.model

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import hr.tvz.weatherapp.MainActivityViewModel
import hr.tvz.weatherapp.R
import hr.tvz.weatherapp.databinding.ChosenCityBinding
import hr.tvz.weatherapp.view.TodayWeatherView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


const val EXTRA_CITY = "EXTRA_CITY"

class ChosenCity : AppCompatActivity(){

    private lateinit var binding: ChosenCityBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var listOfElements7Days: ArrayList<TodayWeatherView>
    private lateinit var listOfElementsToday: ArrayList<TodayWeatherView>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ChosenCityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val chosenCity = intent.getIntExtra(EXTRA_CITY, 0)
        val date = getCurrentDateTime()
        val dateInStringS = date.toString("yyyy/MM/dd")
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val photoUrl = "https://www.metaweather.com/static/img/weather/ico/"



        listOfElements7Days = ArrayList<TodayWeatherView>()
        listOfElements7Days.add(binding.day1)
        listOfElements7Days.add(binding.day2)
        listOfElements7Days.add(binding.day3)
        listOfElements7Days.add(binding.day4)
        listOfElements7Days.add(binding.day5)
        listOfElements7Days.add(binding.day6)
        listOfElements7Days.add(binding.day7)

        //city info + next 7 days
        viewModel.cityData.observe(this){

            val todayWeather = it.consolidated_weather[0]

            binding.maxTemp.text = getString(R.string.maxTemp) + " " + todayWeather.max_temp.toString()
            binding.minTemp.text = getString(R.string.minTemp) + " " + todayWeather.min_temp.toString()
            binding.wind.text = getString(R.string.wind) + " " + todayWeather.wind_speed.toString()
            binding.humidity.text = getString(R.string.humidity) + " " + todayWeather.humidity.toString()
            binding.pressure.text = getString(R.string.pressure) + " " + todayWeather.air_pressure.toString()
            binding.visibility.text = getString(R.string.visibility) + " " + todayWeather.visibility.toString()
            binding.accuracy.text = getString(R.string.accuracy) + " " + todayWeather.predictability.toString()

            var counter = 1

            //ima li info za svih 7 ili samo za 5
            if(it.consolidated_weather.size == 7) {
                for (item in listOfElements7Days) {
                    val dateInUse =
                        formatter.parse(it.consolidated_weather[counter].applicable_date)
                    item.getTimeTextView().text = SimpleDateFormat("EE").format(dateInUse)
                    item.getWeatherImageView().load(
                        photoUrl + it.consolidated_weather[counter].weather_state_abbr + ".ico"
                    )
                    item.getTempTextView().text =
                        it.consolidated_weather[counter].the_temp.toInt().toString() + "°"
                    counter++
                }
            } else{
                for(i in 0..4){
                    val dateInUse =
                        formatter.parse(it.consolidated_weather[counter].applicable_date)
                    listOfElements7Days[i].getTimeTextView().text = SimpleDateFormat("EE").format(dateInUse)
                    listOfElements7Days[i].getWeatherImageView().load(
                        photoUrl + it.consolidated_weather[counter].weather_state_abbr + ".ico"
                    )
                    listOfElements7Days[i].getTempTextView().text =
                        it.consolidated_weather[counter].the_temp.toInt().toString() + "°"
                    counter++
                }
                for(i in 5..6){
                    val newDate = LocalDate.now().plusDays((i+1).toLong()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    counter = 5
                    val dateInUse =
                        formatter.parse(newDate)
                    listOfElements7Days[i].getTimeTextView().text = SimpleDateFormat("EE").format(dateInUse)
                    listOfElements7Days[i].getWeatherImageView().load(
                        photoUrl + it.consolidated_weather[counter].weather_state_abbr + ".ico"
                    )
                    listOfElements7Days[i].getTempTextView().text =
                        it.consolidated_weather[counter].the_temp.toInt().toString() + "°"

                }

            }
            binding.toolbarTitle.text = viewModel.cityData.value?.title

            var dateFormat = SimpleDateFormat("hh:mm a")
            dateFormat.timeZone = TimeZone.getTimeZone(viewModel.cityData.value?.timezone)
            binding.timeAndTimeZone.text = dateFormat.format(date) + "(" + viewModel.cityData.value?.timezone + ")"


        }

        viewModel.getSpecificCityData(chosenCity)


        listOfElementsToday = ArrayList()
        listOfElementsToday.add(binding.time1)
        listOfElementsToday.add(binding.time2)
        listOfElementsToday.add(binding.time3)
        listOfElementsToday.add(binding.time4)
        listOfElementsToday.add(binding.time5)
        listOfElementsToday.add(binding.time6)
        listOfElementsToday.add(binding.time7)
        listOfElementsToday.add(binding.time8)
        listOfElementsToday.add(binding.time9)
        listOfElementsToday.add(binding.time10)
        listOfElementsToday.add(binding.time11)
        listOfElementsToday.add(binding.time12)

        //ovdje za vrijeme po satu

        viewModel.cityDataSpecificDate.observe(this){
            for((counter, i) in (0..23 step 2).withIndex()){
                listOfElementsToday[counter].getTimeTextView().text = "$i:00"
                listOfElementsToday[counter].getWeatherImageView().load(
                    photoUrl + it[i].weather_state_abbr + ".ico"
                )
                listOfElementsToday[counter].getTempTextView().text =
                    it[i].the_temp.toInt().toString() + "°"

            }


        }

       viewModel.getCityDataSpecificDate(chosenCity, dateInStringS)


    }



    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

}