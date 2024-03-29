package com.pokemon.android.version.ui

import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.item.ItemQuantity
import com.pokemon.android.version.utils.ItemUtils

class ItemMenu {
    lateinit var items: ArrayList<ItemQuantity>

    fun loadItemMenu(activity: MainActivity, pokemon: Pokemon?) {
        activity.setContentView(R.layout.item_menu)
        val backButton: Button = activity.findViewById(R.id.itemMenuBackButton)
        backButton.setOnClickListener {
            if (pokemon != null)
                activity.mainMenu.pokemonMenu.pokemonInfoMenu.loadPokemonInfoLayout(activity, pokemon)
            else
                activity.mainMenu.loadGameMenu(activity)
        }
        items = ArrayList(
            ItemQuantity.createItemQuantityFromHashMap(activity.trainer!!.items)
                .filter { !ItemUtils.isBadge(it.itemId) })
        val recyclerView = activity.findViewById<RecyclerView>(R.id.itemRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter = ItemRecyclerAdapter(activity, items,
            if (pokemon == null) View.OnClickListener {} else View.OnClickListener {
                val position = it.tag as Int
                if (ItemUtils.getItemById(items[position].itemId).isUsable(pokemon)) {
                    activity.trainer!!.useItem(items[position].itemId, pokemon)
                    activity.mainMenu.pokemonMenu.pokemonInfoMenu.loadPokemonInfoLayout(activity, pokemon)
                    SaveManager.save(activity)
                } else {
                    Toast.makeText(activity, "This item cannot be used on this Pokemon", Toast.LENGTH_SHORT).show()
                }
            })
        recyclerView.adapter = adapter
    }
}