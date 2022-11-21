package com.pokemon.android.version.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Ability

class AbilityRecyclerAdapter(
    var activity: MainActivity,
    val data: List<Ability>
) :
    RecyclerView.Adapter<AbilityRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.abilitiyNameTextView)
        var descriptionTextView: TextView = itemView.findViewById(R.id.abilityDescrTextView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(activity)
            .inflate(R.layout.ability_item, parent, false)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.nameTextView.text = currentItem.toString().replace("_", " ")
        holder.descriptionTextView.text = currentItem.description
        holder.itemView.tag = position
    }
}