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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking

class FavoriteCityAdapter(
    private val context: Context,
    private val favoritesList: ArrayList<LocationResponse>
):  RecyclerView.Adapter<FavoriteCityAdapter.FavoriteCityViewHolder>(){

    val cityDatabase = CityDatabase.getDatabase(context)!!
    class FavoriteCityViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = RecentCityItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCityViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recent_city_item, parent, false)
        return FavoriteCityViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteCityViewHolder, position: Int) {


        val favorite = favoritesList[position]

        holder.binding.cityName.text = favorite.title
        holder.binding.lattLong.text = favorite.time

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

    fun swapItems(fromPosition: Int, toPosition: Int){
        if(fromPosition < toPosition){
            for(i in fromPosition..toPosition - 1){
                favoritesList.set(i, favoritesList.set(i+1, favoritesList.get(i)))
            }
        } else{
            for(i in fromPosition..toPosition + 1){
                favoritesList.set(i, favoritesList.set(i-1, favoritesList.get(i)))
            }
        }

        notifyItemMoved(fromPosition, toPosition)

        favoritesList[fromPosition].crrnPos = toPosition
        runBlocking {  cityDatabase.getCityDao().insertCity(favoritesList[fromPosition])}
    }

    fun removeItem(position: Int){
        favoritesList[position].favorite = false
        runBlocking { cityDatabase.getCityDao().insertCity(favoritesList[position]) }
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }
}