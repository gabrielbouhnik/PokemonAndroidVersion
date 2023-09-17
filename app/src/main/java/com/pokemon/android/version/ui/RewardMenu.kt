package com.pokemon.android.version.ui

import android.graphics.drawable.Drawable
import android.view.View.GONE
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.model.Gender
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.model.level.*
import com.pokemon.android.version.utils.ItemUtils
import java.io.InputStream
import kotlin.random.Random

class RewardMenu {
    private fun updateAchievements(activity: MainActivity, levelData: LevelData) {
        when (levelData.id) {
            LevelMenu.MISTY_LEVEL -> {
                if (activity.trainer!!.team.all { it.data.id in 152..251 })
                    activity.trainer!!.achievements!!.ceruleanGymAchievement = true
            }
            LevelMenu.DUGTRIO_LEVEL -> {
                if (activity.trainer!!.team.none { it.level > 35 })
                    activity.trainer!!.achievements!!.dugtrioAchievement = true
            }
            LevelMenu.ERIKA_LEVEL -> {
                if (activity.trainer!!.team.all { it.data.id in 152..251 })
                    activity.trainer!!.achievements!!.celadonGymAchievement = true
            }
            LevelMenu.ARTICUNO_LEVEL -> {
                if (activity.trainer!!.team.none { it.hasType(Type.ELECTRIC) })
                    activity.trainer!!.achievements!!.articunoAchievement = true
            }
            LevelMenu.BLAINE_LEVEL -> {
                if (activity.trainer!!.team.all { it.data.id in 152..251 })
                    activity.trainer!!.achievements!!.cinnbarGymAchievement = true
            }
            LevelMenu.MOLTRES_LEVEL -> {
                if (activity.trainer!!.team.none { it.hasType(Type.WATER) })
                    activity.trainer!!.achievements!!.moltresAchievement = true
            }
            LevelMenu.ZAPDOS_LEVEL -> {
                if (activity.trainer!!.team.none { it.hasType(Type.GROUND)})
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
        if (firstTime) {
            when (levelData.id) {
                1 -> {
                    activity.showCustomDialog(activity.getString(R.string.tutorial_after_first_fight))
                }
                LevelMenu.LAPRAS_LEVEL -> {
                    activity.trainer!!.receivePokemon(activity.gameDataService.generatePokemon(131, 45))
                    Toast.makeText(
                        activity,
                        "To thank you, one of the Silph Co scientist gave you a Lapras!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                LevelMenu.TYROGUE_LEVEL -> {
                    activity.trainer!!.receivePokemon(activity.gameDataService.generatePokemon(236, 10))
                    Toast.makeText(
                        activity,
                        "You received a level 10 Tyrogue!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                LevelMenu.MEGA_CHARIZARD_LEVEL_ID -> {
                    when {
                        activity.trainer!!.pokedex[1] == true -> {
                            activity.trainer!!.addItem(110, 1)
                        }
                        activity.trainer!!.pokedex[4] == true -> {
                            activity.trainer!!.addItem(111, 1)
                        }
                        activity.trainer!!.pokedex[7] == true -> {
                            activity.trainer!!.addItem(112, 1)
                        }
                    }
                }
            }
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
        val healTeamButton: Button = activity.findViewById(R.id.healTeamButton)
        if (activity.trainer!!.progression < 9 || levelData.id == BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID)
            healTeamButton.visibility = GONE
        healTeamButton.setOnClickListener {
            activity.trainer!!.team.forEach { activity.trainer!!.heal(it, false) }
            SaveManager.save(activity)
            healTeamButton.visibility = GONE
        }
        val nextLevelButton: Button = activity.findViewById(R.id.nextLevelButton)
        if (activity.trainer!!.progression < 9 || levelData.id == BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID
            || levelData.id == LevelMenu.ELITE_4_FIRST_LEVEL_ID - 1 || levelData.id == LevelMenu.ELITE_4_LAST_LEVEL_ID
            || levelData.id == LevelMenu.MEWTWO_LEVEL
        )
            nextLevelButton.visibility = GONE
        nextLevelButton.setOnClickListener {
            val nextLevelData: LevelData = activity.gameDataService.levels.findLast { it.id == levelData.id + 1 }!!
            if (activity.trainer!!.progression == levelData.id + 1)
                activity.mainMenu.levelMenu.loadLevelDescriptionMenu(activity, nextLevelData)
            else {
                when (nextLevelData) {
                    is WildBattleLevelData -> activity.mainMenu.levelMenu.battleUI.startWildBattle(
                        activity,
                        nextLevelData
                    )
                    is TrainerBattleLevelData -> activity.mainMenu.levelMenu.battleUI.startTrainerBattle(
                        activity,
                        nextLevelData
                    )
                    is LeaderLevelData -> activity.mainMenu.levelMenu.battleUI.startGymLeaderBattle(
                        activity,
                        nextLevelData
                    )
                    else -> activity.mainMenu.levelMenu.battleUI.startBossBattle(
                        activity,
                        nextLevelData as BossBattleLevelData
                    )
                }
            }
        }
        val backButton: Button = activity.findViewById(R.id.rewardsBackButton)
        backButton.setOnClickListener {
            if (levelData.id == BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID) {
                activity.updateMusic(R.raw.main_menu)
                activity.mainMenu.battleFrontierMenu.loadMenu(activity)
            } else {
                if (firstTime && levelData.id == LevelMenu.MOLTRES_LEVEL)
                    activity.showCustomDialog(activity.getString(R.string.tutorial_elite_mode))
                activity.mainMenu.loadGameMenu(activity)
            }
        }
        val rewards =
            if (firstTime) levelData.rewards else ArrayList(levelData.rewards.filter { it.itemId != 0 && it.itemId !in 50..100 })
        if ((levelData is WildBattleLevelData || levelData is BossBattleLevelData) && Random.nextInt(10) == 1) {
            rewards.add(BonusReward(Random.nextInt(150, 168), 1))
        }
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rewardRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter = RewardRecyclerAdapter(
            activity,
            rewards
        )
        recyclerView.adapter = adapter
        if (levelData is LeaderLevelData
            && activity.trainer!!.progression > LevelMenu.ELITE_4_LAST_LEVEL_ID
            && levelData.id != BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID
        )
            activity.trainer!!.coins += 200
        else if (!firstTime
            && !activity.eliteMode
            && levelData.id != BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID) {
            val coinsReward = levelData.rewards.find { it.itemId == 0 }
            if (coinsReward != null)
                activity.trainer!!.coins += coinsReward.quantity / 10
        }
        rewards.forEach {
            if (ItemUtils.isBadge(it.itemId) || it.itemId == 30) {
                if (!activity.trainer!!.items.contains(it.itemId))
                    activity.trainer!!.addItem(it.itemId, it.quantity)
            } else if (it.itemId == 0) {
                activity.trainer!!.coins += it.quantity
            } else
                activity.trainer!!.addItem(it.itemId, it.quantity)
        }
    }

    fun loadHallOfFameMenu(activity: MainActivity) {
        activity.setContentView(R.layout.hall_of_fame)
        val trainerSprite: ImageView = activity.findViewById(R.id.trainerHOFspriteView)
        if (activity.trainer!!.gender == Gender.MALE) {
            val img: InputStream = activity.assets.open(MainMenu.BOY_SPRITE)
            trainerSprite.setImageDrawable(Drawable.createFromStream(img, MainMenu.BOY_SPRITE))
        } else {
            val img: InputStream = activity.assets.open(MainMenu.GIRL_SPRITE)
            trainerSprite.setImageDrawable(Drawable.createFromStream(img, MainMenu.GIRL_SPRITE))
        }
        activity.displayPokemon(
            activity.trainer!!.team[0].data.id,
            activity.trainer!!.team[0].shiny,
            activity.findViewById(R.id.pokemonHOFspriteView)
        )
        if (activity.trainer!!.team.size > 1)
            activity.displayPokemon(
                activity.trainer!!.team[1].data.id,
                activity.trainer!!.team[1].shiny,
                activity.findViewById(R.id.pokemonHOFspriteView2)
            )
        if (activity.trainer!!.team.size > 2)
            activity.displayPokemon(
                activity.trainer!!.team[2].data.id,
                activity.trainer!!.team[2].shiny,
                activity.findViewById(R.id.pokemonHOFspriteView3)
            )
        if (activity.trainer!!.team.size > 3)
            activity.displayPokemon(
                activity.trainer!!.team[3].data.id,
                activity.trainer!!.team[3].shiny,
                activity.findViewById(R.id.pokemonHOFspriteView4)
            )
        if (activity.trainer!!.team.size > 4)
            activity.displayPokemon(
                activity.trainer!!.team[4].data.id,
                activity.trainer!!.team[4].shiny,
                activity.findViewById(R.id.pokemonHOFspriteView5)
            )
        if (activity.trainer!!.team.size > 5)
            activity.displayPokemon(
                activity.trainer!!.team[5].data.id,
                activity.trainer!!.team[5].shiny,
                activity.findViewById(R.id.pokemonHOFspriteView6)
            )
        val backButton: Button = activity.findViewById(R.id.HOFBackButton)
        backButton.setOnClickListener {
            activity.showCustomDialog(activity.getString(R.string.tutorial_post_game))
            activity.mainMenu.loadGameMenu(activity)
        }
    }
}