package hr.tvz.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.weatherapp.R
import hr.tvz.weatherapp.databinding.ChosenCityInfoItemBinding
import hr.tvz.weatherapp.network.model.CityData

class ChosenCityInfoAdapter (
    private val context: Context,
    private val information: ArrayList<CityData>?
    ) : RecyclerView.Adapter<ChosenCityInfoAdapter.ChosenCityInfoViewHolder>(){

        class ChosenCityInfoViewHolder(view: View): RecyclerView.ViewHolder(view){
            val binding = ChosenCityInfoItemBinding.bind(view)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChosenCityInfoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chosen_city_info_item, parent, false)

        return ChosenCityInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChosenCityInfoViewHolder, position: Int) {
        val dataNames = context.resources.getStringArray(R.array.city_info_array)
        val imageNames = context.resources.getStringArray(R.array.info_images_names)
        val cityData = getCityData(information!![0])

        val resourceID = context.resources.getIdentifier(
            "ic_" + imageNames[position],
            "drawable",
            context.packageName
        )


        holder.binding.infoValue.text = cityData[position]
        holder.binding.infoName.text = dataNames[position]
        holder.binding.infoIcon.setImageResource(resourceID)
    }

    override fun getItemCount(): Int {
        return 6
    }


    fun getCityData(cityData: CityData): ArrayList<String> {

        val minMax: String =
            cityData.min_temp.toInt().toString() + "°" + "/" + cityData.max_temp.toInt()
                .toString() + "°"
        val wind: String =
            cityData.wind_speed.toInt().toString() + " km/h (" + cityData.wind_direction_compass + ")"

        return arrayListOf(
            minMax,
            wind,
            cityData.humidity.toInt().toString() + "%",
            cityData.air_pressure.toInt().toString() + "hPa",
            cityData.visibility.toInt().toString() + " km",
            cityData.predictability.toInt().toString() + "%"
        )
    }
}
