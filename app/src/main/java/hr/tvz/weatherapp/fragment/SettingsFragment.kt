package hr.tvz.weatherapp.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import hr.tvz.weatherapp.MainActivity
import hr.tvz.weatherapp.MainActivityViewModel
import hr.tvz.weatherapp.R
import hr.tvz.weatherapp.databinding.FragmentSettingsBinding
import hr.tvz.weatherapp.enums.Languages
import hr.tvz.weatherapp.helpers.FormatAndDesignHelper
import hr.tvz.weatherapp.helpers.NamesFromEnumsHelper
import hr.tvz.weatherapp.model.AboutActivity
import java.util.*

class SettingsFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var locale: Locale

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.unitRadioGroup.setOnCheckedChangeListener { _, _ ->
            if (binding.metricButton.isChecked) {
                MainActivity.metrics = true
            }
            if (binding.imperialButton.isChecked) {
                MainActivity.metrics = false
            }
        }

        val languages = NamesFromEnumsHelper().getNames(enumValues<Languages>().toList())
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            languages
        )

        binding.languageSpinner.adapter = arrayAdapter

        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val chosenLanguage = p0?.getItemAtPosition(p2).toString()

                if (chosenLanguage == Languages.CROATIAN.languages) {
                    locale = Locale("hr")
                    setLocale()
                } else if (chosenLanguage == Languages.SPANISH.languages) {
                    locale = Locale("es")
                    setLocale()
                } else {
                    locale = Locale("en")
                    setLocale()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                /* ... */
            }
        }

        var cities: ArrayList<String> = arrayListOf()

        viewModel.favoriteCitiesFromDB.observe(viewLifecycleOwner) {

            cities.clear()
            cities.addAll(NamesFromEnumsHelper().getCityNames(it))

            val citiesAdapter = ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                cities
            )

            binding.citySpinner.adapter = citiesAdapter

            binding.citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val chosenCity = p0?.getItemAtPosition(p2).toString()

                    for (item in viewModel.favoriteCitiesFromDB.value!!) {
                        if (item.title == chosenCity) {
                            MainActivity.latt = item.latt_long.substringBefore(",").dropLast(3).toDouble()
                            MainActivity.long = item.latt_long.substringAfter(",").dropLast(3).toDouble()
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    /* ... */
                }
            }
        }

        binding.moreInfoTV.setOnClickListener {
            val i = Intent(requireContext(), AboutActivity::class.java)
            startActivity(i)
        }

        binding.clearFavoritesButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialog: AlertDialog = builder
                .setTitle(getString(R.string.clearFavoritesAlertTitle))
                .setMessage(getString(R.string.clearFavoritesAlertText))
                .setPositiveButton(
                    getString(R.string.clearFavoritesAlertClear)
                ) { _, _ ->
                    viewModel.removeFavoritesFromDB(requireContext())
                    Toast.makeText(requireContext(), requireContext().getString(R.string.toastFav), Toast.LENGTH_LONG).show()
                }
                .setNegativeButton(getString(R.string.clearFavoritesAlertCancel)) { _, _ ->
                }
                .create()

            dialog.show()

            FormatAndDesignHelper().setDialogDesign(requireContext(), dialog)
        }

        binding.clearRecentsButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialog: AlertDialog = builder
                .setTitle(getString(R.string.clearRecentsAlertTitle))
                .setMessage(getString(R.string.clearRecentsAlertText))
                .setPositiveButton(
                    getString(R.string.clearFavoritesAlertClear)
                ) { _, _ ->
                    viewModel.removeRecentsFromDB(requireContext())
                    Toast.makeText(requireContext(), requireContext().getString(R.string.toastRec), Toast.LENGTH_LONG).show()
                }
                .setNegativeButton(getString(R.string.clearFavoritesAlertCancel)) { _, _ ->
                }
                .create()

            dialog.show()

            FormatAndDesignHelper().setDialogDesign(requireContext(), dialog)
        }

        viewModel.getFavoriteCitiesFromDB(requireContext())

        return binding.root
    }

    @SuppressLint("ResourceType")
    fun setLocale() {
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        context?.getResources()?.updateConfiguration(
            configuration,
            context?.getResources()?.getDisplayMetrics()
        )
    }
}
