package com.pokemon.android.version.ui

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.model.battle.BattleFrontierArea
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.utils.MoveUtils

class PokemonInfoMenu(var parentId: Int) {
    private var moveDetailsMenu = MoveDetailsMenu(parentId)

    fun displayPokemonInfo(activity: MainActivity, pokemon: Pokemon) {
        val nameTextView: TextView = activity.findViewById(R.id.nameDetailsTextView)
        nameTextView.setTextColor(ColorUtils.getColorByType(pokemon.data.type1))
        nameTextView.text = pokemon.data.name
        val levelTextView: TextView = activity.findViewById(R.id.levelDetailsTextView)
        levelTextView.text = activity.getString(R.string.level, pokemon.level)
        val hpValueTextView: TextView = activity.findViewById(R.id.hpDetailsTextView)
        hpValueTextView.text = activity.getString(R.string.hp, pokemon.currentHP, pokemon.hp)
        val attackTextView: TextView = activity.findViewById(R.id.attackTextView)
        attackTextView.text = activity.getString(R.string.attack, pokemon.attack)
        val defenseTextView: TextView = activity.findViewById(R.id.defenseTextView)
        defenseTextView.text = activity.getString(R.string.defense, pokemon.defense)
        val spAtkTextView: TextView = activity.findViewById(R.id.spAtkTextView)
        spAtkTextView.text = activity.getString(R.string.spAtk, pokemon.spAtk)
        val spDefTextView: TextView = activity.findViewById(R.id.spDefTextView)
        spDefTextView.text = activity.getString(R.string.spDef, pokemon.spDef)
        val speedTextView: TextView = activity.findViewById(R.id.speedTextView)
        speedTextView.text = activity.getString(R.string.speed, pokemon.speed)

        val type1TextView: TextView = activity.findViewById(R.id.type1TextView)
        type1TextView.setTextColor(ColorUtils.getColorByType(pokemon.data.type1))
        type1TextView.text = pokemon.data.type1.toString()

        val type2TextView: TextView = activity.findViewById(R.id.type2TextView)
        if (pokemon.data.type2 != Type.NONE) {
            type2TextView.setTextColor(ColorUtils.getColorByType(pokemon.data.type2))
            type2TextView.text = pokemon.data.type2.toString()
        }

        val imageView: ImageView = activity.findViewById(R.id.pokemonSpriteDetailsView)

        val abiltiesButton: Button = activity.findViewById(R.id.abilitiesButton)
        abiltiesButton.setOnClickListener {
            activity.setContentView(R.layout.ability_layout)
            val recyclerView = activity.findViewById<RecyclerView>(R.id.abilityRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            val adapter = AbilityRecyclerAdapter(activity, pokemon.data.abilities)
            recyclerView.adapter = adapter
            val backButton: Button = activity.findViewById(R.id.abilitiesMenuBackButton)
            backButton.setOnClickListener {
                if (moveDetailsMenu.parent == R.layout.battle_frontier_prep) {
                    activity.mainMenu.battleFrontierMenu.loadPokemonInfoLayout(
                        activity,
                        pokemon,
                        BattleFrontierArea.BATTLE_FACTORY
                    )
                } else {
                    activity.mainMenu.pokemonMenu.pokemonInfoMenu.loadPokemonInfoLayout(activity, pokemon)
                }
            }
        }

        activity.displayPokemon(pokemon.data.id, pokemon.shiny, imageView)
    }

    private fun displayMoveButton(
        activity: MainActivity,
        pokemon: Pokemon,
        move: PokemonMove,
        buttonId: Int,
        ppTextViewId: Int,
        area: BattleFrontierArea?
    ) {
        val moveButton: Button = activity.findViewById(buttonId)
        moveButton.visibility = View.VISIBLE
        moveButton.text = move.move.name
        moveButton.setBackgroundColor(ColorUtils.getColorByType(move.move.type))
        moveButton.setOnClickListener {
            if (parentId == R.layout.pokemons_menu)
                moveDetailsMenu.loadMoveMenu(activity, pokemon, move.move, null)
            else
                moveDetailsMenu.loadMoveMenu(activity, pokemon, move.move, area!!)
        }
        val ppMoveTextView: TextView = activity.findViewById(ppTextViewId)
        ppMoveTextView.visibility = View.VISIBLE
        ppMoveTextView.text = activity.getString(R.string.move_pp, move.pp, move.move.pp)
    }

    fun displayMoveButtons(activity: MainActivity, pokemon: Pokemon, area: BattleFrontierArea?) {
        displayMoveButton(activity, pokemon, pokemon.move1, R.id.move1InfoButton, R.id.ppMove1TextView, area)
        if (pokemon.move2 != null) {
            displayMoveButton(activity, pokemon, pokemon.move2!!, R.id.move2InfoButton, R.id.ppMove2TextView, area)
        }
        if (pokemon.move3 != null) {
            displayMoveButton(activity, pokemon, pokemon.move3!!, R.id.move3InfoButton, R.id.ppMove3TextView, area)
        }
        if (pokemon.move4 != null) {
            displayMoveButton(activity, pokemon, pokemon.move4!!, R.id.move4InfoButton, R.id.ppMove4TextView, area)
        }
    }

    fun loadMovesLayout(activity: MainActivity, pokemon: Pokemon) {
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
                activity.playSoundEffect(R.raw.evolve_sound_effect)
                loadPokemonInfoLayout(activity, pokemon)
            } else if (selectedMoveNumber != null) {
                val pokemonMove = PokemonMove(possibleMoves[position], possibleMoves[position].pp)
                when (selectedMoveNumber) {
                    0 -> pokemon.move1 = pokemonMove
                    1 -> pokemon.move2 = pokemonMove
                    2 -> pokemon.move3 = pokemonMove
                    3 -> pokemon.move4 = pokemonMove
                }
                activity.playSoundEffect(R.raw.evolve_sound_effect)
                loadPokemonInfoLayout(activity, pokemon)
            } else {
                MoveDetailsMenu(R.layout.move_layout).loadMoveMenu(activity, pokemon, possibleMoves[position], null)
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
                activity.mainMenu.pokemonMenu.loadPokemonMenuElite(activity)
            else {
                if (parentId == R.layout.pokemons_menu)
                    activity.mainMenu.pokemonMenu.loadPokemonMenu(activity)
                else
                    activity.mainMenu.battleFrontierMenu.loadMenu(activity)
            }
        }
        val useItemButton: Button = activity.findViewById(R.id.useItemButton)
        useItemButton.setOnClickListener {
            activity.mainMenu.itemMenu.loadItemMenu(activity, pokemon)
        }
        val movesButton: Button = activity.findViewById(R.id.movesButton)
        movesButton.setOnClickListener {
            loadMovesLayout(activity, pokemon)
        }
        val statusTextView: TextView = activity.findViewById(R.id.statusDetailsTextView)
        if (pokemon.status != Status.OK) {
            statusTextView.text = pokemon.status.toBattleIcon()
            statusTextView.setTextColor(ColorUtils.getColorByStatus(pokemon.status))
        } else
            statusTextView.visibility = View.GONE
        val healButton: Button = activity.findViewById(R.id.healButton)
        healButton.setOnClickListener {
            activity.trainer!!.heal(pokemon, true)
            if (pokemon.status == Status.OK)
                statusTextView.text = ""
            displayPokemonInfo(activity, pokemon)
            displayMoveButtons(activity, pokemon, null)
        }
        val evolveButton: Button = activity.findViewById(R.id.evolveButton)
        if (pokemon.canEvolve()) {
            evolveButton.visibility = View.VISIBLE
            evolveButton.setOnClickListener {
                if (pokemon.data.evolutions.size == 1) {
                    pokemon.evolve(activity.gameDataService, pokemon.data.evolutions[0].evolutionId)
                    displayPokemonInfo(activity, pokemon)
                    displayMoveButtons(activity, pokemon, null)
                    activity.playSoundEffect(R.raw.evolve_sound_effect)
                    evolveButton.visibility = View.GONE
                } else {
                    chooseEvolution(activity, pokemon)
                }
            }
        }
        if (!activity.eliteMode && !activity.trainer!!.team.contains(pokemon)
            && activity.trainer!!.pokemons.count { pokemon.data.id == it.data.id } > 1
        ) {
            var alreadyClicked = false
            val releaseButton: Button = activity.findViewById(R.id.releaseButton)
            releaseButton.visibility = View.VISIBLE
            releaseButton.setOnClickListener {
                if (alreadyClicked) {
                    activity.trainer!!.pokemons.remove(pokemon)
                    activity.mainMenu.pokemonMenu.loadPokemonMenu(activity)
                } else {
                    Toast.makeText(
                        activity,
                        "WARNING: Another click will release this Pokemon!",
                        Toast.LENGTH_SHORT
                    ).show()
                    alreadyClicked = true
                }

            }
        }
        displayMoveButtons(activity, pokemon, null)
    }

    private fun chooseEvolution(activity: MainActivity, pokemon: Pokemon) {
        val evolutions: ArrayList<Pokemon> = arrayListOf()
        pokemon.getPossibleEvolutions().forEach {
            evolutions.add(activity.gameDataService.generatePokemon(it, pokemon.level))
        }
        activity.setContentView(R.layout.pokemons_menu)
        val backButton: Button = activity.findViewById(R.id.pokemonMenuBackButton)
        backButton.setOnClickListener {
            loadPokemonInfoLayout(activity, pokemon)
        }
        val teamButton: Button = activity.findViewById(R.id.buildTeamButton)
        teamButton.visibility = View.GONE
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            pokemon.evolve(activity.gameDataService, evolutions[position].data.id)
            loadPokemonInfoLayout(activity, pokemon)
            activity.playSoundEffect(R.raw.evolve_sound_effect)
        }
        val adapter = PokemonRecyclerAdapter(activity, evolutions, myItemClickListener, true)
        recyclerView.adapter = adapter
    }
}