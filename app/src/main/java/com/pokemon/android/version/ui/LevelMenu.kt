package com.pokemon.android.version.ui

import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.level.*
import com.pokemon.android.version.ui.BattleFrontierMenu.Companion.FRONTIER_BRAIN_LEVEL_ID

class LevelMenu {
    companion object {
        const val ROUTE_3_LEVEL = 10
        const val MISTY_LEVEL = 18
        const val OTHER_BANNER_LEVEL = 19
        const val DUGTRIO_LEVEL = 24
        const val MAROWAK_LEVEL = 31
        const val ERIKA_LEVEL = 36
        const val LAPRAS_LEVEL = 47
        const val TYROGUE_LEVEL = 51
        const val ARTICUNO_LEVEL = 57
        const val BLAINE_LEVEL = 60
        const val MEGA_CHARIZARD_LEVEL_ID = 62
        const val GIOVANNI_LEVEL_ID = 65
        const val MEGA_VENUSAUR_LEVEL_ID = 69
        const val MOLTRES_LEVEL = 70
        const val ELITE_4_FIRST_LEVEL_ID = 71
        const val ELITE_4_LAST_LEVEL_ID = 75
        const val ZAPDOS_LEVEL = 78
        const val STEVEN_LEVEL_ID = 79
        const val CYNTHIA_LEVEL_ID = 83
        const val MEWTWO_LEVEL = 88
        const val LAST_LEVEL = 89
    }

    var battleUI: BattleUI = BattleUI()

    private fun startBattle(activity: MainActivity, level: LevelData) {
        when (level) {
            is WildBattleLevelData -> battleUI.startWildBattle(
                activity,
                level
            )
            is BossBattleLevelData -> {
                battleUI.startBossBattle(activity, level)
            }
            is LeaderLevelData -> battleUI.startGymLeaderBattle(activity, level)
            else -> battleUI.startTrainerBattle(
                activity,
                level as TrainerBattleLevelData
            )
        }
    }

    fun loadEliteLevels(activity: MainActivity) {
        activity.setContentView(R.layout.level_menu)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.levelRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val backButton: Button = activity.findViewById(R.id.levelMenuBackButton)
        backButton.setOnClickListener {
            activity.mainMenu.loadGameMenu(activity)
        }
        val levels =
            activity.gameDataService.levels.filter { it.id >= ELITE_4_FIRST_LEVEL_ID && it.id - ELITE_4_FIRST_LEVEL_ID == activity.trainer!!.eliteProgression }
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            val levelData: LevelData = levels[position]
            if (activity.trainer!!.canStillBattle())
                startBattle(activity, levelData)
        }
        val adapter = LevelRecyclerAdapter(activity, levels, myItemClickListener)
        recyclerView.adapter = adapter
    }

    fun loadLevelMenu(activity: MainActivity) {
        activity.setContentView(R.layout.level_menu)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.levelRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val backButton: Button = activity.findViewById(R.id.levelMenuBackButton)
        backButton.setOnClickListener {
            activity.mainMenu.loadGameMenu(activity)
        }
        val levels =
            activity.gameDataService.levels.filter { it.id <= activity.trainer!!.progression && it.id !in ELITE_4_FIRST_LEVEL_ID..ELITE_4_LAST_LEVEL_ID }
                .reversed()
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            val levelData: LevelData = levels[position]
            if (activity.trainer!!.canStillBattle()) {
                if (activity.trainer!!.progression == levelData.id) {
                    loadLevelDescriptionMenu(activity, levelData)
                } else if (activity.trainer!!.progression > levelData.id) {
                    startBattle(activity, levels[position])
                }
            } else
                Toast.makeText(activity, "You need to heal your Pokemon before starting a battle.", Toast.LENGTH_SHORT)
                    .show()

        }
        val adapter = LevelRecyclerAdapter(activity, levels, myItemClickListener)
        recyclerView.adapter = adapter
    }

    fun loadLevelDescriptionMenu(activity: MainActivity, levelData: LevelData) {
        activity.updateMusic(R.raw.level_descr)
        activity.setContentView(R.layout.level_description)
        val backButton: Button = activity.findViewById(R.id.levelDescrBackButton)
        backButton.setOnClickListener {
            activity.updateMusic(R.raw.main_menu)
            if (levelData.id == FRONTIER_BRAIN_LEVEL_ID)
                activity.mainMenu.battleFrontierMenu.loadMenu(activity)
            else
                loadLevelMenu(activity)
        }
        val startButton: Button = activity.findViewById(R.id.levelDescrStartButton)
        startButton.setOnClickListener {
            startBattle(activity, levelData)
        }
        val levelNameDescriptionTextView: TextView = activity.findViewById(R.id.levelNameDescrTextView)
        levelNameDescriptionTextView.text = levelData.name
        val levelDescriptionTextView: TextView = activity.findViewById(R.id.levelDescrTextView)
        levelDescriptionTextView.text = levelData.description
    }
}