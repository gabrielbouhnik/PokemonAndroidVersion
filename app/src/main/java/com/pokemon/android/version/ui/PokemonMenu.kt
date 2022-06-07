package com.pokemon.android.version.ui

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status

class PokemonMenu {
    fun loadPokemonMenu(activity : MainActivity){
        activity.setContentView(R.layout.pokemons_menu)
        val backButton : Button = activity.findViewById(R.id.pokemonMenuBackButton)
        backButton.setOnClickListener{
            activity.mainMenu.loadGameMenu(activity)
        }
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            loadPokemonInfoLayout(activity, activity.trainer!!.pokemons[position])
        }
        val adapter =  PokemonRecyclerAdapter(activity, activity.trainer!!.pokemons, myItemClickListener)
        recyclerView.adapter = adapter
    }

    private fun displayPokemonInfo(activity : MainActivity, pokemon: Pokemon){
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
        val imageView: ImageView = activity.findViewById(R.id.pokemonSpriteDetailsView)
        activity.displayPokemon(pokemon.data.id,imageView)
    }

    private fun displayMoveButtons(activity : MainActivity, pokemon: Pokemon) {
        val move1Button : Button = activity.findViewById(R.id.move1InfoButton)
        move1Button.text = pokemon.move1.move.name
        val ppMove1TextView : TextView = activity.findViewById(R.id.ppMove1TextView)
        ppMove1TextView.visibility = VISIBLE
        ppMove1TextView.text = "${pokemon.move1.pp}/${pokemon.move1.move.pp}"
        val move2Button : Button = activity.findViewById(R.id.move2InfoButton)
        val move3Button : Button = activity.findViewById(R.id.move3InfoButton)
        val move4Button : Button = activity.findViewById(R.id.move4InfoButton)
        if (pokemon.move2 == null){
            move2Button.visibility = GONE
        }
        else {
            move2Button.visibility = VISIBLE
            move2Button.text = pokemon.move2!!.move.name
            val ppMove2TextView : TextView = activity.findViewById(R.id.ppMove2TextView)
            ppMove2TextView.visibility = VISIBLE
            ppMove2TextView.text = "${pokemon.move2!!.pp}/${pokemon.move2!!.move.pp}"
        }
        if (pokemon.move3 == null){
            move3Button.visibility = GONE
        }
        else {
            move3Button.visibility = VISIBLE
            move3Button.text = pokemon.move3!!.move.name
            val ppMove3TextView : TextView = activity.findViewById(R.id.ppMove3TextView)
            ppMove3TextView.visibility = VISIBLE
            ppMove3TextView.text = "${pokemon.move3!!.pp}/${pokemon.move3!!.move.pp}"
        }
        if (pokemon.move4 == null){
            move4Button.visibility = GONE
        }
        else {
            move4Button.visibility = VISIBLE
            move4Button.text = pokemon.move4!!.move.name
            val ppMove4TextView : TextView = activity.findViewById(R.id.ppMove4TextView)
            ppMove4TextView.visibility = VISIBLE
            ppMove4TextView.text = "${pokemon.move4!!.pp}/${pokemon.move4!!.move.pp}"
        }
    }

    private fun loadPokemonInfoLayout(activity : MainActivity, pokemon: Pokemon) {
        activity.setContentView(R.layout.pokemon_info)
        displayPokemonInfo(activity, pokemon)
        val backButton : Button = activity.findViewById(R.id.pokemonInfoBackButton)
        backButton.setOnClickListener{
            loadPokemonMenu(activity)
        }
        val useItemButton : Button = activity.findViewById(R.id.useItemButton)
        useItemButton.setOnClickListener{
            activity.mainMenu.itemMenu.loadItemMenu(activity, pokemon)
        }
        var statusTextView :  TextView = activity.findViewById(R.id.statusDetailsTextView)
        if (pokemon.status != Status.OK)
            statusTextView.text = pokemon.status.toString()
        else
            statusTextView.visibility = GONE
        val evolveButton : Button = activity.findViewById(R.id.evolveButton)
        if (pokemon.canEvolve()) {
            evolveButton.visibility = VISIBLE
            evolveButton.setOnClickListener {
                pokemon.evolve(activity.gameDataService)
                displayPokemonInfo(activity, pokemon)
                displayMoveButtons(activity,pokemon)
                evolveButton.visibility = GONE
                //TODO save
            }
        }
        displayMoveButtons(activity,pokemon)
    }
}