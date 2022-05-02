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
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.weatherapp.MainActivityViewModel
import hr.tvz.weatherapp.R
import hr.tvz.weatherapp.adapter.DragManagerAdapter
import hr.tvz.weatherapp.adapter.FavoriteCityAdapter
import hr.tvz.weatherapp.databinding.FragmentFavoritesBinding
import java.util.*


class FavoritesFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    var editable = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        viewModel.favoriteCitiesFromDB.observe(viewLifecycleOwner){
            val adapter = FavoriteCityAdapter(requireContext(), it)
            binding.recyclerView.adapter = adapter

           // val itemTouchHelper = ItemTouchHelper(simpleCallback)

           /* val callback = DragManagerAdapter(adapter, requireContext(), UP.or(DOWN), START.or(END))
            val helper = ItemTouchHelper(callback)
            helper.attachToRecyclerView(binding.recyclerView)*/

            var simpleCallback = object  : ItemTouchHelper.SimpleCallback(UP.or(DOWN), START.or(END)){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    var startPosition = viewHolder.adapterPosition
                    var endPosition = target.adapterPosition

                    Collections.swap(it, startPosition, endPosition)
                    binding.recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)

                    it[startPosition].crrnPos = startPosition
                    it[endPosition].crrnPos = endPosition
                    viewModel.insertFavoriteCity(requireContext(), it[startPosition])
                    viewModel.insertFavoriteCity(requireContext(), it[endPosition])

                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    adapter.removeItem(position)

                    adapter.notifyDataSetChanged()
                }

            }
            val itemTouchHelper = ItemTouchHelper(simpleCallback)


            binding.editIcon.setOnClickListener {
                if(!editable){
                    editable = true
                    binding.editIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_icons_android_ic_done))
                    itemTouchHelper.attachToRecyclerView(binding.recyclerView)
                }
                else{
                    editable = false
                    binding.editIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_icons_android_ic_edit))
                    itemTouchHelper.attachToRecyclerView(null)
                }
            }

            //itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        }

        viewModel.getFavoriteCitiesFromDB(requireContext())





        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        viewModel.favoriteCitiesFromDB.observe(viewLifecycleOwner){
            val adapter = FavoriteCityAdapter(requireContext(), it)
            binding.recyclerView.adapter = adapter

            // val itemTouchHelper = ItemTouchHelper(simpleCallback)

            /* val callback = DragManagerAdapter(adapter, requireContext(), UP.or(DOWN), START.or(END))
             val helper = ItemTouchHelper(callback)
             helper.attachToRecyclerView(binding.recyclerView)*/

            var simpleCallback = object  : ItemTouchHelper.SimpleCallback(UP.or(DOWN), START.or(END)){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    var startPosition = viewHolder.adapterPosition
                    var endPosition = target.adapterPosition

                    Collections.swap(it, startPosition, endPosition)
                    binding.recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)

                    it[startPosition].crrnPos = startPosition
                    it[endPosition].crrnPos = endPosition
                    viewModel.insertFavoriteCity(requireContext(), it[startPosition])
                    viewModel.insertFavoriteCity(requireContext(), it[endPosition])

                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    adapter.removeItem(position)

                    adapter.notifyDataSetChanged()
                }

            }
            val itemTouchHelper = ItemTouchHelper(simpleCallback)


            binding.editIcon.setOnClickListener {
                if(!editable){
                    editable = true
                    binding.editIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_icons_android_ic_done))
                    itemTouchHelper.attachToRecyclerView(binding.recyclerView)
                }
                else{
                    editable = false
                    binding.editIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_icons_android_ic_edit))
                    itemTouchHelper.attachToRecyclerView(null)
                }
            }

            //itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        }

        viewModel.getFavoriteCitiesFromDB(requireContext())
    }




}