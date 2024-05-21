package com.pokemon.android.version

import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View.GONE
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Trainer
import com.pokemon.android.version.ui.LevelMenu
import com.pokemon.android.version.ui.MainMenu
import com.pokemon.android.version.ui.StarterSelection
import com.pokemon.android.version.utils.SpriteUtils
import java.io.InputStream
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    companion object {
        const val pokemonSpritesUrl: String =
            "https://www.serebii.net/swordshield/pokemon/"
        const val pokemonSVspritesUrl: String = "https://www.serebii.net/scarletviolet/pokemon/new/";
        const val pokemonSVshinySpriteUrl: String = "https://www.serebii.net/Shiny/SV/new/"
        const val megaPokemonSpritesUrl: String =
            "https://www.serebii.net/pokemongo/pokemon/"
        const val pokemonBackSpritesUrl: String =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/"
        const val itemSpriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/ca5a7886c10753144e6fae3b69d45a4d42a449b4/sprites/items/"
        const val armoredMewtwoUrl: String = "https://www.serebii.net/pokemongo/pokemon/150-armored.png"
        val idForSVsprites = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 23, 24, 25, 26, 27, 28, 35, 36, 37, 38, 39, 40, 43, 44,
            45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 69, 70, 71, 72, 73, 74, 75, 76, 79, 80, 81, 82, 84,
            85, 86, 87, 88, 89, 90, 91, 93, 94, 100, 101, 102, 103, 106, 107, 109, 110, 111, 112, 113, 116, 117, 123, 125, 126, 128, 129, 130, 131,
            132, 133, 134, 135, 136, 137, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157,
            158, 159, 160, 161, 162, 163, 164, 167, 168, 173, 181, 182, 183, 184, 186, 187, 188, 189, 190, 193, 195,
            196, 197, 198, 199, 203, 204, 205, 207, 209, 210, 211, 212, 214, 215, 216, 217, 218, 219, 228, 229, 230,
            231, 232, 233, 236, 237, 239, 240, 242, 243, 244, 245, 246, 247, 248, 272, 282, 285, 286, 296, 297, 308,
            330, 342, 350, 373, 376, 381, 398, 403, 404, 405, 418, 419, 424, 426, 429, 430, 435, 442, 448, 453, 454, 461, 462,
            464, 466, 467, 469, 470, 471, 472, 473, 474, 475, 476, 477, 488, 534, 553, 602, 603, 604, 607, 608, 609,
            619, 620, 625, 633, 634, 635, 639, 645, 646, 663, 700, 701, 715, 856, 857, 858, 878, 879, 885, 886, 887,
            901, 902, 919, 920, 981, 10058, 10059, 10088, 10089, 10100, 10101, 10157
        )
        val alolan_forms_id = listOf(10088, 10089, 10105)
        val hisui_forms_id = listOf(10058, 10059, 10100, 10101, 10157)
    }

    private var currentMusicId: Int? = null
    private var mediaPlayer: MediaPlayer? = null
    private var starterSelection: StarterSelection? = null
    var trainer: Trainer? = null
    var gameDataService: GameDataService = GameDataService()
    var mainMenu: MainMenu = MainMenu()
    var eliteMode: Boolean = false
    var hardMode: Boolean = false

    fun displayStarters() {
        val bulbasaur: Pokemon = gameDataService.generatePokemon(1, 5)
        val charmander: Pokemon = gameDataService.generatePokemon(4, 5)
        val squirtle: Pokemon = gameDataService.generatePokemon(7, 5)
        val sprite1: ImageView = findViewById(R.id.Sprite1View)
        val sprite2: ImageView = findViewById(R.id.Sprite2View)
        val sprite3: ImageView = findViewById(R.id.Sprite3View)
        sprite1.setOnClickListener {
            if (trainer!!.pokemons.size == 0)
                trainer!!.receivePokemon(bulbasaur)
            starterSelection!!.displayOakResponse(this, bulbasaur.data.name)
            sprite2.visibility = GONE
            sprite3.visibility = GONE
        }
        sprite2.setOnClickListener {
            if (trainer!!.pokemons.size == 0)
                trainer!!.receivePokemon(charmander)
            starterSelection!!.displayOakResponse(this, charmander.data.name)
            sprite1.visibility = GONE
            sprite3.visibility = GONE
        }
        sprite3.setOnClickListener {
            if (trainer!!.pokemons.size == 0)
                trainer!!.receivePokemon(squirtle)
            starterSelection!!.displayOakResponse(this, squirtle.data.name)
            sprite1.visibility = GONE
            sprite2.visibility = GONE
        }
        displayPokemon(1, false, sprite1)
        displayPokemon(4, false, sprite2)
        displayPokemon(7, false, sprite3)
    }

    fun displayPokemon(id: Int, shiny: Boolean, imageView: ImageView) {
        val idForUrl = SpriteUtils.getThreeDigitId(id)
        var formSuffix = ""
        if (hisui_forms_id.contains(id))
            formSuffix = "-h"
        if (alolan_forms_id.contains(id))
            formSuffix = "-a"
        if (!shiny) {
            when {
                id == -1 -> {
                    Glide.with(this)
                        .load(armoredMewtwoUrl)
                        .into(imageView)
                }
                idForSVsprites.contains(id) -> {
                    Glide.with(this)
                        .load("$pokemonSVspritesUrl$idForUrl$formSuffix.png")
                        .into(imageView)
                }
                else -> {
                    Glide.with(this)
                        .load("$pokemonSpritesUrl$idForUrl$formSuffix.png")
                        .into(imageView)
                }
            }
        } else {
            if (idForSVsprites.contains(id)) {
                Glide.with(this)
                    .load("$pokemonSVshinySpriteUrl$idForUrl$formSuffix.png")
                    .into(imageView)
            } else {
                Glide.with(this)
                    .load("https://www.serebii.net/Shiny/SWSH/$idForUrl$formSuffix.png")
                    .into(imageView)
            }

        }
    }

    fun displayMegaPokemon(id: Int, shiny: Boolean, imageView: ImageView) {
        val idForUrl = SpriteUtils.getThreeDigitId(id)
        var filename = "${idForUrl}-m.png"
        if (id == 6)
            filename = "${idForUrl}-mx.png"
        if (id == 150) {
            filename = "images/mega/150_front.png"
            val img: InputStream = this.assets.open(filename)
            imageView.setImageDrawable(Drawable.createFromStream(img, filename))
        } else {
            if (!shiny) {
                Glide.with(this)
                    .load(megaPokemonSpritesUrl + filename)
                    .into(imageView)
            } else {
                Glide.with(this)
                    .load("${pokemonSpritesUrl}shiny/" + filename)
                    .into(imageView)
            }
        }
    }

    fun displayPokemonBack(id: Int, shiny: Boolean, imageView: ImageView) {
        if (id < 650) {
            if (!shiny) {
                Glide.with(this)
                    .load("$pokemonBackSpritesUrl$id.png")
                    .into(imageView)
            } else {
                Glide.with(this)
                    .load("${pokemonBackSpritesUrl}shiny/$id.png")
                    .into(imageView)
            }
        } else {
            val filename = if (shiny) "images/pokemon/${id}_back_shiny.png" else "images/pokemon/${if (id > 10000) id - 10000 else id}_back.png"
            val img: InputStream = this.assets.open(filename)
            imageView.setImageDrawable(Drawable.createFromStream(img, filename))
        }
    }

    fun displayItem(itemName: String, imageView: ImageView) {
        Glide.with(this)
            .load("${itemSpriteUrl}/${itemName.lowercase().replace(' ', '-')}.png")
            .into(imageView)
    }

    private fun titleScreen() {
        val ids: List<Int> =
            listOf(3, 6, 9, 25, 26, 59, 76, 94, 95, 112, 115, 121, 123, 125, 126, 130, 131, 143, 149, 229, 248)
        val randomId = ids[Random.nextInt(ids.size)]
        val imageView: ImageView = findViewById(R.id.randomPokemonSpriteView)
        displayPokemon(randomId, false, imageView)
        val startButton: Button = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            mediaPlayer?.stop()
            trainer = SaveManager.loadSave(this)
            if (trainer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.oak)
                mediaPlayer?.start()
                starterSelection = StarterSelection()
                starterSelection?.startNewGame(this)
            } else {
                if (trainer!!.progression >= LevelMenu.ELITE_4_LAST_LEVEL_ID) {
                    gameDataService.increaseLevelDifficulty()
                }
                if (hardMode)
                    gameDataService.updateShopForHardMode()
                mainMenu.loadGameMenu(this)
            }
        }
    }

    fun playSoundEffect(id: Int) {
        MediaPlayer.create(this, id).start()
    }

    fun updateMusic(id: Int) {
        if (currentMusicId == id)
            return
        currentMusicId = id
        mediaPlayer?.stop()
        mediaPlayer = MediaPlayer.create(this, id)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    fun showCustomDialog(message: String) {
        val builder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(it)
        }
        builder.setMessage(message)
            .setTitle("Info")
            .setCancelable(false)
            .setPositiveButton("OK, got it!") { dialog, _ ->
                dialog.dismiss()
                if (message == getString(R.string.tutorial_game))
                    showHardModeDialog()
            }
            .create()
            .show()
    }

    private fun showHardModeDialog() {
        val builder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(it)
        }
        builder.setMessage(getString(R.string.hard_mode_details))
            .setTitle(getString(R.string.hard_mode_title))
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                hardMode = true
                findViewById<Button>(R.id.bannersButton).visibility = GONE
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun initGame() {
        currentMusicId = R.raw.title_theme
        mediaPlayer = MediaPlayer.create(this, currentMusicId!!)
        mediaPlayer?.start()
        gameDataService.loadGameData(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGame()
        setContentView(R.layout.title_screen)
        titleScreen()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.stop()
    }

    override fun onRestart() {
        super.onRestart()
        mediaPlayer = MediaPlayer.create(this, currentMusicId!!)
        mediaPlayer?.start()
    }
}