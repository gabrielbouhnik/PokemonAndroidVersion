package com.pokemon.android.version.ui

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.level.LevelData
import com.pokemon.android.version.model.level.TrainerBattleLevelData
import com.pokemon.android.version.model.level.WildBattleLevelData

class LevelMenu {
    var battleUI: BattleUI = BattleUI()

    fun startBattle(activity: MainActivity, level: LevelData) {
        if (level is WildBattleLevelData)
            battleUI.startWildBattle(
                activity,
                level
            )
        else
            battleUI.startTrainerBattle(
                activity,
                level as TrainerBattleLevelData
            )
    }


    fun loadLevelMenu(activity: MainActivity) {
        activity.setContentView(R.layout.level_menu)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.levelRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val backButton: Button = activity.findViewById(R.id.levelMenuBackButton)
        backButton.setOnClickListener {
            activity.mainMenu.loadGameMenu(activity)
        }
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            var levelData : LevelData =  activity.gameDataService.levels[position]
            if (activity.trainer!!.canStillBattle())
                if (activity.trainer!!.progression == levelData.id) {
                    loadLevelDescriptionMenu(activity, activity.gameDataService.levels[position])
                } else if (activity.trainer!!.progression > levelData.id){
                    startBattle(activity,activity.gameDataService.levels[position])
                }
        }
        val adapter = LevelRecyclerAdapter(activity, myItemClickListener)
        recyclerView.adapter = adapter
    }

    fun loadLevelDescriptionMenu(activity: MainActivity, levelData: LevelData) {
        activity.updateMusic(R.raw.level_descr)
        activity.setContentView(R.layout.level_description)
        val backButton: Button = activity.findViewById(R.id.levelDescrBackButton)
        backButton.setOnClickListener {
            activity.updateMusic(R.raw.main_menu)
            loadLevelMenu(activity)
        }
        val startButton: Button = activity.findViewById(R.id.levelDescrStartButton)
        startButton.setOnClickListener {
            startBattle(activity,levelData)
        }
        var levelNameDescrTextView: TextView = activity.findViewById(R.id.levelNameDescrTextView)
        levelNameDescrTextView.text = levelData.name
        var levelDescrTextView: TextView = activity.findViewById(R.id.levelDescrTextView)
        levelDescrTextView.text = levelData.description
    }
}