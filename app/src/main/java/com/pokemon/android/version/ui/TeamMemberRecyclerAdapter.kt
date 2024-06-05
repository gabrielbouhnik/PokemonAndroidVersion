package com.pokemon.android.version.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Pokemon

class TeamMemberRecyclerAdapter(
    val activity: MainActivity,
    val data: MutableList<Pokemon>,
    private val onItemClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<TeamMemberRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.teamMemberNameTextView)
        var hpLevelTextView: TextView = itemView.findViewById(R.id.teamMemberLevelItemTextView)
        var spriteView: ImageView = itemView.findViewById(R.id.teamMemberSpriteView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(activity)
            .inflate(R.layout.team_item, parent, false)
        rowView.setOnClickListener(onItemClickListener)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.nameTextView.text = currentItem.data.name
        if (activity.trainer!!.team.contains(currentItem))
            holder.nameTextView.setTextColor(Color.LTGRAY)
        else
            holder.nameTextView.setTextColor(Color.DKGRAY)
        holder.hpLevelTextView.text = "Level  ${currentItem.level}  ${currentItem.currentHP}/${currentItem.hp}"
        holder.itemView.tag = position

        activity.displayPokemon(currentItem.data.id, currentItem.shiny, holder.spriteView)
    }
}