package hr.tvz.weatherapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import hr.tvz.weatherapp.MainActivityViewModel
import hr.tvz.weatherapp.adapter.DragManageAdapter
import hr.tvz.weatherapp.adapter.FavoriteCityAdapter
import hr.tvz.weatherapp.databinding.FragmentFavoritesBinding



class FavoritesFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        viewModel.favoriteCitiesFromDB.observe(viewLifecycleOwner){
            val adapter = FavoriteCityAdapter(requireContext(), it)
            binding.recyclerView.adapter = adapter

            val callback = DragManageAdapter(adapter, requireContext(), ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), ItemTouchHelper.START.or(ItemTouchHelper.END))
            val helper = ItemTouchHelper(callback)
            helper.attachToRecyclerView(binding.recyclerView)
        }

        viewModel.getFavoriteCitiesFromDB(requireContext())





        return binding.root
    }

    /*override fun onResume() {
        super.onResume()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        viewModel.favoriteCitiesFromDB.observe(viewLifecycleOwner){
            val adapter = FavoriteCityAdapter(requireContext(), it)
            binding.recyclerView.adapter = adapter

            val callback = DragManageAdapter(adapter, requireContext(), UP.or(DOWN), START.or(END))
            val helper = ItemTouchHelper(callback)
            helper.attachToRecyclerView(binding.recyclerView)
        }

        viewModel.getFavoriteCitiesFromDB(requireContext())
    }*/




}