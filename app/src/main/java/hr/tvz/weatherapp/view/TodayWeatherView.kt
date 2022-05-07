package hr.tvz.weatherapp.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import coil.load
import hr.tvz.weatherapp.R
import hr.tvz.weatherapp.databinding.TodayWeatherLayoutBinding

class TodayWeatherView(context: Context, attr: AttributeSet) : FrameLayout(context, attr) {

    private val binding: TodayWeatherLayoutBinding

    init {
        binding = TodayWeatherLayoutBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

    }

}