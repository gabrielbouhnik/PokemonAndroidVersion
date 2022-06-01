package com.pokemon.android.version.ui

import android.view.View
import android.view.View.GONE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Pokemon

class MainMenu {
    fun loadGameMenu(activity : MainActivity){
        activity.setContentView(R.layout.main_menu)
        var button : Button = activity.findViewById(R.id.pokemonsButton)
        button.setOnClickListener {
            loadPokemonMenu(activity)
        }
    }

    private fun loadPokemonMenu(activity : MainActivity){
        activity.setContentView(R.layout.pokemons_menu)
        val backButton : Button = activity.findViewById(R.id.pokemonMenuBackButton)
        backButton.setOnClickListener{
            loadGameMenu(activity)
        }
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            loadPokemonInfoLayout(activity, activity.trainer!!.pokemons[position])
        }
        val adapter =  PokemonRecyclerView(activity, activity.trainer!!.pokemons, myItemClickListener)
        recyclerView.adapter = adapter
    }

    private fun loadPokemonInfoLayout(activity : MainActivity, pokemon: Pokemon) {
        activity.setContentView(R.layout.pokemon_info)
        val nameTextView : TextView = activity.findViewById(R.id.nameDetailsTextView)
        nameTextView.text = pokemon.data.name
        val levelTextView : TextView = activity.findViewById(R.id.levelDetailsTextView)
        levelTextView.text = "Level ${pokemon.level}"
        val hpValueTextView : TextView = activity.findViewById(R.id.hpValueDetailsTextView)
        hpValueTextView.text = "${pokemon.currentHP}/${pokemon.hp}"
        val attackTextView : TextView = activity.findViewById(R.id.attackTextView)
        attackTextView.text = "Attack: ${pokemon.attack}"
        val defenseTextView : TextView = activity.findViewById(R.id.defenseTextView)
        defenseTextView.text = "Defense: ${pokemon.defense}"
        val spAtkTextView : TextView = activity.findViewById(R.id.spAtkTextView)
        spAtkTextView.text = "SpAtk: ${pokemon.spAtk}"
        val spDefTextView : TextView = activity.findViewById(R.id.spDefTextView)
        spDefTextView.text = "SpDef: ${pokemon.spDef}"
        val speedTextView : TextView = activity.findViewById(R.id.speedTextView)
        speedTextView.text = "Speed: ${pokemon.speed}"
        val backButton : Button = activity.findViewById(R.id.pokemonInfoBackButton)
        backButton.setOnClickListener{
            loadPokemonMenu(activity)
        }
        val move1Button : Button = activity.findViewById(R.id.move1InfoButton)
        move1Button.text = pokemon.move1.move.name
        val move2Button : Button = activity.findViewById(R.id.move2InfoButton)
        val move3Button : Button = activity.findViewById(R.id.move3InfoButton)
        val move4Button : Button = activity.findViewById(R.id.move4InfoButton)
        if (pokemon.move2 == null){
            move2Button.visibility = GONE
        }
        else
            move2Button.text = pokemon.move2!!.move.name
        if (pokemon.move3 == null){
            move3Button.visibility = GONE
        }
        else
            move3Button.text = pokemon.move3!!.move.name
        if (pokemon.move4 == null){
            move4Button.visibility = GONE
        }
        else
            move4Button.text = pokemon.move4!!.move.name
        val imageView: ImageView = activity.findViewById(R.id.pokemonSpriteDetailsView)
        activity.displayPokemon(pokemon.data.id,imageView)
    }
}