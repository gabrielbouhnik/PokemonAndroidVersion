package com.pokemon.android.version.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.move.pokemon.MoveLearnedByLevel

class LearnsetRecyclerAdapter(
    val activity: MainActivity,
    var data: List<MoveLearnedByLevel>,
    private val onItemClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<LearnsetRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var moveLearnedNameTextView: TextView = itemView.findViewById(R.id.moveLearnedNameTextView)
        var levelLearnedTextView: TextView = itemView.findViewById(R.id.levelLearnedTextView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(activity)
            .inflate(R.layout.learnset_item, parent, false)
        rowView.setOnClickListener(onItemClickListener)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.itemView.tag = position
        holder.moveLearnedNameTextView.text = currentItem.move.name
        holder.moveLearnedNameTextView.setTextColor(ColorUtils.getColorByType(currentItem.move.type))
        holder.levelLearnedTextView.text = "Level ${currentItem.level}"
        holder.levelLearnedTextView.setTextColor(ColorUtils.getColorByType(currentItem.move.type))
    }
}