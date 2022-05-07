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
import hr.tvz.weatherapp.model.ChosenCity
import hr.tvz.weatherapp.model.EXTRA_CITY
import hr.tvz.weatherapp.model.FAVORITE
import hr.tvz.weatherapp.network.Network
import hr.tvz.weatherapp.network.model.LocationResponse
import kotlinx.coroutines.runBlocking

class FavoriteCityAdapter(
    private val context: Context,
    private val favoritesList: ArrayList<LocationResponse>
):  RecyclerView.Adapter<FavoriteCityAdapter.FavoriteCityViewHolder>(){

    private val cityDatabase = CityDatabase.getDatabase(context)!!
    class FavoriteCityViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = RecentCityItemBinding.bind(view)
    }

    private val photoUrl = "https://www.metaweather.com/static/img/weather/ico/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCityViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recent_city_item, parent, false)
        return FavoriteCityViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavoriteCityViewHolder, position: Int) {


        val favorite = favoritesList[position]
        val cityData = runBlocking { Network().getService().getLocation(favorite.woeid.toInt()) }

        holder.binding.cityName.text = favorite.title


        //prebacit u neki helper il nesta
        val currentTimeHour = cityData.time.subSequence(11, 13).toString().toInt()
        val currentTimeMin = cityData.time.subSequence(14, 16).toString()
        val timeFinal = currentTimeHour.toString() + ":" + currentTimeMin + " (" + cityData.timezone +")"

        holder.binding.lattLong.text = timeFinal


        //vidi jel se moze jednostavnije napisat
        if (favorite.favorite){
            val resourceID = context.resources.getIdentifier(
                "ic_icons_android_ic_star_1",
                "drawable",
                context.packageName
            )

            holder.binding.favoriteIcon.setImageResource(resourceID)
        }else{
            val resourceID = context.resources.getIdentifier(
                "ic_icons_android_ic_star_0",
                "drawable",
                context.packageName
            )

            holder.binding.favoriteIcon.setImageResource(resourceID)
        }

        holder.binding.favoriteIcon.setOnClickListener {

            if (favorite.favorite){
                val resourceID = context.resources.getIdentifier(
                    "ic_icons_android_ic_star_0",
                    "drawable",
                    context.packageName
                )

                holder.binding.favoriteIcon.setImageResource(resourceID)

                favorite.favorite = false
                runBlocking { cityDatabase.getCityDao().insertCity(favorite)}

            }else{
                val resourceID = context.resources.getIdentifier(
                    "ic_icons_android_ic_star_1",
                    "drawable",
                    context.packageName
                )

                holder.binding.favoriteIcon.setImageResource(resourceID)

                favorite.favorite = true
                runBlocking { cityDatabase.getCityDao().insertCity(favorite)}
            }
        }

        holder.binding.temperature.text = cityData.consolidated_weather[0].the_temp.toInt().toString() + "°C"
        holder.binding.weatherType.load(photoUrl + cityData.consolidated_weather[0].weather_state_abbr + ".ico")

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


    fun removeItem(position: Int){

        favoritesList[position].favorite = false
        runBlocking { cityDatabase.getCityDao().insertCity(favoritesList[position]) }

        favoritesList.remove(favoritesList[position])

        notifyItemRemoved(position)

    }

}