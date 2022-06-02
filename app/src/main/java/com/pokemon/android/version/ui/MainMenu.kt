package com.pokemon.android.version.ui

import android.view.View
import android.view.View.GONE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Pokemon

class MainMenu {
    var pokemonMenu : PokemonMenu = PokemonMenu()
    var bannerMenu : BannerMenu = BannerMenu()

    fun loadGameMenu(activity : MainActivity){
        activity.setContentView(R.layout.main_menu)
        var pokemonsButton : Button = activity.findViewById(R.id.pokemonsButton)
        pokemonsButton.setOnClickListener {
            pokemonMenu.loadPokemonMenu(activity)
        }
        var bannersButton : Button = activity.findViewById(R.id.bannersButton)
        bannersButton.setOnClickListener {
            bannerMenu.loadBannerMenu(activity)
        }
    }


}