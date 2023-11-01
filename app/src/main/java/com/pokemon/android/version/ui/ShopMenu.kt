package com.pokemon.android.version.ui

import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.item.ItemQuantity
import com.pokemon.android.version.model.item.ShopItem
import com.pokemon.android.version.utils.ItemUtils

class ShopMenu {
    lateinit var coinsTextView: TextView

    fun loadShopMenu(activity: MainActivity) {
        activity.setContentView(R.layout.shop_menu)
        coinsTextView = activity.findViewById(R.id.coinsShopTextView)
        coinsTextView.text = activity.getString(R.string.andro_coins, activity.trainer!!.coins)

        val backButton: Button = activity.findViewById(R.id.shopMenuBackButton)
        backButton.setOnClickListener {
            activity.mainMenu.loadGameMenu(activity)
        }
        val items = ArrayList(activity.gameDataService.shop.filter { it.badgeCountNeeded <= activity.trainer!!.getBadgeCount()
                && (it.itemId < 60 || it.itemId > 130 || !activity.trainer!!.items.contains(it.itemId))})
        val recyclerView = activity.findViewById<RecyclerView>(R.id.shopRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = ShopRecyclerAdapter(activity,items)
    }
}