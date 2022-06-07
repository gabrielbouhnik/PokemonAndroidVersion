package com.pokemon.android.version.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Pokemon

class PokemonRecyclerAdapter (val context: Context,
                              val data: MutableList<Pokemon>,
                              private val onItemClickListener: View.OnClickListener) :
                        RecyclerView.Adapter<PokemonRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView
        var hpLevelTextView: TextView
        var spriteView: ImageView
        var canEvolveTextView : TextView

        init {
            nameTextView = itemView.findViewById(R.id.nameItemTextView)
            hpLevelTextView = itemView.findViewById(R.id.levelItemTextView)
            spriteView = itemView.findViewById(R.id.pokemonSpriteView)
            canEvolveTextView = itemView.findViewById(R.id.canEvolveTextView)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(context)
            .inflate(R.layout.pokemon_item, parent, false)
        rowView.setOnClickListener(onItemClickListener)
        val viewHolder = ViewHolder(rowView)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.nameTextView.text = currentItem.data.name
        holder.hpLevelTextView.text = "Level  ${currentItem.level}  ${currentItem.currentHP}/${currentItem.hp}"
        holder.itemView.tag = position
        Glide.with(context)
            .load(MainActivity.pokemonSpritesUrl + currentItem.data.id + ".png")
            .into(holder.spriteView)
        if (currentItem.canEvolve())
            holder.canEvolveTextView.visibility = VISIBLE
    }
}