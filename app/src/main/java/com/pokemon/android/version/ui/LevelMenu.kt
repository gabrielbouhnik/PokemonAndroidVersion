package com.pokemon.android.version.ui

import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R

class LevelMenu {
    fun loadBannerMenu(activity : MainActivity){
        activity.setContentView(R.layout.level_menu)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.levelRecyclerView)
        recyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val backButton : Button = activity.findViewById(R.id.levelMenuBackButton)
        backButton.setOnClickListener{
            activity.mainMenu.loadGameMenu(activity)
        }
        val adapter =  LevelRecyclerView(activity)
        recyclerView.adapter = adapter
    }
}