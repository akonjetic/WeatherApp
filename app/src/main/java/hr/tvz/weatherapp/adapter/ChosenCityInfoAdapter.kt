package hr.tvz.weatherapp.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.weatherapp.MainActivity
import hr.tvz.weatherapp.R
import hr.tvz.weatherapp.databinding.ChosenCityInfoItemBinding
import hr.tvz.weatherapp.helpers.MetricImperialHelper
import hr.tvz.weatherapp.network.model.CityData

class ChosenCityInfoAdapter(
    private val context: Context,
    private val information: ArrayList<CityData>
) : RecyclerView.Adapter<ChosenCityInfoAdapter.ChosenCityInfoViewHolder>() {

    class ChosenCityInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ChosenCityInfoItemBinding.bind(view)
    }

    fun updateInformation(updated: ArrayList<CityData>) {
        information.clear()
        information.addAll(updated)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChosenCityInfoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chosen_city_info_item, parent, false)

        return ChosenCityInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChosenCityInfoViewHolder, position: Int) {
        val dataNames = context.resources.getStringArray(R.array.city_info_array)
        val imageNames = context.resources.getStringArray(R.array.info_images_names)
        var cityData = arrayListOf<String>()

        if (information.size > 0) {
            if (MainActivity.metrics) {
                cityData = MetricImperialHelper().getCityDataMetric(information[0])
            } else {
                cityData = MetricImperialHelper().getCityDataImperial(information[0])
            }
            holder.binding.infoValue.text = cityData[position]
        }

        val resourceID = context.resources.getIdentifier(
            "ic_" + imageNames[position],
            "drawable",
            context.packageName
        )

        holder.binding.infoName.text = dataNames[position]
        holder.binding.infoIcon.setImageResource(resourceID)
        holder.binding.infoIcon.imageTintList = ColorStateList.valueOf(context.resources.getColor(R.color.color_primary))
    }

    override fun getItemCount(): Int {
        return 6
    }
}
