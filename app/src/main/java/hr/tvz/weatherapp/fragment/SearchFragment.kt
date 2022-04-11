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

        viewModel.listOfLocations.observe(viewLifecycleOwner){
            val adapter = SearchCitiesAdapter(requireContext(), it)
            binding.recyclerView.adapter = adapter
        }


            binding.searchEditText.doAfterTextChanged {
                viewModel.getLocationList(binding.searchEditText.text.toString())
            }


        return binding.root
    }


}