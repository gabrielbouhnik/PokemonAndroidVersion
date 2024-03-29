package com.pokemon.android.version.ui

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

class PokemonRecyclerAdapter(
    val activity: MainActivity,
    val data: MutableList<Pokemon>,
    private val onItemClickListener: View.OnClickListener,
    private val displayCanEvolve : Boolean
) :
    RecyclerView.Adapter<PokemonRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.nameItemTextView)
        var hpLevelTextView: TextView = itemView.findViewById(R.id.levelItemTextView)
        var spriteView: ImageView = itemView.findViewById(R.id.pokemonSpriteView)
        var canEvolveTextView: TextView = itemView.findViewById(R.id.canEvolveTextView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(activity)
            .inflate(R.layout.pokemon_item, parent, false)
        rowView.setOnClickListener(onItemClickListener)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.itemView.tag = position
        holder.nameTextView.text = currentItem.data.name
        holder.hpLevelTextView.text = "Level  ${currentItem.level}  ${currentItem.currentHP}/${currentItem.hp}"
        if (displayCanEvolve && currentItem.canEvolve())
            holder.canEvolveTextView.visibility = VISIBLE
        Glide.with(activity)
            .load(MainActivity.pokemonSpritesUrl + currentItem.data.id + ".png")
            .into(holder.spriteView)
    }
}