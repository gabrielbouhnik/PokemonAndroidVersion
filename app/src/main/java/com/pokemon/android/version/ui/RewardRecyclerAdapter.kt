package com.pokemon.android.version.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.level.BonusReward
import com.pokemon.android.version.model.level.Reward

class RewardRecyclerAdapter(
    var activity: MainActivity,
    val data: MutableList<Reward>
) :
    RecyclerView.Adapter<RewardRecyclerAdapter.ViewHolder>() {

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
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        val name = activity.gameDataService.items.first{it.id == currentItem.itemId}.name
        holder.nameTextView.text = name
        holder.quantityTextView.text = "x ${currentItem.quantity}"
        if (currentItem is BonusReward) {
            holder.nameTextView.setTextColor(Color.RED)
            holder.quantityTextView.setTextColor(Color.RED)
        }
        holder.itemView.tag = position
    }
}