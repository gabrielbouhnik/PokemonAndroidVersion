package com.pokemon.android.version.ui

import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R

class BannerMenu {
    lateinit var coinsTextView : TextView

    fun loadBannerMenu(activity : MainActivity){
        activity.setContentView(R.layout.banners_menu)
        coinsTextView = activity.findViewById(R.id.coinBannerTextView)
        coinsTextView.text = "${activity.trainer!!.coins} AndroCoins"
        val recyclerView = activity.findViewById<RecyclerView>(R.id.bannerRecyclerView)
        recyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val backButton : Button = activity.findViewById(R.id.bannerMenuBackButton)
        backButton.setOnClickListener{
            activity.mainMenu.loadGameMenu(activity)
        }
        val adapter =  BannerRecyclerView(activity)
        recyclerView.adapter = adapter
    }
}