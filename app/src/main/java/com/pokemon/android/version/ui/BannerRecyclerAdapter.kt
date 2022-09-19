package com.pokemon.android.version.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.banner.Banner
import java.io.InputStream

class BannerRecyclerAdapter(
    var activity: MainActivity,
    var data: List<Banner>
) :
    RecyclerView.Adapter<BannerRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var descriptionTextView: TextView = itemView.findViewById(R.id.bannerDescriptionTextView)
        var costTextView: TextView = itemView.findViewById(R.id.bannerCostTextView)
        var spriteView: ImageView = itemView.findViewById(R.id.bannerImageView)
        var summonButton: Button = itemView.findViewById(R.id.summonButton)
        var arrowImageView: ImageView = itemView.findViewById(R.id.arrowImageView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(activity)
            .inflate(R.layout.banner_item, parent, false)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        if (position == 0 && position == data.size - 1)
            holder.arrowImageView.visibility = GONE
        if (position == 0) {
            val img: InputStream = activity.assets.open("images/right.png")
            holder.arrowImageView.setImageDrawable(Drawable.createFromStream(img, "images/right.png"))
        }
        if (position == data.size - 1) {
            val img: InputStream = activity.assets.open("images/left.png")
            holder.arrowImageView.setImageDrawable(Drawable.createFromStream(img, "images/left.png"))
        }
        holder.descriptionTextView.text = currentItem.description
        holder.costTextView.text = "COST: ${currentItem.cost} AndroCoins"
        holder.itemView.tag = position
        holder.summonButton.setOnClickListener {
            val s = currentItem.summon(activity)
            if (s != null) {
                activity.playSoundEffect(R.raw.item_sound_effect)
                activity.mainMenu.bannerMenu.coinsTextView.text = "${activity.trainer!!.coins} AndroCoins"
                activity.mainMenu.bannerMenu.loadSummonResultScreen(activity, currentItem, s)
            }
        }
        val img: InputStream = activity.assets.open(currentItem.image)
        holder.spriteView.setImageDrawable(Drawable.createFromStream(img, currentItem.image))
    }
}
