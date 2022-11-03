package com.pokemon.android.version

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
import kotlin.random.Random
import kotlin.random.nextInt

import androidx.fragment.app.Fragment
import com.pokemon.android.version.databinding.MenuWithNavbarBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val pokemonSpritesUrl: String =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
        const val pokemonBackSpritesUrl: String =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/"
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
        displayPokemon(1, sprite1)
        displayPokemon(4, sprite2)
        displayPokemon(7, sprite3)
    }

    fun displayPokemon(id: Int, imageView: ImageView) {
        Glide.with(this)
            .load("$pokemonSpritesUrl$id.png")
            .into(imageView)
    }

    fun displayPokemonBack(id: Int, imageView: ImageView) {
        Glide.with(this)
            .load("$pokemonBackSpritesUrl$id.png")
            .into(imageView)
    }

    private fun titleScreen() {
        val ids : List<Int> = listOf(3,6,9,25,26,59,76,94,95,112,115,121,123,125,126,130,131,143,149,229,248)
        val randomId = ids[Random.nextInt(ids.size)]
        val imageView: ImageView = findViewById(R.id.randomPokemonSpriteView)
        Glide.with(this)
            .load("$pokemonSpritesUrl$randomId.png")
            .into(imageView)
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