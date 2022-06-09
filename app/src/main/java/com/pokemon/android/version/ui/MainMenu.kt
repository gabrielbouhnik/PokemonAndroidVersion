package com.pokemon.android.version.ui

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.model.Gender
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.item.ItemQuantity
import com.pokemon.android.version.utils.DailyHeal
import java.io.InputStream

class MainMenu {
    companion object {
        const val BOY_SPRITE = "images/Spr_HGSS_Ethan.png"
        const val GIRL_SPRITE = "images/Spr_HGSS_Lyra.png"
    }

    var pokemonMenu : PokemonMenu = PokemonMenu()
    var bannerMenu : BannerMenu = BannerMenu()
    var levelMenu : LevelMenu = LevelMenu()
    var itemMenu : ItemMenu = ItemMenu()

    fun loadGameMenu(activity : MainActivity){
        activity.setContentView(R.layout.main_menu)
        activity.updateMusic(R.raw.main_menu)
        var pokemonsButton : Button = activity.findViewById(R.id.pokemonsButton)
        pokemonsButton.setOnClickListener {
            pokemonMenu.loadPokemonMenu(activity)
        }
        var bannersButton : Button = activity.findViewById(R.id.bannersButton)
        bannersButton.setOnClickListener {
            bannerMenu.loadBannerMenu(activity)
        }
        var adventureButton : Button = activity.findViewById(R.id.adventureButton)
        adventureButton.setOnClickListener {
            levelMenu.loadLevelMenu(activity)
        }
        var itemsButton : Button = activity.findViewById(R.id.itemsButton)
        itemsButton.setOnClickListener {
            itemMenu.loadItemMenu(activity, null)
        }
        if (DailyHeal.canUseDailyHeal(activity.trainer!!)) {
            var dailyHealButton: Button = activity.findViewById(R.id.dailyHealButton)
            dailyHealButton.visibility = VISIBLE
            dailyHealButton.setOnClickListener {
                DailyHeal.heal(activity.trainer!!)
                dailyHealButton.visibility = GONE
            }
        }
        var trainerCardButton : Button = activity.findViewById(R.id.trainerCardButton)
        trainerCardButton.setOnClickListener {
            loadTrainerCardMenu(activity)
            SaveManager.save(activity)
        }
    }

    fun loadTrainerCardMenu(activity : MainActivity){
        activity.setContentView(R.layout.trainer_card)
        val backButton : Button = activity.findViewById(R.id.trainerCardMenuBackButton)
        backButton.setOnClickListener{
            loadGameMenu(activity)
        }
        var trainerSpriteTextView : ImageView = activity.findViewById(R.id.trainerSpriteTextView)
        var filename = if (activity.trainer!!.gender == Gender.MALE) BOY_SPRITE else GIRL_SPRITE
        val img : InputStream = activity.assets.open(filename)
        trainerSpriteTextView.setImageDrawable(Drawable.createFromStream(img, filename))
        var nameTextView : TextView = activity.findViewById(R.id.trainerNameTextView)
        nameTextView.text = activity.trainer!!.name
        var androCoinTextView : TextView = activity.findViewById(R.id.androCoinTextView)
        androCoinTextView.text = activity.trainer!!.coins.toString() + " AndroCoins"
        val recyclerView = activity.findViewById<RecyclerView>(R.id.badgesRecyclerView)
        recyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter =  ItemRecyclerAdapter(activity, ArrayList(ItemQuantity.createItemQuantityFromHashMap(activity.trainer!!.items).filter{it.itemId > 30}),View.OnClickListener{})
        recyclerView.adapter = adapter
    }
}