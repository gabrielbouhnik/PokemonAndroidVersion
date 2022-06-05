package com.pokemon.android.version.ui

import android.graphics.drawable.Drawable
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Gender
import com.pokemon.android.version.model.battle.Battle
import com.pokemon.android.version.model.battle.State
import com.pokemon.android.version.model.battle.TrainerBattle
import com.pokemon.android.version.model.battle.WildBattle
import com.pokemon.android.version.model.level.LevelData
import com.pokemon.android.version.model.level.TrainerBattleLevelData
import com.pokemon.android.version.model.level.WildBattleLevelData
import java.io.InputStream

class BattleUI {
    companion object {
        const val BOY_BACK_SPRITE = "images/HGSS_Ethan_Back.png"
        const val GIRL_BACK_SPRITE = "images/HGSS_Lyra_Back.png"
        var dialogTextView : TextView? = null
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

    fun loadOpponentTrainerSprite(trainerBackSprite : ImageView, activity: MainActivity, filename : String){
        var img : InputStream = activity.assets.open(filename)
        trainerBackSprite.setImageDrawable(Drawable.createFromStream(img, filename))
    }

    fun displayPokemonsInfos(activity : MainActivity, battle : Battle){
        var opponentPokemonSprite : ImageView = activity.findViewById(R.id.opponentPokemonImageView)
        opponentPokemonSprite.visibility = VISIBLE
        activity.displayPokemon(battle.opponent.data.id, opponentPokemonSprite)
        var pokemonBackSprite : ImageView = activity.findViewById(R.id.pokemonBackSpriteView)
        pokemonBackSprite.visibility = VISIBLE
        activity.displayPokemonBack(battle.pokemon.data.id, pokemonBackSprite)

        var trainerPokemonName : TextView =  activity.findViewById(R.id.myPokemonNameTextView)
        trainerPokemonName.text = battle.pokemon.data.name
        var trainerPokemonHPLevel : TextView =  activity.findViewById(R.id.myPokemonHPLevelTextView)
        trainerPokemonHPLevel.text = "Lv . ${battle.pokemon.level} ${battle.pokemon.currentHP}/${battle.pokemon.hp}"

        var opponentPokemonName : TextView =  activity.findViewById(R.id.opponentPokemonNameTextView)
        opponentPokemonName.text = battle.opponent.data.name
        var opponentPokemonHPLevel : TextView =  activity.findViewById(R.id.opponentPokemonHPLevelTextView)
        opponentPokemonHPLevel.text = "Lv . ${battle.opponent.level} ${battle.opponent.currentHP}/${battle.opponent.hp}"
    }

    fun endBattle(activity : MainActivity){
        var attack1Button : Button =  activity.findViewById(R.id.attack1Button)
        attack1Button.visibility = GONE
        var attack2Button : Button =  activity.findViewById(R.id.attack2Button)
        attack2Button.visibility = GONE
        var attack3Button : Button =  activity.findViewById(R.id.attack3Button)
        attack3Button.visibility = GONE
        var attack4Button : Button =  activity.findViewById(R.id.attack4Button)
        attack4Button.visibility = GONE
        var bagButton : Button =  activity.findViewById(R.id.bagButton)
        bagButton.visibility = GONE
        var switchPkmnButton : Button =  activity.findViewById(R.id.switchPokemonButton)
        switchPkmnButton.visibility = GONE
    }

    fun updateByBattleState(activity : MainActivity, battle : Battle){
        when(battle.getBattleState()){
            State.TRAINER_LOSS -> {
                if (activity.trainer!!.coins > 0)
                    activity.trainer!!.coins -= 10
                endBattle(activity)
                if (battle is TrainerBattle)
                    dialogTextView!!.text = (battle.levelData as TrainerBattleLevelData).endDialogLoose
                var rewardsButton : Button = activity.findViewById(R.id.getRewardsButton)
                rewardsButton.visibility = VISIBLE
                rewardsButton.text = "Exit"
                rewardsButton.setOnClickListener{
                    activity.mainMenu.loadGameMenu(activity)
                }
            }
            State.TRAINER_VICTORY -> {
                activity.updateMusic(R.raw.victory_theme)
                if (activity.trainer!!.progression == battle.levelData.id)
                    activity.trainer!!.progression++
                endBattle(activity)
                if (battle is TrainerBattle)
                    dialogTextView!!.text = (battle.levelData as TrainerBattleLevelData).endDialogWin
                var rewardsButton : Button = activity.findViewById(R.id.getRewardsButton)
                rewardsButton.visibility = VISIBLE
                rewardsButton.text = "See Rewards"
                rewardsButton.setOnClickListener{
                    rewardMenu.loadRewardMenu(activity, battle.levelData.rewards)
                }
            }
        }
    }

    fun updateUI(activity : MainActivity, battle : Battle){
        displayPokemonsInfos(activity, battle)
        updateByBattleState(activity, battle)
    }

    fun buttonSetUp(activity : MainActivity, battle : Battle){
        var attack1Button : Button =  activity.findViewById(R.id.attack1Button)
        if (battle.pokemon.move1.pp > 0){
            attack1Button.visibility = VISIBLE
            attack1Button.text = battle.pokemon.move1.move.name
            ColorUtils.setButtonColor(battle.pokemon.move1.move.type,attack1Button)
            attack1Button.setOnClickListener{
                battle.turn(battle.pokemon.move1)
                updateUI(activity, battle)
            }
        }
        var attack2Button : Button =  activity.findViewById(R.id.attack2Button)
        if (battle.pokemon.move2 != null && battle.pokemon.move2!!.pp > 0){
            attack2Button.visibility = VISIBLE
            attack2Button.text = battle.pokemon.move2!!.move.name
            ColorUtils.setButtonColor(battle.pokemon.move2!!.move.type,attack2Button)
            attack2Button.setOnClickListener{
                battle.turn(battle.pokemon.move2!!)
                updateUI(activity, battle)
            }
        }
        var attack3Button : Button =  activity.findViewById(R.id.attack3Button)
        if (battle.pokemon.move3 != null && battle.pokemon.move3!!.pp > 0){
            attack3Button.visibility = VISIBLE
            attack3Button.text = battle.pokemon.move3!!.move.name
            ColorUtils.setButtonColor(battle.pokemon.move3!!.move.type,attack3Button)
            attack3Button.setOnClickListener{
                battle.turn(battle.pokemon.move3!!)
                updateUI(activity, battle)
            }
        }
        var attack4Button : Button =  activity.findViewById(R.id.attack4Button)
        if (battle.pokemon.move4 != null && battle.pokemon.move4!!.pp > 0){
            attack4Button.visibility = VISIBLE
            attack4Button.text = battle.pokemon.move4!!.move.name
            ColorUtils.setButtonColor(battle.pokemon.move4!!.move.type,attack4Button)
            attack4Button.setOnClickListener{
                battle.turn(battle.pokemon.move3!!)
                updateUI(activity, battle)
            }
        }
    }

    fun startWildBattle(activity : MainActivity, level : WildBattleLevelData){
        activity.updateMusic(R.raw.wild_battle)
        activity.setContentView(R.layout.battle_layout)
        dialogTextView = activity.findViewById(R.id.dialogTextView)
        var trainerBackSprite : ImageView = activity.findViewById(R.id.trainerBackSpriteView)
        loadMainTrainerSprite(trainerBackSprite, activity)
        loadBackgroundImage(level,activity)
        var wildBattle = WildBattle(activity, level)
        buttonSetUp(activity,wildBattle)
        wildBattle.generateRandomEncounter()
        BattleUI.dialogTextView!!.text = "You encountered a wild ${wildBattle.opponent.data.name}!"
        displayPokemonsInfos(activity, wildBattle)
    }

    fun startTrainerBattle(activity : MainActivity, level : TrainerBattleLevelData){
        activity.updateMusic(R.raw.trainer_battle)
        activity.setContentView(R.layout.battle_layout)
        dialogTextView = activity.findViewById(R.id.dialogTextView)
        BattleUI.dialogTextView!!.text = level.startDialog
        var trainerBackSprite : ImageView = activity.findViewById(R.id.trainerBackSpriteView)
        loadMainTrainerSprite(trainerBackSprite, activity)
        loadBackgroundImage(level,activity)
        var opponentTrainerSprite : ImageView = activity.findViewById(R.id.opponentTrainerSpriteView)
        opponentTrainerSprite.visibility = VISIBLE
        loadOpponentTrainerSprite(opponentTrainerSprite,activity,level.opponentTrainerData[0].sprite)
        var trainerBattle = TrainerBattle(activity, level)
        var rewardsButton : Button = activity.findViewById(R.id.getRewardsButton)
        rewardsButton.visibility = VISIBLE
        rewardsButton.text = "BATTLE"
        rewardsButton.setOnClickListener{
            BattleUI.dialogTextView!!.text = "${level.opponentTrainerData[0].name} wants to battle"
            buttonSetUp(activity,trainerBattle)
            displayPokemonsInfos(activity, trainerBattle)
            rewardsButton.visibility = GONE
        }
    }
}