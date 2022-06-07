package com.pokemon.android.version.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.R
import com.pokemon.android.version.model.move.Move

class MoveRecyclerAdapter (val context: Context,
                           var data: MutableList<Move>,
                           private val onItemClickListener: View.OnClickListener) :
    RecyclerView.Adapter<MoveRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var moveNameTextView: TextView
        var movePowerTextView: TextView
        var movePPTextView: TextView
        var moveCategoryTextView: TextView

        init {
            moveNameTextView = itemView.findViewById(R.id.moveNameTextView)
            movePowerTextView = itemView.findViewById(R.id.movePowerTextView)
            movePPTextView = itemView.findViewById(R.id.movePPTextView)
            moveCategoryTextView = itemView.findViewById(R.id.moveCategoryTextView)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(context)
            .inflate(R.layout.move_item, parent, false)
        rowView.setOnClickListener(onItemClickListener)
        val viewHolder = ViewHolder(rowView)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.moveNameTextView.text = currentItem.name
        holder.movePowerTextView.text = currentItem.power.toString()
        holder.movePPTextView.text = currentItem.pp.toString()
        holder.moveCategoryTextView.text = currentItem.category.toString()
    }
}