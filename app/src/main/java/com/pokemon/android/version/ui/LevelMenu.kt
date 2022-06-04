package com.pokemon.android.version.ui

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.level.LevelData
import com.pokemon.android.version.model.level.WildBattleLevelData

class LevelMenu {
    var battleUI : BattleUI = BattleUI()

    fun loadLevelMenu(activity : MainActivity){
        activity.setContentView(R.layout.level_menu)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.levelRecyclerView)
        recyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val backButton : Button = activity.findViewById(R.id.levelMenuBackButton)
        backButton.setOnClickListener{
            activity.mainMenu.loadGameMenu(activity)
        }
        val myItemClickListener = View.OnClickListener {
            val position = it.tag as Int
            if (activity.trainer!!.canStillBattle() && activity.trainer!!.progression >= activity.gameDataService.levels[position].id){
                if (activity.gameDataService.levels[position] is WildBattleLevelData)
                    battleUI.startWildBattle(activity, activity.gameDataService.levels[position] as WildBattleLevelData)
            }
        }
        val adapter =  LevelRecyclerAdapter(activity,myItemClickListener)
        recyclerView.adapter = adapter
    }

    fun loadLevelDescriptionMenu(activity : MainActivity, level : LevelData){
        activity.setContentView(R.layout.level_description)
        val backButton : Button = activity.findViewById(R.id.levelDescrBackButton)
        backButton.setOnClickListener{
            loadLevelMenu(activity)
        }
        val startButton : Button = activity.findViewById(R.id.levelDescrStartButton)
        startButton.setOnClickListener {

        }
        var levelNameDescrTextView : TextView = activity.findViewById(R.id.levelNameDescrTextView)
        levelNameDescrTextView.text = level.name
        var levelDescrTextView : TextView = activity.findViewById(R.id.levelDescrTextView)
        levelDescrTextView.text = level.description
    }
}