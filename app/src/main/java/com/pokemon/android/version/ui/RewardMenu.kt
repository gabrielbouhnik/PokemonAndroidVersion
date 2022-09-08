package com.pokemon.android.version.ui

import android.graphics.drawable.Drawable
import android.view.View.GONE
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Gender
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.model.level.*
import com.pokemon.android.version.utils.ItemUtils
import java.io.InputStream

class RewardMenu {
    private fun updateAchievements(activity: MainActivity, levelData: LevelData) {
        when (levelData.id) {
            LevelMenu.DUGTRIO_LEVEL -> {
                if (activity.trainer!!.team.none { it.level > 35 })
                    activity.trainer!!.achievements!!.dugtrioAchievement = true
            }
            LevelMenu.ARTICUNO_LEVEL -> {
                if (activity.trainer!!.team.none { it.level > 55 })
                    activity.trainer!!.achievements!!.articunoAchievement = true
            }
            LevelMenu.MOLTRES_LEVEL -> {
                if (activity.trainer!!.team.none { it.level > 70 })
                    activity.trainer!!.achievements!!.moltresAchievement = true
            }
            LevelMenu.ZAPDOS_LEVEL -> {
                if (activity.trainer!!.team.none { it.data.type1 == Type.GROUND || it.data.type2 == Type.GROUND })
                    activity.trainer!!.achievements!!.zapdosAchievement = true
            }
            LevelMenu.MEWTWO_LEVEL -> {
                if (activity.trainer!!.team.size <= 3)
                    activity.trainer!!.achievements!!.mewtwoAchievement = true
            }
            BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID -> {
                if (activity.trainer!!.battleTowerProgression!!.progression < 10)
                    activity.trainer!!.achievements!!.winstreak8Achievement = true
                else
                    activity.trainer!!.achievements!!.winstreak15Achievement = true
            }
        }
    }

    fun loadRewardMenu(activity: MainActivity, levelData: LevelData, firstTime: Boolean) {
        activity.setContentView(R.layout.reward_layout)
        if (activity.trainer!!.progression > LevelMenu.ELITE_4_FIRST_LEVEL_ID) {
            updateAchievements(activity, levelData)
            if (levelData is LeaderLevelData &&
                !activity.trainer!!.achievements!!.leadersDefeatedAfterTheLeague.contains(levelData.id) &&
                levelData.id != BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID
            )
                activity.trainer!!.achievements!!.leadersDefeatedAfterTheLeague.add(levelData.id)
        }
        if (firstTime && levelData.id == LevelMenu.LAPRAS_LEVEL) {
            activity.trainer!!.receivePokemon(activity.gameDataService.generatePokemon(131, 25))
        }
        val battleAgainButton: Button = activity.findViewById(R.id.battleAgainButton)
        if (levelData.id == BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID)
            battleAgainButton.visibility = GONE
        battleAgainButton.setOnClickListener {
            when (levelData) {
                is WildBattleLevelData -> activity.mainMenu.levelMenu.battleUI.startWildBattle(activity, levelData)
                is TrainerBattleLevelData -> activity.mainMenu.levelMenu.battleUI.startTrainerBattle(
                    activity,
                    levelData
                )
                is LeaderLevelData -> activity.mainMenu.levelMenu.battleUI.startGymLeaderBattle(activity, levelData)
                else -> activity.mainMenu.levelMenu.battleUI.startBossBattle(activity, levelData as BossBattleLevelData)
            }
        }
        val backButton: Button = activity.findViewById(R.id.rewardsBackButton)
        backButton.setOnClickListener {
            if (levelData.id == BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID) {
                activity.updateMusic(R.raw.main_menu)
                activity.mainMenu.battleFrontierMenu.loadMenu(activity)
            } else
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
            if (ItemUtils.isBadge(it.itemId) || it.itemId == 30) {
                if (!activity.trainer!!.items.contains(it.itemId))
                    activity.trainer!!.addItem(it.itemId, it.quantity)
            } else if (it.itemId == 0) {
                if (levelData is LeaderLevelData
                    && activity.trainer!!.progression > LevelMenu.ELITE_4_LAST_LEVEL_ID
                    && levelData.id != BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID
                )
                    activity.trainer!!.coins += 100
                else
                    activity.trainer!!.coins += if (firstTime) it.quantity else it.quantity / 10
            } else
                activity.trainer!!.addItem(it.itemId, it.quantity)
        }
    }

    fun loadHallOfFameMenu(activity: MainActivity){
        activity.setContentView(R.layout.hall_of_fame)
        val trainerSprite: ImageView = activity.findViewById(R.id.trainerHOFspriteView)
        if (activity.trainer!!.gender == Gender.MALE){
            val img: InputStream = activity.assets.open(MainMenu.BOY_SPRITE)
            trainerSprite.setImageDrawable(Drawable.createFromStream(img, MainMenu.BOY_SPRITE))
        } else{
            val img: InputStream = activity.assets.open(MainMenu.GIRL_SPRITE)
            trainerSprite.setImageDrawable(Drawable.createFromStream(img, MainMenu.GIRL_SPRITE))
        }
        activity.displayPokemon(activity.trainer!!.team[0].data.id,activity.findViewById(R.id.pokemonHOFspriteView))
        if (activity.trainer!!.team.size > 1)
            activity.displayPokemon(activity.trainer!!.team[1].data.id,activity.findViewById(R.id.pokemonHOFspriteView2))
        if (activity.trainer!!.team.size > 2)
            activity.displayPokemon(activity.trainer!!.team[2].data.id,activity.findViewById(R.id.pokemonHOFspriteView3))
        if (activity.trainer!!.team.size > 3)
            activity.displayPokemon(activity.trainer!!.team[3].data.id,activity.findViewById(R.id.pokemonHOFspriteView4))
        if (activity.trainer!!.team.size > 4)
            activity.displayPokemon(activity.trainer!!.team[4].data.id,activity.findViewById(R.id.pokemonHOFspriteView5))
        if (activity.trainer!!.team.size > 5)
            activity.displayPokemon(activity.trainer!!.team[5].data.id,activity.findViewById(R.id.pokemonHOFspriteView6))
        val backButton : Button = activity.findViewById(R.id.HOFBackButton)
        backButton.setOnClickListener{
            activity.mainMenu.loadGameMenu(activity)
        }
    }
}