package com.pokemon.android.version.ui

import android.os.Build
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.utils.DateUtils
import java.time.DayOfWeek
import java.util.*

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
        val calendar: Calendar = Calendar.getInstance()
        val day: DayOfWeek = DateUtils.getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
        val adapter =  BannerRecyclerAdapter(activity, ArrayList(activity.gameDataService.banners.filter{it.days.contains(day)}))
        recyclerView.adapter = adapter
    }
}