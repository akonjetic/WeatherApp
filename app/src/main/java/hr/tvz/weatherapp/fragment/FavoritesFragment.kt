package hr.tvz.weatherapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.weatherapp.MainActivityViewModel
import hr.tvz.weatherapp.adapter.FavoriteCityAdapter
import hr.tvz.weatherapp.databinding.FragmentFavoritesBinding
import hr.tvz.weatherapp.helpers.MappingHelper
import java.util.*

class FavoritesFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private var editable = false

    private val favoritesAdapter by lazy { FavoriteCityAdapter(requireContext(), arrayListOf()) }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = favoritesAdapter

        viewModel.favoriteCitiesDBAllData.observe(viewLifecycleOwner) {

            favoritesAdapter.updateFavorites(it)

            var simpleCallback = object : ItemTouchHelper.SimpleCallback(UP.or(DOWN), START.or(END)) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    var startPosition = viewHolder.adapterPosition
                    var endPosition = target.adapterPosition

                    Collections.swap(it, startPosition, endPosition)
                    binding.recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)

                    val locationResponse = MappingHelper().mapToLocationResponseList(it)

                    it[startPosition].crrnPos = startPosition
                    it[endPosition].crrnPos = endPosition
                    locationResponse[startPosition].crrnPos = startPosition
                    locationResponse[endPosition].crrnPos = endPosition

                    viewModel.insertFavoriteCity(requireContext(), locationResponse[startPosition])
                    viewModel.insertFavoriteCity(requireContext(), locationResponse[endPosition])

                    return true
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    favoritesAdapter.removeItem(position)
                    favoritesAdapter.notifyDataSetChanged()
                }
            }
            val itemTouchHelper = ItemTouchHelper(simpleCallback)

            binding.editIcon.setOnClickListener {
                if (!editable) {
                    editable = true
                    binding.editIcon.isActivated = editable
                    itemTouchHelper.attachToRecyclerView(binding.recyclerView)
                } else {
                    editable = false
                    binding.editIcon.isActivated = editable
                    itemTouchHelper.attachToRecyclerView(null)
                }
            }
        }

        viewModel.getFavoriteCitiesFromDB(requireContext())
        viewModel.getFavoriteCitiesDBData(requireContext())

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onResume() {
        super.onResume()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = favoritesAdapter

        viewModel.favoriteCitiesDBAllData.observe(viewLifecycleOwner) {

            favoritesAdapter.updateFavorites(it)

            var simpleCallback = object : ItemTouchHelper.SimpleCallback(UP.or(DOWN), START.or(END)) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    var startPosition = viewHolder.adapterPosition
                    var endPosition = target.adapterPosition

                    Collections.swap(it, startPosition, endPosition)
                    binding.recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)

                    val locationResponse = MappingHelper().mapToLocationResponseList(it)

                    it[startPosition].crrnPos = startPosition
                    it[endPosition].crrnPos = endPosition
                    locationResponse[startPosition].crrnPos = startPosition
                    locationResponse[endPosition].crrnPos = endPosition

                    viewModel.insertFavoriteCity(requireContext(), locationResponse[startPosition])
                    viewModel.insertFavoriteCity(requireContext(), locationResponse[endPosition])

                    return true
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    favoritesAdapter.removeItem(position)

                    favoritesAdapter.notifyDataSetChanged()
                }
            }
            val itemTouchHelper = ItemTouchHelper(simpleCallback)

            binding.editIcon.setOnClickListener {
                if (!editable) {
                    editable = true
                    binding.editIcon.isActivated = editable
                    itemTouchHelper.attachToRecyclerView(binding.recyclerView)
                } else {
                    editable = false
                    binding.editIcon.isActivated = editable
                    itemTouchHelper.attachToRecyclerView(null)
                }
            }
        }

        viewModel.getFavoriteCitiesFromDB(requireContext())
        viewModel.getFavoriteCitiesDBData(requireContext())
    }
}
