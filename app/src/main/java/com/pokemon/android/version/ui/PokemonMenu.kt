package com.pokemon.android.version.ui

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.utils.MoveUtils

class PokemonMenu {
    fun loadPokemonMenuElite(activity: MainActivity) {
        activity.setContentView(R.layout.pokemons_menu)
        val backButton: Button = activity.findViewById(R.id.pokemonMenuBackButton)
        backButton.setOnClickListener {
            activity.mainMenu.loadGameMenu(activity)
        }
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            loadPokemonInfoLayout(activity, activity.trainer!!.pokemons[position])
        }
        val pokemons: MutableList<Pokemon> = (activity.trainer!!.team).toMutableList()
        val adapter = PokemonRecyclerAdapter(activity, pokemons, myItemClickListener)
        recyclerView.adapter = adapter
        val teamButton: Button = activity.findViewById(R.id.buildTeamButton)
        teamButton.setOnClickListener {
            loadTeamMenuElite(activity)
        }
    }

    private fun loadTeamMenuElite(activity: MainActivity) {
        activity.setContentView(R.layout.team_layout)
        val teamSize = activity.trainer!!.team.size
        val backButton: Button = activity.findViewById(R.id.teamMenuBackButton)
        backButton.setOnClickListener {
            if (activity.trainer!!.team.size == teamSize) {
                loadPokemonMenuElite(activity)
            }
            else
                Toast.makeText(activity, "You need to select all your Pokemon.", Toast.LENGTH_SHORT).show()
        }
        val teamRecyclerView = activity.findViewById<RecyclerView>(R.id.teamRecyclerView)
        teamRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val pokemonsRecyclerView = activity.findViewById<RecyclerView>(R.id.pokemonsRecyclerView)
        pokemonsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val pokemons: MutableList<Pokemon> = (activity.trainer!!.team).toMutableList()
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            if (activity.trainer!!.team.size < teamSize && !activity.trainer!!.team.contains(pokemons[position])) {
                activity.trainer!!.team.add(pokemons[position])
                teamRecyclerView.adapter =
                    TeamMemberRecyclerAdapter(activity, activity.trainer!!.team) {}
            }
        }
        pokemonsRecyclerView.adapter = TeamMemberRecyclerAdapter(activity, pokemons, myItemClickListener)
        activity.trainer!!.team.clear()
    }


    fun loadPokemonMenu(activity: MainActivity) {
        activity.setContentView(R.layout.pokemons_menu)
        val backButton: Button = activity.findViewById(R.id.pokemonMenuBackButton)
        backButton.setOnClickListener {
            activity.mainMenu.loadGameMenu(activity)
        }
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            loadPokemonInfoLayout(activity, activity.trainer!!.pokemons[position])
        }
        val pokemons: MutableList<Pokemon> = (activity.trainer!!.team + activity.trainer!!.pokemons.filter {
            !activity.trainer!!.team.contains(it)
        }).toMutableList()
        val adapter = PokemonRecyclerAdapter(activity, pokemons, myItemClickListener)
        recyclerView.adapter = adapter
        val teamButton: Button = activity.findViewById(R.id.buildTeamButton)
        teamButton.setOnClickListener {
            loadTeamMenu(activity)
        }
    }

    private fun loadTeamMenu(activity: MainActivity) {
        activity.setContentView(R.layout.team_layout)
        val backButton: Button = activity.findViewById(R.id.teamMenuBackButton)
        backButton.setOnClickListener {
            if (activity.trainer!!.team.size > 0) {
                loadPokemonMenu(activity)
                SaveManager.save(activity)
            }
            else
                Toast.makeText(activity, "You need at least one Pokemon on your team.", Toast.LENGTH_SHORT).show()
        }
        activity.trainer!!.team.clear()
        val teamRecyclerView = activity.findViewById<RecyclerView>(R.id.teamRecyclerView)
        teamRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val pokemonsRecyclerView = activity.findViewById<RecyclerView>(R.id.pokemonsRecyclerView)
        pokemonsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val pokemons: MutableList<Pokemon> = (activity.trainer!!.team + activity.trainer!!.pokemons.filter {
            !activity.trainer!!.team.contains(it)
        }).toMutableList()
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            if (activity.trainer!!.team.size < 6 && !activity.trainer!!.team.contains(pokemons[position])) {
                activity.trainer!!.team.add(pokemons[position])
                teamRecyclerView.adapter =
                    TeamMemberRecyclerAdapter(activity, activity.trainer!!.team) {}
                activity.trainer!!.pokemons = ArrayList(activity.trainer!!.team + activity.trainer!!.pokemons.filter { pokemon ->
                    !activity.trainer!!.team.contains(pokemon)
                })
            }
        }
        pokemonsRecyclerView.adapter = TeamMemberRecyclerAdapter(activity, pokemons, myItemClickListener)
    }

    private fun displayPokemonInfo(activity: MainActivity, pokemon: Pokemon) {
        val nameTextView: TextView = activity.findViewById(R.id.nameDetailsTextView)
        nameTextView.text = pokemon.data.name
        val levelTextView: TextView = activity.findViewById(R.id.levelDetailsTextView)
        levelTextView.text = "Level ${pokemon.level}"
        val hpValueTextView: TextView = activity.findViewById(R.id.hpValueDetailsTextView)
        hpValueTextView.text = "${pokemon.currentHP}/${pokemon.hp}"
        val attackTextView: TextView = activity.findViewById(R.id.attackTextView)
        attackTextView.text = "Attack: ${pokemon.attack}"
        val defenseTextView: TextView = activity.findViewById(R.id.defenseTextView)
        defenseTextView.text = "Defense: ${pokemon.defense}"
        val spAtkTextView: TextView = activity.findViewById(R.id.spAtkTextView)
        spAtkTextView.text = "SpAtk: ${pokemon.spAtk}"
        val spDefTextView: TextView = activity.findViewById(R.id.spDefTextView)
        spDefTextView.text = "SpDef: ${pokemon.spDef}"
        val speedTextView: TextView = activity.findViewById(R.id.speedTextView)
        speedTextView.text = "Speed: ${pokemon.speed}"
        val imageView: ImageView = activity.findViewById(R.id.pokemonSpriteDetailsView)
        activity.displayPokemon(pokemon.data.id, imageView)
    }

    private fun displayMoveButton(activity: MainActivity, move : PokemonMove, buttonId : Int, ppTextViewId : Int){
        val moveButton: Button = activity.findViewById(buttonId)
        moveButton.visibility = VISIBLE
        moveButton.text = move.move.name
        moveButton.setBackgroundColor(ColorUtils.getColorByType(move.move.type))
        val ppMoveTextView: TextView = activity.findViewById(ppTextViewId)
        ppMoveTextView.visibility = VISIBLE
        ppMoveTextView.text = "${move.pp}/${move.move.pp}"
    }

    private fun displayMoveButtons(activity: MainActivity, pokemon: Pokemon) {
        displayMoveButton(activity,pokemon.move1,R.id.move1InfoButton, R.id.ppMove1TextView)
        if (pokemon.move2 != null) {
            displayMoveButton(activity,pokemon.move2!!,R.id.move2InfoButton, R.id.ppMove2TextView)
        }
        if (pokemon.move3 != null) {
            displayMoveButton(activity,pokemon.move3!!,R.id.move3InfoButton, R.id.ppMove3TextView)
        }
        if (pokemon.move4 != null) {
            displayMoveButton(activity,pokemon.move4!!,R.id.move4InfoButton, R.id.ppMove4TextView)
        }
    }

    private fun loadMovesLayout(activity: MainActivity, pokemon: Pokemon) {
        var selectedMoveNumber: Int? = null
        activity.setContentView(R.layout.move_layout)
        val backButton: Button = activity.findViewById(R.id.moveMenuBackButton)
        backButton.setOnClickListener {
            loadPokemonInfoLayout(activity, pokemon)
        }
        val currentMovesRecyclerView = activity.findViewById<RecyclerView>(R.id.currentMovesRecyclerView)
        currentMovesRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val movesRecyclerView = activity.findViewById<RecyclerView>(R.id.movesRecyclerView)
        movesRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val currentMoves: ArrayList<Move> = ArrayList(MoveUtils.getMoveList(pokemon).map { it.move })
        val possibleMoves: ArrayList<Move> = ArrayList(MoveUtils.getPossibleMoves(pokemon))
        val currentMovesItemClickListener = View.OnClickListener {
            selectedMoveNumber = it.tag as Int
        }
        currentMovesRecyclerView.adapter = MoveRecyclerAdapter(activity, currentMoves, currentMovesItemClickListener)

        val movesItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            if (currentMoves.size < 4) {
                pokemon.autoLearnMove(possibleMoves[position])
                loadPokemonInfoLayout(activity, pokemon)
            } else if (selectedMoveNumber != null) {
                val pkmnMove = PokemonMove(possibleMoves[position], possibleMoves[position].pp)
                when (selectedMoveNumber) {
                    0 -> pokemon.move1 = pkmnMove
                    1 -> pokemon.move2 = pkmnMove
                    2 -> pokemon.move3 = pkmnMove
                    3 -> pokemon.move4 = pkmnMove
                }
                activity.playSoundEffect(R.raw.evolve_sound_effect)
                loadPokemonInfoLayout(activity, pokemon)
            }
        }
        movesRecyclerView.adapter = MoveRecyclerAdapter(activity, possibleMoves, movesItemClickListener)
    }

    fun loadPokemonInfoLayout(activity: MainActivity, pokemon: Pokemon) {
        activity.setContentView(R.layout.pokemon_info)
        displayPokemonInfo(activity, pokemon)
        val backButton: Button = activity.findViewById(R.id.pokemonInfoBackButton)
        backButton.setOnClickListener {
            if (activity.eliteMode)
                loadPokemonMenuElite(activity)
            else
                loadPokemonMenu(activity)
        }
        val useItemButton: Button = activity.findViewById(R.id.useItemButton)
        useItemButton.setOnClickListener {
            activity.mainMenu.itemMenu.loadItemMenu(activity, pokemon)
        }
        val movesButton: Button = activity.findViewById(R.id.movesButton)
        movesButton.setOnClickListener {
            loadMovesLayout(activity, pokemon)
        }
        val healButton: Button = activity.findViewById(R.id.healButton)
        healButton.setOnClickListener {
            activity.trainer!!.heal(pokemon)
            displayPokemonInfo(activity, pokemon)
            displayMoveButtons(activity, pokemon)
        }
        val statusTextView: TextView = activity.findViewById(R.id.statusDetailsTextView)
        if (pokemon.status != Status.OK)
            statusTextView.text = pokemon.status.toString()
        else
            statusTextView.visibility = GONE
        val evolveButton: Button = activity.findViewById(R.id.evolveButton)
        if (pokemon.canEvolve()) {
            evolveButton.visibility = VISIBLE
            evolveButton.setOnClickListener {
                if (pokemon.data.evolutions.size == 1) {
                    pokemon.evolve(activity.gameDataService, pokemon.data.evolutions[0].evolutionId)
                    displayPokemonInfo(activity, pokemon)
                    displayMoveButtons(activity, pokemon)
                    activity.playSoundEffect(R.raw.evolve_sound_effect)
                    evolveButton.visibility = GONE
                } else {
                    chooseEvolution(activity, pokemon)
                }
            }
        }
        displayMoveButtons(activity, pokemon)
    }

    private fun chooseEvolution(activity: MainActivity, pokemon: Pokemon) {
        val evolutions: ArrayList<Pokemon> = arrayListOf()
        pokemon.getPossibleEvolutions().forEach {
            evolutions.add(activity.gameDataService.generatePokemon(it, pokemon.level))
        }
        activity.setContentView(R.layout.pokemons_menu)
        val backButton: Button = activity.findViewById(R.id.pokemonMenuBackButton)
        backButton.setOnClickListener {
            activity.mainMenu.pokemonMenu.loadPokemonInfoLayout(activity, pokemon)
        }
        val teamButton: Button = activity.findViewById(R.id.buildTeamButton)
        teamButton.visibility = GONE
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            pokemon.evolve(activity.gameDataService, evolutions[position].data.id)
            loadPokemonInfoLayout(activity, pokemon)
            activity.playSoundEffect(R.raw.evolve_sound_effect)
        }
        val adapter = PokemonRecyclerAdapter(activity, evolutions, myItemClickListener)
        recyclerView.adapter = adapter
    }
}