package com.pokemon.android.version.ui

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.PokemonData
import com.pokemon.android.version.model.Type

class PokedexMenu {
    private fun loadPokedexPage(activity: MainActivity, data: PokemonData) {
        activity.setContentView(R.layout.pokedex_page)
        val spriteView: ImageView = activity.findViewById(R.id.pokedexPageSpriteView)
        Glide.with(activity)
            .load(MainActivity.pokemonSpritesUrl + data.id + ".png")
            .into(spriteView)
        val nameTextView: TextView = activity.findViewById(R.id.pokedexPageNameTextView)
        nameTextView.text = data.name
        val recyclerView = activity.findViewById<RecyclerView>(R.id.learnsetRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        if (activity.trainer!!.name == "gab" || activity.trainer!!.pokedex[data.id] == true) {
            val type1TextView: TextView = activity.findViewById(R.id.pokedexType1TextView)
            type1TextView.setTextColor(ColorUtils.getColorByType(data.type1))
            type1TextView.text = data.type1.toString()
            val type2TextView: TextView = activity.findViewById(R.id.pokedexType2TextView)
            type2TextView.setTextColor(ColorUtils.getColorByType(data.type2))
            if (data.type2 != Type.NONE)
                type2TextView.text = data.type2.toString()
            val adapter = LearnsetRecyclerAdapter(activity, data.movesByLevel) {}
            recyclerView.adapter = adapter
        } else
            recyclerView.adapter = LearnsetRecyclerAdapter(activity, listOf()) {}

        val backButton: Button = activity.findViewById(R.id.pokedexPageBackButton)
        backButton.setOnClickListener { loadPokedexMenu(activity) }
    }

    fun loadPokedexMenu(activity: MainActivity) {
        activity.setContentView(R.layout.pokedex_layout)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.pokedexRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val data = if (activity.trainer!!.name ==  "gab ") activity.gameDataService.pokemons else activity.gameDataService.pokemons.filter { it.id < 252 }
        val myItemClickListener = View.OnClickListener {
            val dexNumber = it.tag as Int + 1
            if (activity.trainer!!.name == "gab" || activity.trainer!!.pokedex.containsKey(dexNumber))
                loadPokedexPage(activity, data[dexNumber - 1])
        }
        val adapter = PokedexRecyclerAdapter(activity, data, myItemClickListener)
        recyclerView.adapter = adapter
        val backButton: Button = activity.findViewById(R.id.pokedexMenuBackButton)
        backButton.setOnClickListener { activity.mainMenu.loadGameMenu(activity) }
    }
}