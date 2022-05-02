package hr.tvz.weatherapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.weatherapp.R
import hr.tvz.weatherapp.database.CityDatabase
import hr.tvz.weatherapp.databinding.RecentCityItemBinding
import hr.tvz.weatherapp.model.ChosenCity
import hr.tvz.weatherapp.model.EXTRA_CITY
import hr.tvz.weatherapp.model.FAVORITE
import hr.tvz.weatherapp.network.model.LocationResponse
import kotlinx.coroutines.runBlocking

class RecentCityAdapter(
    private val context: Context,
    private val recentsList: ArrayList<LocationResponse>
): RecyclerView.Adapter<RecentCityAdapter.RecentCityViewHolder>() {

    class RecentCityViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = RecentCityItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentCityViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recent_city_item, parent, false)
        return RecentCityViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentCityViewHolder, position: Int) {
        val cityDatabase = CityDatabase.getDatabase(context)!!

       val recent = recentsList[position]
        holder.binding.cityName.text = recent.title
        holder.binding.lattLong.text = recent.latt_long

        if(!recent.favorite){
            val resourceID = context.resources.getIdentifier(
                "ic_icons_android_ic_star_0",
                "drawable",
                context.packageName
            )
            holder.binding.favoriteIcon.setImageResource(resourceID)
        } else{
            val resourceID = context.resources.getIdentifier(
                "ic_icons_android_ic_star_1",
                "drawable",
                context.packageName
            )
            holder.binding.favoriteIcon.setImageResource(resourceID)
        }

        holder.binding.favoriteIcon.setOnClickListener {
            if(!recent.favorite){
                val resourceID = context.resources.getIdentifier(
                    "ic_icons_android_ic_star_1",
                    "drawable",
                    context.packageName
                )
                holder.binding.favoriteIcon.setImageResource(resourceID)

                recent.favorite = true

                runBlocking { cityDatabase.getCityDao().insertCity(recent) }
            } else{
                val resourceID = context.resources.getIdentifier(
                    "ic_icons_android_ic_star_0",
                    "drawable",
                    context.packageName
                )
                holder.binding.favoriteIcon.setImageResource(resourceID)

                recent.favorite = false
                runBlocking { cityDatabase.getCityDao().insertCity(recent) }
            }
        }

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