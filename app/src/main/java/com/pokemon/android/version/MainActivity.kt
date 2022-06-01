package com.pokemon.android.version

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.pokemon.android.version.model.Pokemon
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    companion object {
        const val spritesUrl: String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
    }
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
        sprite1.setOnClickListener{
            trainer!!.pokemons.add(bulbasaur)
            starterSelection!!.displayOakResponse(this, bulbasaur.data!!.name)
        }
        val sprite2 : ImageView = findViewById(R.id.Sprite2View)
        sprite2.setOnClickListener{
            trainer!!.pokemons.add(charmander)
            starterSelection!!.displayOakResponse(this, charmander.data!!.name)
        }
        val sprite3 : ImageView = findViewById(R.id.Sprite3View)
        sprite3.setOnClickListener{
            trainer!!.pokemons.add(squirtle)
            starterSelection!!.displayOakResponse(this, squirtle.data!!.name)
        }
        Glide.with(this)
            .load(spritesUrl + "1.png")
            .into(sprite1)
        Glide.with(this)
            .load(spritesUrl + "4.png")
            .into(sprite2)
        Glide.with(this)
            .load(spritesUrl + "7.png")
            .into(sprite3)
    }


    fun displayRandomPokemon(){
        val random = Random.nextInt(1..225)
        val imageView : ImageView = findViewById(R.id.randomPokemonSpriteView)
        Glide.with(this)
            .load("$spritesUrl$random.png")
            .into(imageView)
        val startButton : Button = findViewById(R.id.startButton)
        startButton.setOnClickListener{
            mediaPlayer?.stop()
            trainer = LoadSaveFile.loadSave()
            if (trainer == null){
                mediaPlayer = MediaPlayer.create(this,R.raw.oak)
                mediaPlayer?.start()
                starterSelection = StarterSelection()
                starterSelection?.startNewGame(this)
            }
            else {
                mediaPlayer = MediaPlayer.create(this,R.raw.main_menu)
                mediaPlayer?.start()
                mainMenu.loadGameMenu(this)
            }
        }
    }

    fun updateMusic(id : Int){
        mediaPlayer?.stop()
        mediaPlayer = MediaPlayer.create(this,id)
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