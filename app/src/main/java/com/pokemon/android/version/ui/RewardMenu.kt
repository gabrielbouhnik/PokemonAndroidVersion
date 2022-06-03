package com.pokemon.android.version.ui

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.model.level.Reward

class RewardMenu {
    fun loadRewardMenu(activity: MainActivity, rewards : ArrayList<Reward> ){
        activity.updateMusic(R.raw.victory_theme)
        activity.setContentView(R.layout.reward_layout)
        val backButton : Button = activity.findViewById(R.id.rewardsBackButton)
        backButton.setOnClickListener{
            activity.mainMenu.loadGameMenu(activity)
        }
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rewardRecyclerView)
        recyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter =  RewardRecyclerAdapter(activity, rewards)
        recyclerView.adapter = adapter
        rewards.forEach{
            if (it.itemId == 0)
                activity.trainer!!.coins += it.quantity
            else
                activity.trainer!!.addItem(it.itemId, it.quantity)
        }
        SaveManager.save(activity)
    }
}