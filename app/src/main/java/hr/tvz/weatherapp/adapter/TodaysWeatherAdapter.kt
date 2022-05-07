package hr.tvz.weatherapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import hr.tvz.weatherapp.R
import hr.tvz.weatherapp.databinding.TodaysWeatherItemBinding
import hr.tvz.weatherapp.network.model.CityData
import java.text.SimpleDateFormat

class TodaysWeatherAdapter(
    private val context: Context,
    private val hoursList: ArrayList<CityData>?,
    private val daysList: ArrayList<CityData>?,
    private val todayOr7Day: Boolean
) : RecyclerView.Adapter<TodaysWeatherAdapter.TodaysWeatherViewHolder>(){

    private val photoUrl = "https://www.metaweather.com/static/img/weather/ico/"
    private val limit = 12
    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    class TodaysWeatherViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = TodaysWeatherItemBinding.bind(view)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodaysWeatherAdapter.TodaysWeatherViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.todays_weather_item, parent, false)
        return TodaysWeatherViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(
        holder: TodaysWeatherAdapter.TodaysWeatherViewHolder,
        position: Int
    ) {
        if(todayOr7Day) {
            val timeOfTheDay = hoursList!![position]
            holder.binding.temp.text = timeOfTheDay.the_temp.toInt().toString()  + "°"
            holder.binding.weatherType.load(
                photoUrl + timeOfTheDay.weather_state_abbr + ".ico"
            )
            val exactTime = position * 2
            holder.binding.time.text = "$exactTime:00"
        }else{
            val theDay = daysList!![position+1]
            val dateInUse = formatter.parse(theDay.applicable_date)
            holder.binding.temp.text = theDay.the_temp.toInt().toString() + "°"
            holder.binding.time.text = SimpleDateFormat("EE").format(dateInUse)
            holder.binding.weatherType.load(
                photoUrl + theDay.weather_state_abbr + ".ico"
            )
        }

    }

    override fun getItemCount(): Int {
        return if(todayOr7Day) {
            if (hoursList!!.size > limit) limit else hoursList.size
        } else{
            5
        }
    }


}
