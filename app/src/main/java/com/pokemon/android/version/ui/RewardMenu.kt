package com.pokemon.android.version.ui

import android.view.View.GONE
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.model.level.*
import com.pokemon.android.version.utils.ItemUtils

class RewardMenu {
    fun loadRewardMenu(activity: MainActivity, levelData: LevelData, firstTime: Boolean) {
        activity.setContentView(R.layout.reward_layout)
        if (firstTime && levelData.id == 51) {
            activity.trainer!!.receivePokemon(activity.gameDataService.generatePokemon(131, 25))
        }
        val battleAgainButton: Button = activity.findViewById(R.id.battleAgainButton)
        if (levelData.id == 99)
            battleAgainButton.visibility = GONE
        battleAgainButton.setOnClickListener {
            when (levelData) {
                is WildBattleLevelData -> activity.mainMenu.levelMenu.battleUI.startWildBattle(activity, levelData)
                is TrainerBattleLevelData -> activity.mainMenu.levelMenu.battleUI.startTrainerBattle(activity, levelData)
                else -> activity.mainMenu.levelMenu.battleUI.startBossBattle(activity, levelData as BossBattleLevelData)
            }
        }
        val backButton: Button = activity.findViewById(R.id.rewardsBackButton)
        backButton.setOnClickListener {
            if (levelData.id == 99)
                activity.mainMenu.battleFrontierMenu.loadMenu(activity)
            else
                activity.mainMenu.loadGameMenu(activity)
        }
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rewardRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter = RewardRecyclerAdapter(
            activity,
            if (firstTime) levelData.rewards else ArrayList(levelData.rewards.filter { it.itemId != 0 })
        )
        recyclerView.adapter = adapter
        levelData.rewards.forEach {
            if (ItemUtils.isBadge(it.itemId)) {
                if (firstTime)
                    activity.trainer!!.addItem(it.itemId, it.quantity)
            } else if (it.itemId == 0) {
                if (levelData is LeaderLevelData
                    && activity.trainer!!.progression > LevelMenu.ELITE_4_LAST_LEVEL_ID
                    && levelData.id != BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID)
                    activity.trainer!!.coins += 100
                else
                    activity.trainer!!.coins += if (firstTime) it.quantity else it.quantity / 10
            } else
                activity.trainer!!.addItem(it.itemId, it.quantity)
        }
    }
}