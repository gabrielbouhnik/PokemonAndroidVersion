package com.pokemon.android.version

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlin.random.Random
import kotlin.random.nextInt
import lombok.Setter

class MainActivity : AppCompatActivity() {
    companion object {
        const val spritesUrl: String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
    }
    @Setter
    var character : com.pokemon.android.version.model.Character? = null
    var mediaPlayer: MediaPlayer? = null
    var gameDataService : GameDataService = GameDataService()
    var starterSelection : StarterSelection? = null

    fun displayStarters(){
        val sprite1 : ImageView = findViewById(R.id.Sprite1View)
        sprite1.setOnClickListener{
        }
        val sprite2 : ImageView = findViewById(R.id.Sprite2View)
        sprite2.setOnClickListener{
        }
        val sprite3 : ImageView = findViewById(R.id.Sprite3View)
        sprite3.setOnClickListener{
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
            character = LoadSaveFile.loadSave()
            if (character == null){
                mediaPlayer = MediaPlayer.create(this,R.raw.oak)
                mediaPlayer?.start()
                starterSelection = StarterSelection()
                starterSelection?.startNewGame(this)
            }
        }
    }

    fun initGame(){
        mediaPlayer = MediaPlayer.create(this,R.raw.title_theme)
        mediaPlayer?.start()
        gameDataService.loadGameData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGame()
        setContentView(R.layout.title_screen)
        displayRandomPokemon()
    }
}