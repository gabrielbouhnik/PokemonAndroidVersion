package com.pokemon.android.version.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.item.ItemQuantity
import com.pokemon.android.version.utils.ItemUtils

class ItemRecyclerAdapter(
    var activity: MainActivity,
    val data: ArrayList<ItemQuantity>,
    private val onItemClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.rewardNameTextView)
        var quantityTextView: TextView = itemView.findViewById(R.id.rewardQuantityTextView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(activity)
            .inflate(R.layout.reward_item, parent, false)
        rowView.setOnClickListener(onItemClickListener)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        val name = activity.gameDataService.items[currentItem.itemId].name
        holder.nameTextView.text = name
        if (!ItemUtils.isBadge(currentItem.itemId)) {
            holder.quantityTextView.text = "x ${currentItem.quantity}"
            holder.itemView.tag = position
        }
    }
}
