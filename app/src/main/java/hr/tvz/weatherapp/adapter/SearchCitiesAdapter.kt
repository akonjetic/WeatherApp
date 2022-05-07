package hr.tvz.weatherapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.weatherapp.R
import hr.tvz.weatherapp.databinding.SearchCityItemBinding
import hr.tvz.weatherapp.model.ChosenCity
import hr.tvz.weatherapp.model.EXTRA_CITY
import hr.tvz.weatherapp.network.model.LocationSearchResponse

class SearchCitiesAdapter(
    private val context: Context,
    private val citiesList: ArrayList<LocationSearchResponse>
) : RecyclerView.Adapter<SearchCitiesAdapter.SearchCitiesViewHolder>(){

    private val limit = 1

    class SearchCitiesViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = SearchCityItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCitiesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_city_item, parent, false)
        return SearchCitiesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchCitiesViewHolder, position: Int) {
        val city = citiesList[position]
        holder.binding.cityName.text = city.title

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, ChosenCity::class.java).apply {
                putExtra(EXTRA_CITY, city.woeid)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return if(citiesList.size > limit){
            limit
        } else{
            citiesList.size
        }
    }


}