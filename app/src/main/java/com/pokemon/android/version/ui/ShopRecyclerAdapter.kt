package com.pokemon.android.version.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.model.item.ShopItem

class ShopRecyclerAdapter(
    var activity: MainActivity,
    val data: MutableList<ShopItem>
) : RecyclerView.Adapter<ShopRecyclerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        var buyButton: Button = itemView.findViewById(R.id.buyItemButton)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(activity)
            .inflate(R.layout.shop_item, parent, false)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        val name = activity.gameDataService.items.find{it.id == currentItem.itemId}!!.name
        holder.nameTextView.text = name
        holder.buyButton.text = "Buy for ${currentItem.cost} AndroCoins"
        holder.itemView.tag = position
        holder.buyButton.setOnClickListener {
            if (activity.trainer!!.items.containsKey(currentItem.itemId) && activity.trainer!!.items[currentItem.itemId] == 99){
                Toast.makeText(activity, "You can't have more of this item.", Toast.LENGTH_SHORT).show()
            }
            else if (activity.trainer!!.coins >= currentItem.cost){
                activity.trainer!!.addItem(currentItem.itemId,1)
                activity.trainer!!.coins -= currentItem.cost
                activity.playSoundEffect(R.raw.item_sound_effect)
                SaveManager.save(activity)
                val coinsTextView = activity.findViewById<TextView>(R.id.coinsShopTextView)
                coinsTextView.text = activity.getString(R.string.andro_coins, activity.trainer!!.coins)
            } else
                Toast.makeText(activity, "You don't have enough AndroCoins.", Toast.LENGTH_SHORT).show()
        }
    }
}