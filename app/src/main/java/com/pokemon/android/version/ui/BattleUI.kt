package com.pokemon.android.version.ui

import android.graphics.drawable.Drawable
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Gender
import com.pokemon.android.version.model.battle.State
import com.pokemon.android.version.model.battle.WildBattle
import com.pokemon.android.version.model.level.LevelData
import com.pokemon.android.version.model.level.WildBattleLevelData
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import java.io.InputStream

class BattleUI {
    companion object {
        const val BOY_BACK_SPRITE = "images/HGSS_Ethan_Back.png"
        const val GIRL_BACK_SPRITE = "images/HGSS_Lyra_Back.png"
    }

    var rewardMenu : RewardMenu = RewardMenu()

    fun loadBackgroundImage(level : LevelData, activity: MainActivity){
        var background : ImageView = activity.findViewById(R.id.battleBackgroundImageView)
        var img : InputStream = activity.assets.open(level.background)
        background.setImageDrawable(Drawable.createFromStream(img, level.background))
    }

    fun loadMainTrainerSprite(trainerBackSprite : ImageView, activity: MainActivity){
        val backSprite : String = if (activity.trainer!!.gender!! == Gender.MALE) BOY_BACK_SPRITE else GIRL_BACK_SPRITE
        var img : InputStream = activity.assets.open(backSprite)
        trainerBackSprite.setImageDrawable(Drawable.createFromStream(img, backSprite))
    }

    fun displayPokemonsInfos(activity : MainActivity, wildBattle : WildBattle){
        var wildPokemonSprite : ImageView = activity.findViewById(R.id.opponentPokemonImageView)
        activity.displayPokemon(wildBattle.opponent.data.id, wildPokemonSprite)
        var pokemonBackSprite : ImageView = activity.findViewById(R.id.pokemonBackSpriteView)
        activity.displayPokemonBack(wildBattle.pokemon.data.id, pokemonBackSprite)

        var trainerPokemonName : TextView =  activity.findViewById(R.id.myPokemonNameTextView)
        trainerPokemonName.text = wildBattle.pokemon.data.name
        var trainerPokemonHPLevel : TextView =  activity.findViewById(R.id.myPokemonHPLevelTextView)
        trainerPokemonHPLevel.text = "Lv . ${wildBattle.pokemon.level} ${wildBattle.pokemon.currentHP}/${wildBattle.pokemon.hp}"

        var opponentPokemonName : TextView =  activity.findViewById(R.id.opponentPokemonNameTextView)
        opponentPokemonName.text = wildBattle.opponent.data.name
        var opponentPokemonHPLevel : TextView =  activity.findViewById(R.id.opponentPokemonHPLevelTextView)
        opponentPokemonHPLevel.text = "Lv . ${wildBattle.opponent.level} ${wildBattle.opponent.currentHP}/${wildBattle.opponent.hp}"
    }

    fun updateByWildBattleState(activity : MainActivity, wildBattle : WildBattle){
        when(wildBattle.getBattleState()){
            State.TRAINER_LOSS -> {
                wildBattle.pokemon.currentHP = wildBattle.pokemon.hp
                if (activity.trainer!!.coins > 0)
                    activity.trainer!!.coins -= 10
                activity.mainMenu.loadGameMenu(activity)
            }
            State.TRAINER_VICTORY -> {
                if (activity.trainer!!.progression + 2 == wildBattle.wildBattleLevelData.id)
                    activity.trainer!!.progression++
                rewardMenu.loadRewardMenu(activity, wildBattle.wildBattleLevelData.rewards)
            }
        }
    }

    fun updateUI(activity : MainActivity, wildBattle : WildBattle){
        displayPokemonsInfos(activity, wildBattle)
        updateByWildBattleState(activity, wildBattle)
    }

    fun buttonSetUp(activity : MainActivity, wildBattle : WildBattle){
        var attack1Button : Button =  activity.findViewById(R.id.attack1Button)
        if (wildBattle.pokemon.move1.pp > 0){
            attack1Button.visibility = VISIBLE
            attack1Button.text = wildBattle.pokemon.move1.move.name
            attack1Button.setOnClickListener{
                wildBattle.turn(wildBattle.pokemon.move1)
                updateUI(activity, wildBattle)
            }
        }
        var attack2Button : Button =  activity.findViewById(R.id.attack2Button)
        if (wildBattle.pokemon.move2 != null && wildBattle.pokemon.move2!!.pp > 0){
            attack2Button.visibility = VISIBLE
            attack2Button.text = wildBattle.pokemon.move2!!.move.name
            attack2Button.setOnClickListener{
                wildBattle.turn(wildBattle.pokemon.move2!!)
                updateUI(activity, wildBattle)
            }
        }
        var attack3Button : Button =  activity.findViewById(R.id.attack3Button)
        if (wildBattle.pokemon.move3 != null && wildBattle.pokemon.move3!!.pp > 0){
            attack3Button.visibility = VISIBLE
            attack3Button.text = wildBattle.pokemon.move3!!.move.name
            attack3Button.setOnClickListener{
                wildBattle.turn(wildBattle.pokemon.move3!!)
                updateUI(activity, wildBattle)
            }
        }
        var attack4Button : Button =  activity.findViewById(R.id.attack4Button)
        if (wildBattle.pokemon.move4 != null && wildBattle.pokemon.move4!!.pp > 0){
            attack4Button.visibility = VISIBLE
            attack4Button.text = wildBattle.pokemon.move4!!.move.name
            attack4Button.setOnClickListener{
                wildBattle.turn(wildBattle.pokemon.move3!!)
                updateUI(activity, wildBattle)
            }
        }
    }

    fun startWildBattle(activity : MainActivity, level : WildBattleLevelData){
        activity.updateMusic(R.raw.wild_battle)
        activity.setContentView(R.layout.battle_layout)
        var dialogTextView : TextView = activity.findViewById(R.id.dialogTextView)
        dialogTextView.text = level.description
        var trainerBackSprite : ImageView = activity.findViewById(R.id.trainerBackSpriteView)
        loadMainTrainerSprite(trainerBackSprite, activity)
        loadBackgroundImage(level,activity)
        var wildBattle = WildBattle(activity, level)
        buttonSetUp(activity,wildBattle)
        wildBattle.generateRandomEncounter()
        displayPokemonsInfos(activity, wildBattle)
    }
}