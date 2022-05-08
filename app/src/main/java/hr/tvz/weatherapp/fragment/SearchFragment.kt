package hr.tvz.weatherapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import hr.tvz.weatherapp.MainActivityViewModel
import hr.tvz.weatherapp.adapter.RecentCityAdapter
import hr.tvz.weatherapp.adapter.SearchCitiesAdapter
import hr.tvz.weatherapp.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchAdapter by lazy { SearchCitiesAdapter(requireContext(), arrayListOf()) }
    private val recentsAdapter by lazy { RecentCityAdapter(requireContext(), arrayListOf()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = searchAdapter

        viewModel.listOfLocations.observe(viewLifecycleOwner) {
            searchAdapter.updateCitiesList(it)
        }

        binding.searchEditText.doAfterTextChanged {
            viewModel.getLocationList(binding.searchEditText.text.toString())
            binding.recyclerView.visibility = View.VISIBLE

            if (binding.searchEditText.text.toString() == "") {
                binding.recyclerView.visibility = View.GONE
            }
        }

        binding.recentRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recentRecycler.adapter = recentsAdapter

        viewModel.recentCitiesDBAllData.observe(viewLifecycleOwner) {
            recentsAdapter.updateRecents(it)
        }

        viewModel.getRecentCitiesFromDB(requireContext())
        viewModel.getRecentCitiesFromDBData(requireContext())

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = searchAdapter

        viewModel.listOfLocations.observe(viewLifecycleOwner) {
            searchAdapter.updateCitiesList(it)
        }

        binding.searchEditText.doAfterTextChanged {
            viewModel.getLocationList(binding.searchEditText.text.toString())
            binding.recyclerView.visibility = View.VISIBLE

            if (binding.searchEditText.text.toString() == "") {
                binding.recyclerView.visibility = View.GONE
            }
        }

        binding.recentRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recentRecycler.adapter = recentsAdapter

        viewModel.recentCitiesDBAllData.observe(viewLifecycleOwner) {
            recentsAdapter.updateRecents(it)
        }

        viewModel.getRecentCitiesFromDB(requireContext())
        viewModel.getRecentCitiesFromDBData(requireContext())
    }
}
