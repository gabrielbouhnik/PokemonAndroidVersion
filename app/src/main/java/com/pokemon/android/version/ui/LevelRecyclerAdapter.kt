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
import com.pokemon.android.version.model.level.LevelData
import java.io.InputStream

class LevelRecyclerAdapter (var activity : MainActivity,
                            var data : List<LevelData>,
                            private val onItemClickListener: View.OnClickListener) :
    RecyclerView.Adapter<LevelRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView
        var levelImageView: ImageView
        init {
            nameTextView = itemView.findViewById(R.id.levelNameTextView)
            levelImageView = itemView.findViewById(R.id.levelItemImageView)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(activity)
            .inflate(R.layout.level_item, parent, false)
        rowView.setOnClickListener(onItemClickListener)
        val viewHolder = ViewHolder(rowView)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.nameTextView.text = currentItem.name
        holder.itemView.tag = position
        var img : InputStream = activity.assets.open(currentItem.icon)
        holder.levelImageView.setImageDrawable(Drawable.createFromStream(img, currentItem.background))
    }
}