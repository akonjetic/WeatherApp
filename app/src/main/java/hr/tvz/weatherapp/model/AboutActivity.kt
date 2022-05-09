package hr.tvz.weatherapp.model

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.tvz.weatherapp.databinding.AboutLayoutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: AboutLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AboutLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backArrow.setOnClickListener {
            finish()
        }
    }
}
