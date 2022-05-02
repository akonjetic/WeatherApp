package hr.tvz.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import hr.tvz.weatherapp.R
import hr.tvz.weatherapp.databinding.TodaysWeatherItemBinding
import hr.tvz.weatherapp.network.model.CityData
import hr.tvz.weatherapp.network.model.LocationResponse
import java.text.SimpleDateFormat

class TodaysWeatherAdapter(
    private val context: Context,
    private val hoursList: ArrayList<CityData>?,
    private val daysList: ArrayList<CityData>?,
    private val todayOr7Day: Boolean
) : RecyclerView.Adapter<TodaysWeatherAdapter.TodaysWeatherViewHolder>(){

    val photoUrl = "https://www.metaweather.com/static/img/weather/ico/"
    val limit = 12
    val formatter = SimpleDateFormat("yyyy-MM-dd")

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

            /*for(item in days!!){
                val dateInUse = formatter.parse(item.applicable_date)
                holder.binding.time.text = SimpleDateFormat("EE").format(dateInUse)
                holder.binding.weatherType.load(
                    photoUrl + item.weather_state_abbr + ".ico"
                )
                holder.binding.temp.text = item.the_temp.toInt().toString() + "°"
            }*/
        }

        //holder.binding.root.se
    }

    override fun getItemCount(): Int {
       if(todayOr7Day) {
           if (hoursList!!.size > limit) return limit else return hoursList.size
       } else{
           return 5
       }
    }


}
