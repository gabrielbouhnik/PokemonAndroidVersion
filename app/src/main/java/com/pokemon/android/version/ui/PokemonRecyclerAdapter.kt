package com.pokemon.android.version.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Pokemon
import java.io.InputStream

class PokemonRecyclerAdapter(
    val activity: MainActivity,
    val data: MutableList<Pokemon>,
    private val onItemClickListener: View.OnClickListener,
    private val displayCanEvolve: Boolean
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
        holder.hpLevelTextView.text = "Level  ${currentItem.level}  ${currentItem.currentHP}/${currentItem.hp}"
        if (displayCanEvolve && currentItem.canEvolve())
            holder.canEvolveTextView.text = "CAN EVOLVE"
        else
            holder.canEvolveTextView.text = " "
        if (currentItem.isMegaEvolved) {
            holder.nameTextView.text = "Mega ${currentItem.data.name}"
            activity.displayMegaPokemon(currentItem.data.id, currentItem.shiny, holder.spriteView)
        } else {
            holder.nameTextView.text = currentItem.data.name
            activity.displayPokemon(currentItem.data.id, currentItem.shiny, holder.spriteView)
        }
    }
}