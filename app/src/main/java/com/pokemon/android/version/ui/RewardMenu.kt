package com.pokemon.android.version.ui

import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.model.level.LevelData

class RewardMenu {
    fun loadRewardMenu(activity: MainActivity, levelData : LevelData, firstTime : Boolean){
        activity.setContentView(R.layout.reward_layout)
        val backButton : Button = activity.findViewById(R.id.rewardsBackButton)
        backButton.setOnClickListener{
            activity.mainMenu.loadGameMenu(activity)
        }
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rewardRecyclerView)
        recyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter =  RewardRecyclerAdapter(activity, if (firstTime) levelData.rewards else ArrayList(levelData.rewards.filter{it.itemId != 0}))
        recyclerView.adapter = adapter
        levelData.rewards.forEach{
            if (it.itemId > 30){
                if (firstTime)
                    activity.trainer!!.addItem(it.itemId, it.quantity)
            }
            else if (it.itemId == 0) {
                activity.trainer!!.coins += if (firstTime) it.quantity else it.quantity/10
            }
            else
                activity.trainer!!.addItem(it.itemId, it.quantity)
        }
        SaveManager.save(activity)
    }
}