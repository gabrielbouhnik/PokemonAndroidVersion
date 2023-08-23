package com.pokemon.android.version

import android.app.AlertDialog
import android.content.DialogInterface
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
        const val armoredMewtwoUrl: String = "https://www.serebii.net/pokemongo/pokemon/150-armored.png"
        val idForSVsprites = listOf(6, 25, 26, 39, 40, 48, 49, 52, 53, 54, 55, 58, 59, 79, 80, 81, 82,
            90, 91, 93, 94, 100, 101, 113, 123, 128, 129, 130, 133, 134, 135, 136, 144, 145, 146, 147, 148,
            149, 150, 151, 155, 156, 157, 181, 183, 184, 187, 188, 189, 195, 196, 197, 198, 199, 203, 204,
            205, 211, 212, 214, 215, 216, 217, 228, 229, 231, 232, 242, 246, 247, 248, 282, 285, 286, 296,
            297, 308, 373, 398, 405, 426, 429, 430, 442, 448, 453, 454, 461, 462, 470, 475, 488, 633, 634,
            635, 700, 715, 856, 857, 858, 878, 879, 885, 886,887
        )
    }

    private var currentMusicId: Int? = null
    private var mediaPlayer: MediaPlayer? = null
    private var starterSelection: StarterSelection? = null
    var trainer: Trainer? = null
    var gameDataService: GameDataService = GameDataService()
    var mainMenu: MainMenu = MainMenu()
    var eliteMode: Boolean = false

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
        if (!shiny) {
            when {
                id == -1 -> {
                    Glide.with(this)
                        .load(armoredMewtwoUrl)
                        .into(imageView)
                }
                idForSVsprites.contains(id) -> {
                    Glide.with(this)
                        .load("$pokemonSVspritesUrl$idForUrl.png")
                        .into(imageView)
                }
                else -> {
                    Glide.with(this)
                        .load("$pokemonSpritesUrl$idForUrl.png")
                        .into(imageView)
                }
            }
        } else {
            if (idForSVsprites.contains(id)) {
                Glide.with(this)
                    .load("$pokemonSVshinySpriteUrl$idForUrl.png")
                    .into(imageView)
            } else {
                Glide.with(this)
                    .load("https://www.serebii.net/Shiny/SWSH/$idForUrl.png")
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
            val filename = if (shiny) "images/pokemon/${id}_back_shiny.png" else "images/pokemon/${id}_back.png"
            val img: InputStream = this.assets.open(filename)
            imageView.setImageDrawable(Drawable.createFromStream(img, filename))
        }
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
                    gameDataService.updateEliteMode()
                    gameDataService.updateGymLeaderExp()
                }
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