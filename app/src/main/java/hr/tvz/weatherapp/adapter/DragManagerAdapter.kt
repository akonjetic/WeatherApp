package hr.tvz.weatherapp.adapter

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class DragManagerAdapter (adapter: FavoriteCityAdapter, context: Context, dragDirs: Int, swipeDirs : Int)
    : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs){

        var favoriteCityAdapter = adapter
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
//        favoriteCityAdapter.swapItems(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    //    favoriteCityAdapter.deleteItems(viewHolder.adapterPosition)
    }


}