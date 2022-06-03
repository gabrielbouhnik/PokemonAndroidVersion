package com.pokemon.android.version.ui

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Gender
import com.pokemon.android.version.model.level.WildBattleLevel
import java.io.InputStream

class BattleUI {
    companion object {
        const val BOY_BACK_SPRITE = "images/HGSS_Ethan_Back.png"
        const val GIRL_BACK_SPRITE = "images/HGSS_Lyra_Back.png"
    }

    fun loadMainTrainerSprite(trainerBackSprite : ImageView, activity: MainActivity){
        val backSprite : String = if (activity.trainer!!.gender!! == Gender.MALE) BOY_BACK_SPRITE else GIRL_BACK_SPRITE
        var img : InputStream = activity.assets.open(backSprite)
        trainerBackSprite.setImageDrawable(Drawable.createFromStream(img, backSprite))
    }

    fun startWildBattles(activity : MainActivity, level : WildBattleLevel){
        activity.setContentView(R.layout.battle_layout)
        var trainerBackSprite : ImageView = activity.findViewById(R.id.trainerBackSpriteView)
        loadMainTrainerSprite(trainerBackSprite, activity)
    }
}