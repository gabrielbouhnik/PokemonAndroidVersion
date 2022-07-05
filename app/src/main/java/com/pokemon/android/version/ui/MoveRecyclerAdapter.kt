package com.pokemon.android.version.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.R
import com.pokemon.android.version.model.move.Move

class MoveRecyclerAdapter(
    val context: Context,
    var data: MutableList<Move>,
    private val onItemClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<MoveRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var moveNameTextView: TextView = itemView.findViewById(R.id.moveNameTextView)
        var movePowerTextView: TextView = itemView.findViewById(R.id.movePowerTextView)
        var movePPTextView: TextView = itemView.findViewById(R.id.movePPTextView)
        var moveCategoryTextView: TextView = itemView.findViewById(R.id.moveCategoryTextView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(context)
            .inflate(R.layout.move_item, parent, false)
        rowView.setOnClickListener(onItemClickListener)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.itemView.tag = position
        holder.moveNameTextView.text = currentItem.name
        holder.moveNameTextView.setTextColor(ColorUtils.getColorByType(currentItem.type))
        holder.movePowerTextView.text = "power: " + currentItem.power.toString()
        holder.movePPTextView.text = "pp: " + currentItem.pp.toString()
        holder.moveCategoryTextView.text = currentItem.category.toString()
    }
}