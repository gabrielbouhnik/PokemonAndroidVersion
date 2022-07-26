package com.pokemon.android.version.ui

import android.widget.Button
import android.widget.TextView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.battle.BattleFrontierArea
import com.pokemon.android.version.model.move.Move

class MoveDetailsMenu(var parent: Int) {
    fun loadMoveMenu(activity: MainActivity, pokemon: Pokemon, move: Move, area: BattleFrontierArea?){
        activity.setContentView(R.layout.move_details)
        val nameTextView: TextView = activity.findViewById(R.id.moveDetailsNameTextView)
        nameTextView.setTextColor(ColorUtils.getColorByType(move.type))
        nameTextView.text = move.name
        val descriptionTextView: TextView = activity.findViewById(R.id.moveDescriptionTextView)
        descriptionTextView.text = move.description
        val infoTextView: TextView = activity.findViewById(R.id.moveDetailsInfoTextView)
        infoTextView.text = activity.getString(R.string.move_info, move.power, move.type.toString(), move.pp, if (move.accuracy == null) "_" else move.accuracy.toString(), move.category.toString())
        val backButton: Button = activity.findViewById(R.id.moveDetailsBackButton)
        backButton.setOnClickListener{
            when (parent) {
                R.layout.battle_frontier_prep -> {
                    activity.mainMenu.battleFrontierMenu.loadPokemonInfoLayout(activity, pokemon, area!!)
                }
                R.layout.move_layout -> {
                    activity.mainMenu.pokemonMenu.pokemonInfoMenu.loadMovesLayout(activity, pokemon)
                }
                else -> {
                    activity.mainMenu.pokemonMenu.pokemonInfoMenu.loadPokemonInfoLayout(activity, pokemon)
                }
            }
        }
    }
}