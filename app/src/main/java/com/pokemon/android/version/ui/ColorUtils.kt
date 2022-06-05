package com.pokemon.android.version.ui

import android.graphics.Color
import android.widget.Button
import com.pokemon.android.version.model.Type

class ColorUtils {
    companion object{
        fun setButtonColor(type : Type, button : Button){
            when(type){
                Type.GRASS -> {
                    button.setBackgroundColor(Color.GREEN)
                }
                Type.FIRE -> {
                    button.setBackgroundColor(Color.RED)
                }
                Type.WATER -> {
                    button.setBackgroundColor(Color.BLUE)
                }
                Type.ELECTRIC -> {
                    button.setBackgroundColor(Color.YELLOW)
                }
                Type.NORMAL -> {
                    button.setBackgroundColor(Color.WHITE)
                    button.setTextColor(Color.BLACK);
                }
                Type.FLYING -> {

                }
                Type.BUG -> {
                    button.setBackgroundColor(Color.GREEN)
                }
                Type.POISON -> {

                }
                Type.ROCK -> {

                }
                Type.GROUND -> {

                }
                Type.FIGHTING -> {

                }
                Type.PSYCHIC -> {

                }
                Type.GHOST -> {

                }
                Type.ICE -> {

                }
                Type.DRAGON -> {

                }
                Type.STEEL -> {
                    button.setBackgroundColor(Color.GRAY)
                }
                Type.DARK -> {
                    button.setBackgroundColor(Color.BLACK)
                }
                Type.FAIRY -> {

                }
            }
        }
    }
}