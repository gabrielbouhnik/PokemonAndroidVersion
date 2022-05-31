package com.pokemon.android.version

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    var spritesUrl: String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
    var mediaPlayer: MediaPlayer? = null

    fun displayRandomPokemon(){
        val random = Random.nextInt(1..225)
        val imageView : ImageView = findViewById(R.id.randomPokemonSpriteView)
        Glide.with(this)
            .load("$spritesUrl$random.png")
            .into(imageView)
    }

    fun initGame(){
        mediaPlayer = MediaPlayer.create(this,R.raw.title_theme)
        mediaPlayer?.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGame()
        setContentView(R.layout.title_screen)
        displayRandomPokemon()
    }
}