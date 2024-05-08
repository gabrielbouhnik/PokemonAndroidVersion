package com.pokemon.android.version.ui

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.level.*
import java.io.InputStream

class LevelRecyclerAdapter(
    var activity: MainActivity,
    var data: List<LevelData>,
    private val onItemClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<LevelRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.levelNameTextView)
        var levelImageView: ImageView = itemView.findViewById(R.id.levelItemImageView)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(activity)
            .inflate(R.layout.level_item, parent, false)
        rowView.setOnClickListener(onItemClickListener)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        when (currentItem) {
            is WildBattleLevelData -> holder.nameTextView.setTextColor(Color.GREEN)
            is TrainerBattleLevelData -> holder.nameTextView.setTextColor(Color.BLUE)
            is LeaderLevelData -> holder.nameTextView.setTextColor(Color.YELLOW)
            is BossBattleLevelData -> holder.nameTextView.setTextColor(Color.RED)
        }
        if (currentItem.name == "\nSafari Zone")
            holder.nameTextView.setTextColor(Color.WHITE)
        holder.nameTextView.text = currentItem.name
        holder.itemView.tag = position
        val img: InputStream = activity.assets.open(currentItem.icon)
        holder.levelImageView.setImageDrawable(Drawable.createFromStream(img, currentItem.background))
    }
}