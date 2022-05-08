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
import hr.tvz.weatherapp.helpers.FormatAndDesignHelper
import hr.tvz.weatherapp.helpers.MetricImperialHelper
import hr.tvz.weatherapp.network.model.CityData

class TodaysWeatherAdapter(
    private val context: Context,
    private val dataList: ArrayList<CityData>,
    private var todayOr7Day: Boolean
) : RecyclerView.Adapter<TodaysWeatherAdapter.TodaysWeatherViewHolder>() {

    private val photoUrl = "https://www.metaweather.com/static/img/weather/ico/"
    private val limit = 12

    fun updateAdapterData(updated: ArrayList<CityData>, boolean: Boolean) {
        dataList.clear()
        dataList.addAll(updated)
        todayOr7Day = boolean
        notifyDataSetChanged()
    }

    class TodaysWeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = TodaysWeatherItemBinding.bind(view)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodaysWeatherAdapter.TodaysWeatherViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.todays_weather_item, parent, false)
        return TodaysWeatherViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: TodaysWeatherAdapter.TodaysWeatherViewHolder,
        position: Int
    ) {
        if (todayOr7Day) {
            val timeOfTheDay = dataList[position]
            holder.binding.temp.text = MetricImperialHelper().getTempConverted(timeOfTheDay.the_temp)
            holder.binding.weatherType.load(
                photoUrl + timeOfTheDay.weather_state_abbr + ".ico"
            )
            val exactTime = position * 2
            holder.binding.time.text = "$exactTime:00"
        } else {
            val theDay = dataList[position + 1]
            holder.binding.temp.text = MetricImperialHelper().getTempConverted(theDay.the_temp)
            holder.binding.time.text = FormatAndDesignHelper().formatDateToDayInWeek(theDay.applicable_date)
            holder.binding.weatherType.load(
                photoUrl + theDay.weather_state_abbr + ".ico"
            )
        }
    }

    override fun getItemCount(): Int {
        return if (todayOr7Day) {
            if (dataList.size > limit) limit else dataList.size
        } else {
            5
        }
    }
}
