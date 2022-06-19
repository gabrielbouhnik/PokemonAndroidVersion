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
import com.pokemon.android.version.utils.ItemUtils
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
        if (activity.eliteMode) {
            activity.updateMusic(R.raw.elite4_music)
            val bannerTextView : TextView = activity.findViewById(R.id.MysteryGiftTextView)
            bannerTextView.visibility = GONE
        }
        else
            activity.updateMusic(R.raw.main_menu)
        val pokemonsButton : Button = activity.findViewById(R.id.pokemonsButton)
        pokemonsButton.setOnClickListener {
            if (activity.eliteMode)
                pokemonMenu.loadPokemonMenuElite(activity)
            else
                pokemonMenu.loadPokemonMenu(activity)
        }
        val bannersButton : Button = activity.findViewById(R.id.bannersButton)
        if (activity.eliteMode) {
            bannersButton.visibility = GONE
        }
        else {
            bannersButton.setOnClickListener {
                bannerMenu.loadBannerMenu(activity)
            }
        }
        val adventureButton : Button = activity.findViewById(R.id.adventureButton)
        if (activity.eliteMode) {
            adventureButton.text = "POKEMON LEAGUE"
            val adventureDescrTextView : TextView = activity.findViewById(R.id.adventureDescriptionTextView)
            adventureDescrTextView.text = "Face the elite 4 to become the champion."
        }
        adventureButton.setOnClickListener {
            if (activity.eliteMode)
                levelMenu.loadEliteLevels(activity)
            else
                levelMenu.loadLevelMenu(activity)
        }
        val itemsButton : Button = activity.findViewById(R.id.itemsButton)
        itemsButton.setOnClickListener {
            itemMenu.loadItemMenu(activity, null)
        }
        if (!!activity.eliteMode && DailyHeal.canUseDailyHeal(activity.trainer!!)) {
            val dailyHealButton: Button = activity.findViewById(R.id.dailyHealButton)
            dailyHealButton.visibility = VISIBLE
            dailyHealButton.setOnClickListener {
                activity.playSoundEffect(R.raw.daily_heal_sound_effect)
                DailyHeal.heal(activity.trainer!!)
                dailyHealButton.visibility = GONE
            }
        }
        val trainerCardButton : Button = activity.findViewById(R.id.trainerCardButton)
        trainerCardButton.setOnClickListener {
            loadTrainerCardMenu(activity)
            SaveManager.save(activity)
        }
        val eliteButton : Button = activity.findViewById(R.id.eliteButton)
        if (!activity.eliteMode && activity.trainer!!.progression >= 63) {
            eliteButton.visibility = VISIBLE
            eliteButton.setOnClickListener {
                activity.eliteMode = true
                loadGameMenu(activity)
            }
        }
    }

    fun loadTrainerCardMenu(activity : MainActivity){
        activity.setContentView(R.layout.trainer_card)
        val backButton : Button = activity.findViewById(R.id.trainerCardMenuBackButton)
        backButton.setOnClickListener{
            loadGameMenu(activity)
        }
        val trainerSpriteTextView : ImageView = activity.findViewById(R.id.trainerSpriteTextView)
        val filename = if (activity.trainer!!.gender == Gender.MALE) BOY_SPRITE else GIRL_SPRITE
        val img : InputStream = activity.assets.open(filename)
        trainerSpriteTextView.setImageDrawable(Drawable.createFromStream(img, filename))
        val nameTextView : TextView = activity.findViewById(R.id.trainerNameTextView)
        nameTextView.text = activity.trainer!!.name
        val androCoinTextView : TextView = activity.findViewById(R.id.androCoinTextView)
        androCoinTextView.text = activity.trainer!!.coins.toString() + " AndroCoins"
        val maxLevelTextView : TextView = activity.findViewById(R.id.maxLevelTextView)
        maxLevelTextView.text = "Your Pokemon will gain experience up to level " + activity.trainer!!.getMaxLevel()
        val recyclerView = activity.findViewById<RecyclerView>(R.id.badgesRecyclerView)
        recyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter =  ItemRecyclerAdapter(activity, ArrayList(ItemQuantity.createItemQuantityFromHashMap(activity.trainer!!.items).filter{ItemUtils.isBadge(it.itemId)}),View.OnClickListener{})
        recyclerView.adapter = adapter
    }
}