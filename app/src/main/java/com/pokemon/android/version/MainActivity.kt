package com.pokemon.android.version

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    var spritesUrl: String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
    var mediaPlayer: MediaPlayer? = null
    var character : com.pokemon.android.version.model.Character? = null
    var gameDataService : GameDataService = GameDataService()

    fun startNewGame(){
        mediaPlayer = MediaPlayer.create(this,R.raw.oak)
        mediaPlayer?.start()
        setContentView(R.layout.starter_selection)
        val nextButton : Button = findViewById(R.id.nextButton)
        val oakTextView : TextView = findViewById(R.id.oakTextView)
        nextButton.setOnClickListener{
            val sprite1 : ImageView = findViewById(R.id.Sprite1View)
            val sprite2 : ImageView = findViewById(R.id.Sprite2View)
            val sprite3 : ImageView = findViewById(R.id.Sprite3View)
            Glide.with(this)
                .load("$spritesUrl$1.png")
                .into(sprite1)
            Glide.with(this)
                .load("$spritesUrl$2.png")
                .into(sprite2)
            Glide.with(this)
                .load("$spritesUrl$3.png")
                .into(sprite3)
            oakTextView.text = "Now you can introduce yourself to me"
        }
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
                startNewGame()
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