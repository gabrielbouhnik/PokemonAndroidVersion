package com.pokemon.android.version.utils

import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R

class MusicUtils {
    companion object{
        fun playMusic(activity: MainActivity, id : Int){
            when(id){
                2 -> activity.updateMusic(R.raw.trainer_battle)
                3 -> activity.updateMusic(R.raw.gym_leader)
                4 -> activity.updateMusic(R.raw.giovanni_theme)
                5 -> activity.updateMusic(R.raw.champion_theme)
            }
        }
    }
}