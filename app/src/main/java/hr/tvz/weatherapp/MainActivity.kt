package hr.tvz.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import hr.tvz.weatherapp.databinding.ActivityMainBinding
import hr.tvz.weatherapp.fragment.FavoritesFragment
import hr.tvz.weatherapp.fragment.SearchFragment
import hr.tvz.weatherapp.fragment.SettingsFragment
import hr.tvz.weatherapp.network.model.LocationSearchResponse

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val searchFragment = SearchFragment()
        val favoritesFragment = FavoritesFragment()
        val settingsFragment = SettingsFragment()

        setCurrentFragment(searchFragment)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_search -> setCurrentFragment(searchFragment)
                R.id.ic_favorites -> setCurrentFragment(favoritesFragment)
                R.id.ic_settings -> setCurrentFragment(settingsFragment)
            }
            true
        }


    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }
}