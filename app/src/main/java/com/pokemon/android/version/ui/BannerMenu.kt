package com.pokemon.android.version.ui

import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.banner.Banner
import com.pokemon.android.version.model.banner.ItemBanner
import com.pokemon.android.version.model.banner.PokemonBanner
import com.pokemon.android.version.model.banner.Summonable
import com.pokemon.android.version.utils.DateUtils
import java.time.DayOfWeek
import java.util.*

class BannerMenu {
    lateinit var coinsTextView: TextView

    fun loadBannerMenu(activity: MainActivity) {
        activity.setContentView(R.layout.banners_menu)
        coinsTextView = activity.findViewById(R.id.coinBannerTextView)
        coinsTextView.text = activity.getString(R.string.andro_coins, activity.trainer!!.coins)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.bannerRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val backButton: Button = activity.findViewById(R.id.bannerMenuBackButton)
        backButton.setOnClickListener {
            activity.mainMenu.loadGameMenu(activity)
        }
        val calendar: Calendar = Calendar.getInstance()
        val day: DayOfWeek = DateUtils.getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
        val banners = if (activity.trainer!!.progression < LevelMenu.OTHER_BANNER_LEVEL) activity.gameDataService.banners.filter { it.description.contains("Hyper Ball") || it.description.contains("Pikachu")} else activity.gameDataService.banners.filter { it.days.contains(day) }
        if (activity.trainer!!.progression < LevelMenu.ELITE_4_FIRST_LEVEL_ID - 1)
            recyclerView.adapter =
                BannerRecyclerAdapter(activity, (banners.filter { !it.image.contains("tm_banner") }).toMutableList())
        else
            recyclerView.adapter = BannerRecyclerAdapter(activity, banners)
    }

    fun loadSummonResultScreen(activity: MainActivity, banner: Banner, s: Summonable) {
        activity.setContentView(R.layout.summon_layout)
        val coinsSummonAgainTextView : TextView = activity.findViewById(R.id.coinSummonResultTextView)
        coinsSummonAgainTextView.text = activity.getString(R.string.andro_coins, activity.trainer!!.coins)
        val backButton: Button = activity.findViewById(R.id.summonResultBackButton)
        backButton.setOnClickListener {
            loadBannerMenu(activity)
        }
        val summonAgainButton: Button = activity.findViewById(R.id.summonAgainButton)
        if (activity.trainer!!.coins >= banner.cost)
        summonAgainButton.setOnClickListener {
            val newSummonResult = banner.summon(activity)
            if (newSummonResult != null) {
                activity.mainMenu.bannerMenu.coinsTextView.text = "${activity.trainer!!.coins} AndroCoins"
                activity.mainMenu.bannerMenu.loadSummonResultScreen(activity, banner, newSummonResult)
                activity.playSoundEffect(R.raw.item_sound_effect)
            }
        }
        val resultTextView: TextView = activity.findViewById(R.id.summonResultTextView)
        if (s is PokemonBanner) {
            resultTextView.text = activity.getString(R.string.summon_pokemon, s.name)
        } else {
            resultTextView.text =
                activity.getString(R.string.summon_item, activity.gameDataService.items[(s as ItemBanner).id].name)
        }
    }
}