package hr.tvz.weatherapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import hr.tvz.weatherapp.MainActivityViewModel
import hr.tvz.weatherapp.R
import hr.tvz.weatherapp.adapter.FavoriteCityAdapter
import hr.tvz.weatherapp.adapter.RecentCityAdapter
import hr.tvz.weatherapp.adapter.SearchCitiesAdapter
import hr.tvz.weatherapp.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //binding.searchEditText.background = resources.getDrawable(R.color.black)

        viewModel.listOfLocations.observe(viewLifecycleOwner){
            val adapter = SearchCitiesAdapter(requireContext(), it)
            binding.recyclerView.adapter = adapter
        }


            binding.searchEditText.doAfterTextChanged {
                viewModel.getLocationList(binding.searchEditText.text.toString())
                binding.recyclerView.visibility = View.VISIBLE

                if(binding.searchEditText.text.toString() == "") {
                    binding.recyclerView.visibility = View.GONE
                }
            }

        //na promjenu liste u bazi podataka, on u novi adapter preda listu iz baze
        //u novom adapteru on fetcha nazive iz baze (a vrijeme i temp s interneta?)

        binding.recentRecycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.recentCitiesFromDB.observe(viewLifecycleOwner) {
            val adapter = RecentCityAdapter(requireContext(), it)
            binding.recentRecycler.adapter = adapter
        }



        viewModel.getRecentCitiesFromDB(requireContext())



        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.listOfLocations.observe(viewLifecycleOwner){
            val adapter = SearchCitiesAdapter(requireContext(), it)
            binding.recyclerView.adapter = adapter
        }


        binding.searchEditText.doAfterTextChanged {
            viewModel.getLocationList(binding.searchEditText.text.toString())
            binding.recyclerView.visibility = View.VISIBLE

            if(binding.searchEditText.text.toString() == ""){
               binding.recyclerView.visibility = View.GONE           }
        }


        binding.recentRecycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.recentCitiesFromDB.observe(viewLifecycleOwner) {
            val adapter = RecentCityAdapter(requireContext(), it)
            binding.recentRecycler.adapter = adapter
        }



        viewModel.getRecentCitiesFromDB(requireContext())
    }


}