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
                    button.setBackgroundColor(Color.parseColor("#87CEFA"))
                }
                Type.BUG -> {
                    button.setBackgroundColor(Color.GREEN)
                }
                Type.POISON -> {
                    button.setBackgroundColor(Color.parseColor("#8B008B"))
                }
                Type.ROCK -> {
                    button.setBackgroundColor(Color.parseColor("#B6A136"))
                }
                Type.GROUND -> {
                    button.setBackgroundColor(Color.parseColor("#D2B48C"))
                }
                Type.FIGHTING -> {
                    button.setBackgroundColor(Color.parseColor("#B22222"))
                }
                Type.PSYCHIC -> {
                    button.setBackgroundColor(Color.parseColor("#F95587"))
                }
                Type.GHOST -> {
                    button.setBackgroundColor(Color.parseColor("#9932CC"))
                }
                Type.ICE -> {
                    button.setBackgroundColor(Color.parseColor("#E0FFFF"))
                }
                Type.DRAGON -> {
                    button.setBackgroundColor(Color.parseColor("#00008B"))
                }
                Type.STEEL -> {
                    button.setBackgroundColor(Color.GRAY)
                }
                Type.DARK -> {
                    button.setBackgroundColor(Color.BLACK)
                }
                Type.FAIRY -> {
                    button.setBackgroundColor(Color.parseColor("#FFB6C1"))
                }
            }
        }
    }
}