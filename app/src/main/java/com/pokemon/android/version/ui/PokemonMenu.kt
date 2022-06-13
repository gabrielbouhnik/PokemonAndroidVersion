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
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.utils.MoveUtils

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
        val teamButton : Button = activity.findViewById(R.id.buildTeamButton)
        teamButton.setOnClickListener{
            loadTeamMenu(activity)
        }
    }

    fun loadTeamMenu(activity : MainActivity){
        activity.setContentView(R.layout.team_layout)
        val backButton : Button = activity.findViewById(R.id.teamMenuBackButton)
        backButton.setOnClickListener{
            if (activity.trainer!!.team.size > 0)
                loadPokemonMenu(activity)
        }
        activity.trainer!!.team.clear()
        val teamRecyclerView = activity.findViewById<RecyclerView>(R.id.teamRecyclerView)
        teamRecyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val pokemonsRecyclerView = activity.findViewById<RecyclerView>(R.id.pokemonsRecyclerView)
        pokemonsRecyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            if (activity.trainer!!.team.size < 6 && !activity.trainer!!.team.contains(activity.trainer!!.pokemons[position])) {
                activity.trainer!!.team.add(activity.trainer!!.pokemons[position])
                teamRecyclerView.adapter = PokemonRecyclerAdapter(activity, activity.trainer!!.team, View.OnClickListener {})
            }
        }
        pokemonsRecyclerView.adapter = PokemonRecyclerAdapter(activity, activity.trainer!!.pokemons, myItemClickListener)
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
        ColorUtils.setButtonColor(pokemon.move1.move.type, move1Button)
        val ppMove1TextView : TextView = activity.findViewById(R.id.ppMove1TextView)
        ppMove1TextView.visibility = VISIBLE
        if (pokemon.move1.pp < 0)
            pokemon.move1.pp = 0
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
            ColorUtils.setButtonColor(pokemon.move2!!.move.type, move2Button)
            val ppMove2TextView : TextView = activity.findViewById(R.id.ppMove2TextView)
            ppMove2TextView.visibility = VISIBLE
            if (pokemon.move2!!.pp < 0)
                pokemon.move2!!.pp = 0
            ppMove2TextView.text = "${pokemon.move2!!.pp}/${pokemon.move2!!.move.pp}"
        }
        if (pokemon.move3 == null){
            move3Button.visibility = GONE
        }
        else {
            move3Button.visibility = VISIBLE
            move3Button.text = pokemon.move3!!.move.name
            ColorUtils.setButtonColor(pokemon.move3!!.move.type, move3Button)
            val ppMove3TextView : TextView = activity.findViewById(R.id.ppMove3TextView)
            ppMove3TextView.visibility = VISIBLE
            if (pokemon.move3!!.pp < 0)
                pokemon.move3!!.pp = 0
            ppMove3TextView.text = "${pokemon.move3!!.pp}/${pokemon.move3!!.move.pp}"
        }
        if (pokemon.move4 == null){
            move4Button.visibility = GONE
        }
        else {
            move4Button.visibility = VISIBLE
            move4Button.text = pokemon.move4!!.move.name
            ColorUtils.setButtonColor(pokemon.move4!!.move.type, move4Button)
            val ppMove4TextView : TextView = activity.findViewById(R.id.ppMove4TextView)
            ppMove4TextView.visibility = VISIBLE
            if (pokemon.move4!!.pp < 0)
                pokemon.move4!!.pp = 0
            ppMove4TextView.text = "${pokemon.move4!!.pp}/${pokemon.move4!!.move.pp}"
        }
    }

    private fun loadMovesLayout(activity : MainActivity, pokemon: Pokemon){
        var selectedMoveNumber : Int? = null
        activity.setContentView(R.layout.move_layout)
        val backButton : Button = activity.findViewById(R.id.moveMenuBackButton)
        backButton.setOnClickListener{
            loadPokemonInfoLayout(activity, pokemon)
        }
        val currentMovesRecyclerView = activity.findViewById<RecyclerView>(R.id.currentMovesRecyclerView)
        currentMovesRecyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val movesRecyclerView = activity.findViewById<RecyclerView>(R.id.movesRecyclerView)
        movesRecyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val currentMoves : ArrayList<Move> = ArrayList(MoveUtils.getMoveList(pokemon).map{it.move})
        val possibleMoves : ArrayList<Move> = ArrayList(MoveUtils.getPossibleMoves(pokemon))
        val currentMovesItemClickListener = View.OnClickListener {
            selectedMoveNumber = it.tag as Int
        }
        currentMovesRecyclerView.adapter = MoveRecyclerAdapter(activity,currentMoves , currentMovesItemClickListener)

        val movesItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            if (currentMoves.size < 4) {
                pokemon.autoLearnMove(possibleMoves[position])
                loadPokemonInfoLayout(activity, pokemon)
            }
            else if (selectedMoveNumber != null){
                var pkmnMove = PokemonMove(possibleMoves[position],possibleMoves[position].pp)
                when(selectedMoveNumber){
                    0 -> pokemon.move1 = pkmnMove
                    1 -> pokemon.move2 = pkmnMove
                    2 -> pokemon.move3 = pkmnMove
                    3 -> pokemon.move4 = pkmnMove
                }
                loadPokemonInfoLayout(activity, pokemon)
            }
        }
        movesRecyclerView.adapter = MoveRecyclerAdapter(activity, possibleMoves, movesItemClickListener)
    }

    fun loadPokemonInfoLayout(activity : MainActivity, pokemon: Pokemon) {
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
        val movesButton : Button = activity.findViewById(R.id.movesButton)
        movesButton.setOnClickListener{
            loadMovesLayout(activity, pokemon)
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
                activity.playSoundEffect(R.raw.evolve_sound_effect)
                evolveButton.visibility = GONE
            }
        }
        displayMoveButtons(activity,pokemon)
    }
}