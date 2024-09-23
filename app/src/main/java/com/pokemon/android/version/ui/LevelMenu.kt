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
        const val SURGE_LEVEL = 22
        const val OTHER_BANNER_LEVEL = 19
        const val DUGTRIO_LEVEL = 25
        const val MAROWAK_LEVEL = 32
        const val ERIKA_LEVEL = 37
        const val KAREN_LEVEL = 41
        const val KOGA_LEVEL = 44
        const val LAPRAS_LEVEL = 49
        const val ARCHER_LEVEL = 51
        const val GIOVANNI_LEVEL = 52
        const val TYROGUE_LEVEL = 53
        const val SABRINA_LEVEL = 55
        const val ARTICUNO_LEVEL = 59
        const val BLAINE_LEVEL = 62
        const val OAK_LEVEL_ID = 64
        const val GIOVANNI_2_LEVEL = 66
        const val ARMORED_MEWTWO_LEVEL_ID = 67
        const val MOLTRES_LEVEL = 72
        const val ELITE_4_FIRST_LEVEL_ID = 73
        const val ELITE_4_LAST_LEVEL_ID = 77
        const val ZAPDOS_LEVEL = 80
        const val STEVEN_LEVEL_ID = 81
        const val CYNTHIA_LEVEL_ID = 85
        const val MEWTWO_LEVEL = 93
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