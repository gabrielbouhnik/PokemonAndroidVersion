package com.pokemon.android.version.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.PokemonData
import com.pokemon.android.version.model.Type
import java.io.InputStream

class PokedexRecyclerAdapter(
    val activity: MainActivity,
    val data: List<PokemonData>,
    private val onItemClickListener: View.OnClickListener,
) :
    RecyclerView.Adapter<PokedexRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.pokedexItemNameTextView)
        var spriteView: ImageView = itemView.findViewById(R.id.pokedexItemSpriteView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(activity)
            .inflate(R.layout.pokedex_item, parent, false)
        rowView.setOnClickListener(onItemClickListener)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.itemView.tag = position
        if (activity.trainer!!.name == PokedexMenu.ADMIN || activity.trainer!!.pokedex.containsKey(currentItem.id)) {
            holder.nameTextView.text = currentItem.name
            if (activity.trainer!!.name == PokedexMenu.ADMIN || activity.trainer!!.pokedex[currentItem.id] == true)
                holder.nameTextView.setTextColor(ColorUtils.getColorByType(currentItem.type1))
            Glide.with(activity)
                .load(MainActivity.pokemonSpritesUrl + currentItem.id + ".png")
                .into(holder.spriteView)
        } else {
            holder.nameTextView.text = "???"
            holder.nameTextView.setTextColor(ColorUtils.getColorByType(Type.NONE))
            val img: InputStream = activity.assets.open("images/poke-ball.png")
            holder.spriteView.setImageDrawable(Drawable.createFromStream(img, "images/poke-ball.png"))
        }
    }
}