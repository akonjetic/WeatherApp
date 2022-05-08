package hr.tvz.weatherapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import hr.tvz.weatherapp.R
import hr.tvz.weatherapp.database.CityDatabase
import hr.tvz.weatherapp.databinding.RecentCityItemBinding
import hr.tvz.weatherapp.helpers.FormatAndDesignHelper
import hr.tvz.weatherapp.helpers.MappingHelper
import hr.tvz.weatherapp.helpers.MetricImperialHelper
import hr.tvz.weatherapp.model.ChosenCity
import hr.tvz.weatherapp.model.EXTRA_CITY
import hr.tvz.weatherapp.model.FAVORITE
import hr.tvz.weatherapp.network.model.LocationAndCityData
import kotlinx.coroutines.runBlocking

class RecentCityAdapter(
    private val context: Context,
    private val recentsList: ArrayList<LocationAndCityData>
) : RecyclerView.Adapter<RecentCityAdapter.RecentCityViewHolder>() {

    fun updateRecents(updated: ArrayList<LocationAndCityData>) {
        recentsList.clear()
        recentsList.addAll(updated)
        notifyDataSetChanged()
    }

    class RecentCityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecentCityItemBinding.bind(view)
    }

    private val photoUrl = "https://www.metaweather.com/static/img/weather/ico/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentCityViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recent_city_item, parent, false)
        return RecentCityViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecentCityViewHolder, position: Int) {
        val cityDatabase = CityDatabase.getDatabase(context)!!

        val recent = recentsList[position]
        val locRes = MappingHelper().mapToLocationResponse(recent)

        holder.binding.cityName.text = recent.title
        holder.binding.lattLong.text = FormatAndDesignHelper().getLattLongFormatted(recent.latt_long)
        holder.binding.distance.text = FormatAndDesignHelper().getDistance(context, recent.latt_long)

        holder.binding.favoriteIcon.isActivated = recent.favorite

        holder.binding.favoriteIcon.setOnClickListener {
            if (!recent.favorite) {

                holder.binding.favoriteIcon.isActivated = !recent.favorite

                recent.favorite = true
                locRes.favorite = true

                runBlocking { cityDatabase.getCityDao().insertCity(locRes) }
            } else {

                holder.binding.favoriteIcon.isActivated = !recent.favorite

                recent.favorite = false
                locRes.favorite = false

                runBlocking { cityDatabase.getCityDao().insertCity(locRes) }
            }
        }

        holder.binding.temperature.text = MetricImperialHelper().getTempConverted(recent.the_temp)
        holder.binding.weatherType.load(photoUrl + recent.weather_state_abbr + ".ico")

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, ChosenCity::class.java).apply {
                putExtra(EXTRA_CITY, recent.woeid.toInt())
                putExtra(FAVORITE, recent.favorite)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return recentsList.size
    }
}
