package com.pokemon.android.version

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View.GONE
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.ui.MainMenu
import com.pokemon.android.version.ui.StarterSelection
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    companion object {
        const val pokemonSpritesUrl: String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
        const val pokemonBackSpritesUrl: String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/"
        const val itemsSpritesUrl : String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/"
    }

    private var currentMusicId: Int? = null
    var trainer : com.pokemon.android.version.model.Trainer? = null
    var mediaPlayer: MediaPlayer? = null
    var gameDataService : GameDataService = GameDataService()
    var starterSelection : StarterSelection? = null
    var mainMenu : MainMenu = MainMenu()

    fun displayStarters(){
        val bulbasaur : Pokemon = gameDataService.generatePokemon(1,5)
        val charmander : Pokemon = gameDataService.generatePokemon(4,5)
        val squirtle : Pokemon = gameDataService.generatePokemon(7,5)
        val sprite1 : ImageView = findViewById(R.id.Sprite1View)
        val sprite2 : ImageView = findViewById(R.id.Sprite2View)
        val sprite3 : ImageView = findViewById(R.id.Sprite3View)
        sprite1.setOnClickListener{
            if (trainer!!.pokemons.size == 0)
                trainer!!.catchPokemon(bulbasaur)
            starterSelection!!.displayOakResponse(this, bulbasaur.data!!.name)
            sprite2.visibility = GONE
            sprite3.visibility = GONE
        }
        sprite2.setOnClickListener{
            if (trainer!!.pokemons.size == 0)
                trainer!!.catchPokemon(charmander)
            starterSelection!!.displayOakResponse(this, charmander.data!!.name)
            sprite1.visibility = GONE
            sprite3.visibility = GONE
        }
        sprite3.setOnClickListener{
            if (trainer!!.pokemons.size == 0)
                trainer!!.catchPokemon(squirtle)
            starterSelection!!.displayOakResponse(this, squirtle.data!!.name)
            sprite1.visibility = GONE
            sprite2.visibility = GONE
        }
        Glide.with(this)
            .load(pokemonSpritesUrl + "1.png")
            .into(sprite1)
        Glide.with(this)
            .load(pokemonSpritesUrl + "4.png")
            .into(sprite2)
        Glide.with(this)
            .load(pokemonSpritesUrl + "7.png")
            .into(sprite3)
    }

    fun displayPokemon(id : Int, imageView: ImageView){
        Glide.with(this)
            .load("$pokemonSpritesUrl$id.png")
            .into(imageView)
    }

    fun displayPokemonBack(id : Int, imageView: ImageView){
        Glide.with(this)
            .load("$pokemonBackSpritesUrl$id.png")
            .into(imageView)
    }

    fun displayRandomPokemon(){
        val random = Random.nextInt(1..251)
        val imageView : ImageView = findViewById(R.id.randomPokemonSpriteView)
        Glide.with(this)
            .load("$pokemonSpritesUrl$random.png")
            .into(imageView)
        val startButton : Button = findViewById(R.id.startButton)
        startButton.setOnClickListener{
            mediaPlayer?.stop()
            trainer = SaveManager.loadSave(this)
            if (trainer == null){
                mediaPlayer = MediaPlayer.create(this,R.raw.oak)
                mediaPlayer?.start()
                starterSelection = StarterSelection()
                starterSelection?.startNewGame(this)
            }
            else {
                mainMenu.loadGameMenu(this)
            }
        }
    }

    fun playSoundEffect(id : Int){
        MediaPlayer.create(this,id).start()
    }

    fun updateMusic(id : Int){
        if (currentMusicId == id)
            return
        currentMusicId = id
        mediaPlayer?.stop()
        mediaPlayer = MediaPlayer.create(this,id)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    fun initGame(){
        mediaPlayer = MediaPlayer.create(this,R.raw.title_theme)
        mediaPlayer?.start()
        gameDataService.loadGameData(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGame()
        setContentView(R.layout.title_screen)
        displayRandomPokemon()
    }
}