package com.pokemon.android.version.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.banner.Banner
import java.io.InputStream

class BannerRecyclerView (var activity : MainActivity) :
    RecyclerView.Adapter<BannerRecyclerView.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var descriptionTextView: TextView
        var costTextView: TextView
        var spriteView: ImageView
        var summonButton : Button = itemView.findViewById(R.id.summonButton)

        init {
            descriptionTextView = itemView.findViewById(R.id.bannerDescriptionTextView)
            costTextView = itemView.findViewById(R.id.bannerCostTextView)
            spriteView = itemView.findViewById(R.id.bannerImageView)
        }
    }

    override fun getItemCount(): Int {
        return activity.gameDataService.banners.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(activity)
            .inflate(R.layout.banner_item, parent, false)
        val viewHolder = ViewHolder(rowView)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = activity.gameDataService.banners[position]
        holder.descriptionTextView.text = currentItem.description
        holder.costTextView.text = "${currentItem.cost} AndroCoins"
        holder.itemView.tag = position
        holder.summonButton.setOnClickListener{
            currentItem.summon(activity)
            activity.mainMenu.bannerMenu.coinsTextView.text = "${activity.trainer!!.coins} AndroCoins"
        }
        var img : InputStream = activity.assets.open(currentItem.image)
        holder.spriteView.setImageDrawable(Drawable.createFromStream(img, currentItem.image))
    }
}
