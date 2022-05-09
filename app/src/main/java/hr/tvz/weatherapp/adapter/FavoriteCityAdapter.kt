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
import hr.tvz.weatherapp.helpers.MappingHelper
import hr.tvz.weatherapp.helpers.MetricImperialHelper
import hr.tvz.weatherapp.model.ChosenCity
import hr.tvz.weatherapp.model.EXTRA_CITY
import hr.tvz.weatherapp.model.FAVORITE
import hr.tvz.weatherapp.network.model.LocationAndCityData
import kotlinx.coroutines.runBlocking

class FavoriteCityAdapter(
    private val context: Context,
    private val favoritesList: ArrayList<LocationAndCityData>
) : RecyclerView.Adapter<FavoriteCityAdapter.FavoriteCityViewHolder>() {

    private val cityDatabase = CityDatabase.getDatabase(context)!!
    class FavoriteCityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecentCityItemBinding.bind(view)
    }

    private val photoUrl = "https://www.metaweather.com/static/img/weather/ico/"
    var favLocRes = MappingHelper().mapToLocationResponseList(favoritesList)

    fun updateFavorites(updated: ArrayList<LocationAndCityData>) {
        favoritesList.clear()
        favoritesList.addAll(updated)
        favLocRes = MappingHelper().mapToLocationResponseList(favoritesList)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCityViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recent_city_item, parent, false)
        return FavoriteCityViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavoriteCityViewHolder, position: Int) {

        val favorite = favoritesList[position]
        val locationResponseCurrent = MappingHelper().mapToLocationResponse(favorite)

        holder.binding.cityName.text = favorite.title

        val currentTimeHour = favorite.time.subSequence(11, 13).toString().toInt()
        val currentTimeMin = favorite.time.subSequence(14, 16).toString()
        val timeFinal = "$currentTimeHour:$currentTimeMin"

        holder.binding.lattLong.text = timeFinal
        holder.binding.distance.text = favorite.timezone.substringBefore("/")

        holder.binding.favoriteIcon.isActivated = favorite.favorite

        holder.binding.favoriteIcon.setOnClickListener {

            if (favorite.favorite) {
                holder.binding.favoriteIcon.isActivated = !favorite.favorite

                favorite.favorite = false
                locationResponseCurrent.favorite = false
                runBlocking { cityDatabase.getCityDao().insertCity(locationResponseCurrent) }
            } else {
                holder.binding.favoriteIcon.isActivated = !favorite.favorite

                favorite.favorite = true
                locationResponseCurrent.favorite = true
                runBlocking { cityDatabase.getCityDao().insertCity(locationResponseCurrent) }
            }
        }

        holder.binding.temperature.text = MetricImperialHelper().getTempConverted(favorite.the_temp)
        holder.binding.weatherType.load(photoUrl + favorite.weather_state_abbr + ".ico")

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, ChosenCity::class.java).apply {
                putExtra(EXTRA_CITY, favorite.woeid.toInt())
                putExtra(FAVORITE, favorite.favorite)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }

    fun removeItem(position: Int) {

        favLocRes[position].favorite = false
        favoritesList[position].favorite = false
        runBlocking { cityDatabase.getCityDao().insertCity(favLocRes[position]) }

        favoritesList.remove(favoritesList[position])

        notifyItemRemoved(position)
    }
}
