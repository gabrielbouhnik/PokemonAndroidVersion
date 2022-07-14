package com.pokemon.android.version.ui

import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.BattleFrontierProgression
import com.pokemon.android.version.model.battle.BattleFrontierArea
import com.pokemon.android.version.model.battle.BattleFrontierBattle

class BattleFrontierMenu {
    fun loadMenu(activity: MainActivity){
        activity.setContentView(R.layout.battle_frontier_layout)
        val backButton: Button = activity.findViewById(R.id.battleFrontierBackButton)
        backButton.setOnClickListener {
            activity.mainMenu.loadGameMenu(activity)
        }
        val battleTowerButton : Button = activity.findViewById(R.id.battleTowerButton)
        battleTowerButton.setOnClickListener {
            if (activity.trainer!!.battleTowerProgression != null){
                activity.setContentView(R.layout.battle_frontier_prep)
                val battleFrontierPrepBackButton: Button = activity.findViewById(R.id.battleFrontierPrepBackButton)
                battleFrontierPrepBackButton.setOnClickListener {
                    loadMenu(activity)
                }
                val winStreakTextView : TextView = activity.findViewById(R.id.winStreakTextView)
                winStreakTextView.text = "Win Streak: ${activity.trainer!!.battleTowerProgression!!.progression}"
                val startBattleButton : Button = activity.findViewById(R.id.startBattleFrontierButton)
                startBattleButton.setOnClickListener {
                    activity.mainMenu.levelMenu.battleUI.startBattleFrontierBattle(activity, activity.trainer!!.battleTowerProgression!!.team, BattleFrontierArea.BATTLE_TOWER)
                }
            }
            else{
                activity.setContentView(R.layout.battle_tower)
                val battleTowerBackButton : Button = activity.findViewById(R.id.battleTowerBackButton)
                battleTowerBackButton.setOnClickListener {
                    loadMenu(activity)
                }
            }
        }
        val battleFactoryButton : Button = activity.findViewById(R.id.battleFactoryButton)
        battleFactoryButton.setOnClickListener {
            if (activity.trainer!!.battleFactoryProgression == null){
                activity.trainer!!.battleFactoryProgression = BattleFrontierProgression(0,BattleFrontierBattle.generateTeam(activity.gameDataService).toMutableList())
            }
            activity.setContentView(R.layout.battle_frontier_prep)
            val winStreakTextView : TextView = activity.findViewById(R.id.winStreakTextView)
            winStreakTextView.text = "Win Streak: ${activity.trainer!!.battleFactoryProgression!!.progression}"
            val battleFrontierPrepBackButton: Button = activity.findViewById(R.id.battleFrontierPrepBackButton)
            battleFrontierPrepBackButton.setOnClickListener {
                loadMenu(activity)
            }
            val recyclerView = activity.findViewById<RecyclerView>(R.id.battleFrontierRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            val adapter = PokemonRecyclerAdapter(activity, activity.trainer!!.battleFactoryProgression!!.team){}
            recyclerView.adapter = adapter
            val startBattleButton : Button = activity.findViewById(R.id.startBattleFrontierButton)
            startBattleButton.setOnClickListener {
                activity.mainMenu.levelMenu.battleUI.startBattleFrontierBattle(activity, activity.trainer!!.battleFactoryProgression!!.team, BattleFrontierArea.BATTLE_FACTORY)
            }
        }
    }
}