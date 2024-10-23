package com.pokemon.android.version.ui

import android.view.View
import android.view.View.GONE
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.model.BattleFrontierProgression
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.battle.BattleFrontierArea
import com.pokemon.android.version.model.battle.BattleFrontierBattle
import com.pokemon.android.version.model.level.LeaderLevelData
import com.pokemon.android.version.utils.MoveUtils

class BattleFrontierMenu {
    companion object {
        const val FRONTIER_BRAIN_LEVEL_ID = 99
    }

    var pokemonInfoMenu = PokemonInfoMenu(R.layout.battle_frontier_prep)

    fun loadPokemonInfoLayout(activity: MainActivity, pokemon: Pokemon, area: BattleFrontierArea) {
        activity.setContentView(R.layout.pokemon_info)
        pokemonInfoMenu.displayPokemonInfo(activity, pokemon)
        val backButton: Button = activity.findViewById(R.id.pokemonInfoBackButton)
        backButton.setOnClickListener {
            loadBattlePrepLayout(activity, area)
        }
        val itemTextView: TextView = activity.findViewById(R.id.heldItemTextView)
        if (pokemon.heldItem != null) {
            val itemName: String = pokemon.heldItem!!.heldItemToString()
            itemTextView.text = "Item:\n${itemName}"
        } else
            itemTextView.visibility = GONE
        val useItemButton: Button = activity.findViewById(R.id.useItemButton)
        useItemButton.visibility = GONE
        val movesButton: Button = activity.findViewById(R.id.movesButton)
        movesButton.visibility = GONE
        val healButton: Button = activity.findViewById(R.id.healButton)
        healButton.visibility = GONE
        val statusTextView: TextView = activity.findViewById(R.id.statusDetailsTextView)
        statusTextView.visibility = GONE
        pokemonInfoMenu.displayMoveButtons(activity, pokemon, area)
    }

    private fun loadBattlePrepLayout(activity: MainActivity, area: BattleFrontierArea) {
        activity.setContentView(R.layout.battle_frontier_prep)
        val battleFrontierPrepBackButton: Button = activity.findViewById(R.id.battleFrontierPrepBackButton)
        battleFrontierPrepBackButton.setOnClickListener {
            loadMenu(activity)
        }
        val resetProgressionButton: Button = activity.findViewById(R.id.resetProgressionButton)
        val winStreakTextView: TextView = activity.findViewById(R.id.winStreakTextView)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.battleFrontierRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        var alreadyClickedOnReset = false
        if (area == BattleFrontierArea.BATTLE_FACTORY) {
            winStreakTextView.text =
                activity.getString(R.string.win_streak, activity.trainer!!.battleFactoryProgression!!.progression)
            val adapter = PokemonRecyclerAdapter(activity, activity.trainer!!.battleFactoryProgression!!.team, {
                val position = it.tag as Int
                loadPokemonInfoLayout(activity, activity.trainer!!.battleFactoryProgression!!.team[position], area)
            }, false)
            resetProgressionButton.setOnClickListener {
                if (alreadyClickedOnReset) {
                    activity.trainer!!.battleFactoryProgression = null
                    SaveManager.save(activity)
                    loadMenu(activity)
                } else {
                    Toast.makeText(
                        activity,
                        "WARNING: Another click will reset your Battle Factory progression!",
                        Toast.LENGTH_SHORT
                    ).show()
                    alreadyClickedOnReset = true
                }
            }
            recyclerView.adapter = adapter
        } else {
            winStreakTextView.text =
                activity.getString(R.string.win_streak, activity.trainer!!.battleTowerProgression!!.progression)
            val adapter = PokemonRecyclerAdapter(activity, activity.trainer!!.battleTowerProgression!!.team, {
                val position = it.tag as Int
                loadPokemonInfoLayout(activity, activity.trainer!!.battleTowerProgression!!.team[position], area)
            }, false)
            recyclerView.adapter = adapter
            resetProgressionButton.setOnClickListener {
                if (alreadyClickedOnReset) {
                    activity.trainer!!.battleTowerProgression = null
                    loadMenu(activity)
                } else {
                    Toast.makeText(
                        activity,
                        "WARNING: Another click will reset your Battle Tower progression!",
                        Toast.LENGTH_SHORT
                    ).show()
                    alreadyClickedOnReset = true
                }
            }
        }
        val startBattleButton: Button = activity.findViewById(R.id.startBattleFrontierButton)
        startBattleButton.setOnClickListener {
            if (area == BattleFrontierArea.BATTLE_TOWER &&
                activity.trainer!!.battleTowerProgression!!.progression > 0 &&
                activity.trainer!!.battleTowerProgression!!.progression % 7 == 0
            ) {
                activity.mainMenu.levelMenu.loadLevelDescriptionMenu(activity,
                    activity.gameDataService.levels.find { it.id == 99 } as LeaderLevelData)
            } else
                activity.mainMenu.levelMenu.battleUI.startBattleFrontierBattle(activity, area)
        }
    }

