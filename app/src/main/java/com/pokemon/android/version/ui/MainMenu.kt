package com.pokemon.android.version.ui

import android.graphics.drawable.Drawable
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.model.Gender
import com.pokemon.android.version.model.item.ItemQuantity
import com.pokemon.android.version.utils.HealUtils
import com.pokemon.android.version.utils.ItemUtils
import java.io.InputStream

class MainMenu {
    companion object {
        const val BOY_SPRITE = "images/Spr_HGSS_Ethan.png"
        const val GIRL_SPRITE = "images/Spr_HGSS_Lyra.png"
    }

    var pokemonMenu: PokemonMenu = PokemonMenu()
    var bannerMenu: BannerMenu = BannerMenu()
    var levelMenu: LevelMenu = LevelMenu()
    var itemMenu: ItemMenu = ItemMenu()
    var battleFrontierMenu = BattleFrontierMenu()
    var pokedexMenu = PokedexMenu()

    fun loadGameMenu(activity: MainActivity) {
        activity.setContentView(R.layout.main_menu)
        if (activity.eliteMode) {
            activity.updateMusic(R.raw.elite4_music)
            val bannerTextView: TextView = activity.findViewById(R.id.MysteryGiftTextView)
            bannerTextView.visibility = GONE
        } else
            activity.updateMusic(R.raw.main_menu)
        val pokemonsButton: Button = activity.findViewById(R.id.pokemonsButton)
        pokemonsButton.setOnClickListener {
            if (activity.eliteMode)
                pokemonMenu.loadPokemonMenuElite(activity)
            else
                pokemonMenu.loadPokemonMenu(activity)
        }
        val bannersButton: Button = activity.findViewById(R.id.bannersButton)
        if (activity.eliteMode) {
            bannersButton.visibility = GONE
        } else {
            bannersButton.setOnClickListener {
                bannerMenu.loadBannerMenu(activity)
            }
        }
        val adventureButton: Button = activity.findViewById(R.id.adventureButton)
        if (activity.eliteMode) {
            adventureButton.text = activity.getString(R.string.pokemon_league)
            val adventureDescriptionTextView: TextView = activity.findViewById(R.id.adventureDescriptionTextView)
            adventureDescriptionTextView.text = activity.getString(R.string.face_elite)
        }
        adventureButton.setOnClickListener {
            if (activity.eliteMode)
                levelMenu.loadEliteLevels(activity)
            else
                levelMenu.loadLevelMenu(activity)
        }
        val itemsButton: Button = activity.findViewById(R.id.itemsButton)
        itemsButton.setOnClickListener {
            itemMenu.loadItemMenu(activity, null)
        }
        if (!activity.eliteMode && HealUtils.canUseDailyHeal(activity.trainer!!)) {
            val dailyHealButton: Button = activity.findViewById(R.id.dailyHealButton)
            dailyHealButton.visibility = VISIBLE
            dailyHealButton.setOnClickListener {
                activity.playSoundEffect(R.raw.daily_heal_sound_effect)
                HealUtils.dailyHeal(activity.trainer!!)
                dailyHealButton.visibility = GONE
            }
        }
        val trainerCardButton: Button = activity.findViewById(R.id.trainerCardButton)
        trainerCardButton.setOnClickListener {
            loadTrainerCardMenu(activity)
            SaveManager.save(activity)
        }
        val eliteButton: Button = activity.findViewById(R.id.eliteButton)
        if (!activity.eliteMode && activity.trainer!!.progression >= LevelMenu.ELITE_4_FIRST_LEVEL_ID) {
            eliteButton.visibility = VISIBLE
            eliteButton.setOnClickListener {
                activity.eliteMode = true
                loadGameMenu(activity)
            }
        }
        val battleFrontierButton: Button = activity.findViewById(R.id.battleFrontierButton)
        if (!activity.eliteMode && activity.trainer!!.progression > 51) {
            battleFrontierButton.setOnClickListener {
                battleFrontierMenu.loadMenu(activity)
            }
        } else {
            battleFrontierButton.visibility = GONE
            val battleFrontierTextView: TextView = activity.findViewById(R.id.BattleFrontierTextView)
            battleFrontierTextView.visibility = GONE
        }
        val pokedexButton: Button = activity.findViewById(R.id.pokedexButton)
        pokedexButton.setOnClickListener {
                pokedexMenu.loadPokedexMenu(activity)
        }
    }

    private fun loadTrainerCardMenu(activity: MainActivity) {
        activity.setContentView(R.layout.trainer_card)
        val backButton: Button = activity.findViewById(R.id.trainerCardMenuBackButton)
        backButton.setOnClickListener {
            loadGameMenu(activity)
        }
        val achievementsButton: TextView = activity.findViewById(R.id.achievementsButton)
        if (activity.trainer!!.progression < LevelMenu.ELITE_4_LAST_LEVEL_ID)
            achievementsButton.visibility = GONE
        else {
            achievementsButton.setOnClickListener {
                loadAchievementsMenu(activity)
            }
        }
        val trainerSpriteTextView: ImageView = activity.findViewById(R.id.trainerSpriteTextView)
        val filename = if (activity.trainer!!.gender == Gender.MALE) BOY_SPRITE else GIRL_SPRITE
        val img: InputStream = activity.assets.open(filename)
        trainerSpriteTextView.setImageDrawable(Drawable.createFromStream(img, filename))
        val nameTextView: TextView = activity.findViewById(R.id.trainerNameTextView)
        nameTextView.text = activity.trainer!!.name
        val androCoinTextView: TextView = activity.findViewById(R.id.androCoinTextView)
        androCoinTextView.text = activity.getString(R.string.andro_coins, activity.trainer!!.coins)
        val maxLevelTextView: TextView = activity.findViewById(R.id.maxLevelTextView)
        maxLevelTextView.text = activity.getString(R.string.exp_limit, activity.trainer!!.getMaxLevel())
        val recyclerView = activity.findViewById<RecyclerView>(R.id.badgesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter = ItemRecyclerAdapter(
            activity,
            ArrayList(
                ItemQuantity.createItemQuantityFromHashMap(activity.trainer!!.items)
                    .filter { ItemUtils.isBadge(it.itemId) })
        ) {}
        recyclerView.adapter = adapter
    }

    private fun loadAchievementsMenu(activity: MainActivity) {
        activity.setContentView(R.layout.achievements_menu)
        val backButton: Button = activity.findViewById(R.id.achievementsMenuBackButton)
        backButton.setOnClickListener {
            loadTrainerCardMenu(activity)
        }
        val recyclerView = activity.findViewById<RecyclerView>(R.id.achievementsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter = AchievementRecyclerAdapter(activity, activity.gameDataService.achievements)
        recyclerView.adapter = adapter
    }
}