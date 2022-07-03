package com.pokemon.android.version.ui

import android.graphics.Color
import com.pokemon.android.version.model.Type

class ColorUtils {
    companion object{
        fun getColorByType(type : Type) : Int{
            when(type){
                Type.GRASS -> {
                    return Color.parseColor("#7AC74C")
                }
                Type.FIRE -> {
                    return Color.parseColor("#EE8130")
                }
                Type.WATER -> {
                    return Color.parseColor("#6390F0")
                }
                Type.ELECTRIC -> {
                    return Color.parseColor("#F7D02C")
                }
                Type.NORMAL -> {
                    return Color.parseColor("#A8A77A")
                }
                Type.FLYING -> {
                    return Color.parseColor("#A98FF3")
                }
                Type.BUG -> {
                    return Color.parseColor("#A6B91A")
                }
                Type.POISON -> {
                    return Color.parseColor("#8B008B")
                }
                Type.ROCK -> {
                    return Color.parseColor("#B6A136")
                }
                Type.GROUND -> {
                    return Color.parseColor("#D2B48C")
                }
                Type.FIGHTING -> {
                    return Color.parseColor("#B22222")
                }
                Type.PSYCHIC -> {
                    return Color.parseColor("#F95587")
                }
                Type.GHOST -> {
                    return Color.parseColor("#9932CC")
                }
                Type.ICE -> {
                    return Color.parseColor("#E0FFFF")
                }
                Type.DRAGON -> {
                    return Color.parseColor("#00008B")
                }
                Type.STEEL -> {
                    return Color.parseColor("#B7B7CE")
                }
                Type.DARK -> {
                    return Color.parseColor("#705746")
                }
                Type.FAIRY -> {
                    return Color.parseColor("#FFB6C1")
                }
            }
            return Color.BLACK
        }
    }
}