    fun loadMenu(activity: MainActivity) {
        activity.setContentView(R.layout.battle_frontier_layout)
        val backButton: Button = activity.findViewById(R.id.battleFrontierBackButton)
        backButton.setOnClickListener {
            activity.mainMenu.loadGameMenu(activity)
        }
        val battleTowerButton: Button = activity.findViewById(R.id.battleTowerButton)
        battleTowerButton.setOnClickListener {
            if (activity.trainer!!.battleTowerProgression != null) {
                loadBattlePrepLayout(activity, BattleFrontierArea.BATTLE_TOWER)
            } else {
                activity.setContentView(R.layout.battle_tower)
                val battleTowerBackButton: Button = activity.findViewById(R.id.battleTowerBackButton)
                battleTowerBackButton.setOnClickListener {
                    loadMenu(activity)
                }
                val teamRecyclerView = activity.findViewById<RecyclerView>(R.id.teamBattleTowerRecyclerView)
                val pokemonRecyclerView = activity.findViewById<RecyclerView>(R.id.pokemonsBattleTowerRecyclerView)
                pokemonRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                teamRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                val pokemons = activity.trainer!!.pokemons.filter { it.level >= 50 }.map {
                    activity.gameDataService.generatePokemonWithMoves(it.data.id, 50, MoveUtils.getMoveList(it)
                        .map { m -> m.move },null)
                }.toMutableList()
                val team = arrayListOf<Pokemon>()
                val itemClickListener = View.OnClickListener {
                    val position = it.tag as Int
                    if (team.size < 3 && !team.contains(pokemons[position])) {
                        team.add(pokemons[position])
                        pokemons[position].trainer = activity.trainer
                        teamRecyclerView.adapter = TeamMemberRecyclerAdapter(activity, team) {}
                    }
                }
                val validateTeamButton: Button = activity.findViewById(R.id.validateTeamSelectionButton)
                validateTeamButton.setOnClickListener {
                    if (team.size < 3)
                        Toast.makeText(activity, "You need to select 3 Pokemon.", Toast.LENGTH_SHORT).show()
                    else {
                        activity.trainer!!.battleTowerProgression = BattleFrontierProgression(0, team)
                        SaveManager.save(activity)
                        loadBattlePrepLayout(activity, BattleFrontierArea.BATTLE_TOWER)
                    }
                }
                pokemonRecyclerView.adapter = TeamMemberRecyclerAdapter(activity, pokemons, itemClickListener)
            }
        }
        val battleFactoryButton: Button = activity.findViewById(R.id.battleFactoryButton)
        if (!activity.hardMode) {
            battleFactoryButton.setOnClickListener {
                if (activity.trainer!!.battleFactoryProgression == null) {
                    activity.trainer!!.battleFactoryProgression = BattleFrontierProgression(
                        0,
                        BattleFrontierBattle.generateTrainerTeam(activity.gameDataService).toMutableList()
                    )
                }
                loadBattlePrepLayout(activity, BattleFrontierArea.BATTLE_FACTORY)
            }
        } else {
            battleFactoryButton.visibility = GONE
            val battleFactoryTextView : TextView = activity.findViewById(R.id.battleFactoryTextView)
            battleFactoryTextView.visibility = GONE
        }
    }
}