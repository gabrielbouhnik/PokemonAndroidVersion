package com.pokemon.android.version.ui

import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Gender
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.battle.Battle
import com.pokemon.android.version.model.battle.State
import com.pokemon.android.version.model.battle.TrainerBattle
import com.pokemon.android.version.model.battle.WildBattle
import com.pokemon.android.version.model.item.ItemQuantity
import com.pokemon.android.version.model.level.LevelData
import com.pokemon.android.version.model.level.TrainerBattleLevelData
import com.pokemon.android.version.model.level.WildBattleLevelData
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.utils.MusicUtils
import java.io.InputStream

class BattleUI {
    companion object {
        const val BOY_BACK_SPRITE = "images/HGSS_Ethan_Back.png"
        const val GIRL_BACK_SPRITE = "images/HGSS_Lyra_Back.png"
    }


    var dialogTextView : TextView? = null
    var rewardMenu : RewardMenu = RewardMenu()

    private fun loadBackgroundImage(level : LevelData, activity: MainActivity){
        val background : ImageView = activity.findViewById(R.id.battleBackgroundImageView)
        val img : InputStream = activity.assets.open(level.background)
        background.setImageDrawable(Drawable.createFromStream(img, level.background))
    }

    private fun loadMainTrainerSprite(trainerBackSprite : ImageView, activity: MainActivity){
        val backSprite : String = if (activity.trainer!!.gender!! == Gender.MALE) BOY_BACK_SPRITE else GIRL_BACK_SPRITE
        val img : InputStream = activity.assets.open(backSprite)
        trainerBackSprite.setImageDrawable(Drawable.createFromStream(img, backSprite))
    }

    fun loadOpponentTrainerSprite(opponentTrainerSprite : ImageView, activity: MainActivity, filename : String){
        val img : InputStream = activity.assets.open(filename)
        opponentTrainerSprite.setImageDrawable(Drawable.createFromStream(img, filename))
    }

    private fun displayPokemonsInfos(activity : MainActivity, battle : Battle){
        val opponentPokemonSprite : ImageView = activity.findViewById(R.id.opponentPokemonImageView)
        opponentPokemonSprite.visibility = VISIBLE
        activity.displayPokemon(battle.opponent.data.id, opponentPokemonSprite)
        val pokemonBackSprite : ImageView = activity.findViewById(R.id.pokemonBackSpriteView)
        pokemonBackSprite.visibility = VISIBLE
        activity.displayPokemonBack(battle.pokemon.data.id, pokemonBackSprite)

        val trainerPokemonName : TextView =  activity.findViewById(R.id.myPokemonNameTextView)
        trainerPokemonName.text = battle.pokemon.data.name
        val trainerPokemonHPLevel : TextView =  activity.findViewById(R.id.myPokemonHPLevelTextView)
        trainerPokemonHPLevel.text = "Lv . ${battle.pokemon.level} ${battle.pokemon.currentHP}/${battle.pokemon.hp}"

        val opponentPokemonName : TextView =  activity.findViewById(R.id.opponentPokemonNameTextView)
        opponentPokemonName.text = battle.opponent.data.name
        val opponentPokemonHPLevel : TextView =  activity.findViewById(R.id.opponentPokemonHPLevelTextView)
        opponentPokemonHPLevel.text = "Lv . ${battle.opponent.level} ${battle.opponent.currentHP}/${battle.opponent.hp}"
    }

    private fun disableButton(activity: MainActivity, id : Int){
        val button : Button =  activity.findViewById(id)
        button.visibility = GONE
    }

    private fun disableAttackButtons(activity : MainActivity){

        disableButton(activity, R.id.attack1Button)
        disableButton(activity, R.id.attack2Button)
        disableButton(activity, R.id.attack3Button)
        disableButton(activity, R.id.attack4Button)
    }

    private fun disableBattleButtons(activity : MainActivity){
        disableAttackButtons(activity)
        disableButton(activity, R.id.bagButton)
        disableButton(activity, R.id.switchPokemonButton)
    }

    private fun updateByBattleState(activity : MainActivity, battle : Battle){
        when(battle.getBattleState()){
            State.TRAINER_LOSS -> {
                disableBattleButtons(activity)
                if (battle is TrainerBattle)
                    dialogTextView!!.text = (battle.levelData as TrainerBattleLevelData).endDialogLoose
                val rewardsButton : Button = activity.findViewById(R.id.getRewardsButton)
                rewardsButton.visibility = VISIBLE
                rewardsButton.text = "Exit"
                rewardsButton.setOnClickListener{
                    activity.mainMenu.loadGameMenu(activity)
                }
            }
            State.TRAINER_VICTORY -> {
                var firstTime : Boolean = activity.trainer!!.progression == battle.levelData.id
                activity.updateMusic(R.raw.victory_theme)
                if (firstTime)
                    activity.trainer!!.progression++
                disableBattleButtons(activity)
                val opponentPokemonSprite : ImageView = activity.findViewById(R.id.opponentPokemonImageView)
                opponentPokemonSprite.visibility = GONE
                val opponentPokemonName : TextView =  activity.findViewById(R.id.opponentPokemonNameTextView)
                opponentPokemonName.visibility = GONE
                val opponentPokemonHPLevel : TextView =  activity.findViewById(R.id.opponentPokemonHPLevelTextView)
                opponentPokemonHPLevel.visibility = GONE
                if (battle is TrainerBattle)
                    dialogTextView!!.text = (battle.levelData as TrainerBattleLevelData).endDialogWin
                val rewardsButton : Button = activity.findViewById(R.id.getRewardsButton)
                rewardsButton.visibility = VISIBLE
                rewardsButton.text = "See Rewards"
                rewardsButton.setOnClickListener{
                    activity.trainer!!.receiveExp(if (firstTime) battle.levelData.exp else battle.levelData.exp/2)
                    rewardMenu.loadRewardMenu(activity, battle.levelData, firstTime)
                }
            }
            else -> {
                if (battle.pokemon.currentHP == 0){
                    disableAttackButtons(activity)
                    val recyclerView = activity.findViewById<RecyclerView>(R.id.battleRecyclerView)
                    recyclerView.visibility = VISIBLE
                    val blackImageView : ImageView = activity.findViewById(R.id.blackImageView)
                    blackImageView.visibility = VISIBLE
                    recyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                    val myItemClickListener = View.OnClickListener {
                        val position = it.tag as Int
                        val clickedPokemon : Pokemon = activity.trainer!!.team[position]
                        if (clickedPokemon.currentHP > 0 && clickedPokemon != battle.pokemon) {
                            recyclerView.visibility = GONE
                            blackImageView.visibility = GONE
                            disableAttackButtons(activity)
                            battle.switchPokemon(clickedPokemon)
                            setUpAttackButtons(activity, battle)
                            displayPokemonsInfos(activity, battle)
                        }
                    }
                    val adapter =  PokemonRecyclerAdapter(activity, activity.trainer!!.team, myItemClickListener)
                    recyclerView.adapter = adapter
                }
            }
        }
    }

    private fun updateBattleUI(activity : MainActivity, battle : Battle){
        displayPokemonsInfos(activity, battle)
        updateByBattleState(activity, battle)
    }

    private fun setupAttackButton(activity : MainActivity, battle : Battle, move : PokemonMove?, id : Int){
        val attackButton : Button =  activity.findViewById(id)
        if (move != null && move.pp > 0){
            attackButton.visibility = VISIBLE
            attackButton.text = move.move.name
            ColorUtils.setButtonColor(move.move.type,attackButton)
            attackButton.setOnClickListener{
                battle.turn(move)
                updateBattleUI(activity, battle)
            }
        }
    }

    private fun setUpAttackButtons(activity : MainActivity, battle : Battle){
        val attack1Button : Button =  activity.findViewById(R.id.attack1Button)
        if (battle.pokemon.move1.pp > 0){
            attack1Button.visibility = VISIBLE
            attack1Button.text = battle.pokemon.move1.move.name
            ColorUtils.setButtonColor(battle.pokemon.move1.move.type,attack1Button)
            attack1Button.setOnClickListener{
                battle.turn(battle.pokemon.move1)
                updateBattleUI(activity, battle)
            }
        }
        setupAttackButton(activity, battle, battle.pokemon.move2,R.id.attack2Button)
        setupAttackButton(activity, battle, battle.pokemon.move3,R.id.attack3Button)
        setupAttackButton(activity, battle, battle.pokemon.move4,R.id.attack4Button)
    }

    private fun buttonSetUp(activity : MainActivity, battle : Battle){
        setUpAttackButtons(activity, battle)
        val switchButton : Button = activity.findViewById(R.id.switchPokemonButton)
        val bagButton : Button = activity.findViewById(R.id.bagButton)
        switchButton.setOnClickListener {
            if (!battle.pokemon.battleData!!.battleStatus.contains(Status.TRAPPED)) {
                val closeButton: Button = activity.findViewById(R.id.closeTeamButton)
                closeButton.visibility = VISIBLE
                switchButton.visibility = GONE
                bagButton.visibility = GONE
                disableAttackButtons(activity)

                val recyclerView = activity.findViewById<RecyclerView>(R.id.battleRecyclerView)
                recyclerView.visibility = VISIBLE
                val blackImageView: ImageView = activity.findViewById(R.id.blackImageView)
                blackImageView.visibility = VISIBLE
                recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                val myItemClickListener = View.OnClickListener {
                    val position = it.tag as Int
                    val clickedPokemon: Pokemon = activity.trainer!!.team[position]
                    if (clickedPokemon.currentHP > 0 && clickedPokemon != battle.pokemon) {
                        recyclerView.visibility = GONE
                        blackImageView.visibility = GONE
                        battle.turnWithSwitch(clickedPokemon)
                        updateBattleUI(activity, battle)
                        setUpAttackButtons(activity, battle)
                        closeButton.visibility = GONE
                        switchButton.visibility = VISIBLE
                        bagButton.visibility = VISIBLE
                    }
                }
                val adapter = PokemonRecyclerAdapter(activity, activity.trainer!!.team, myItemClickListener)
                recyclerView.adapter = adapter
                closeButton.setOnClickListener {
                    recyclerView.visibility = GONE
                    blackImageView.visibility = GONE
                    closeButton.visibility = GONE
                    switchButton.visibility = VISIBLE
                    bagButton.visibility = VISIBLE
                    setUpAttackButtons(activity, battle)
                }
            }
        }

        bagButton.setOnClickListener {
            val closeButton : Button = activity.findViewById(R.id.closeBagButton)
            closeButton.visibility = VISIBLE
            switchButton.visibility = GONE
            bagButton.visibility = GONE
            disableAttackButtons(activity)
            val blackImageView : ImageView = activity.findViewById(R.id.blackImageView)
            blackImageView.visibility = VISIBLE
            val recyclerView = activity.findViewById<RecyclerView>(R.id.battleRecyclerView)
            recyclerView.visibility = VISIBLE
            recyclerView.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            val items : ArrayList<ItemQuantity> = ArrayList(ItemQuantity.createItemQuantityFromHashMap(activity.trainer!!.items).filter{it.itemId < 14})
            val myItemClickListener = View.OnClickListener {
                val position = it.tag as Int
                val clickedItem : ItemQuantity = items[position]
                recyclerView.visibility = GONE
                blackImageView.visibility = GONE
                closeButton.visibility = GONE
                battle.turnWithItemUsed(clickedItem.itemId)
                updateBattleUI(activity, battle)
                if (battle.pokemon.currentHP > 0)
                    setUpAttackButtons(activity, battle)
                switchButton.visibility = VISIBLE
                bagButton.visibility = VISIBLE
            }
            val adapter = ItemRecyclerAdapter(activity, items, myItemClickListener)
            recyclerView.adapter = adapter
            closeButton.setOnClickListener {
                recyclerView.visibility = GONE
                blackImageView.visibility = GONE
                closeButton.visibility = GONE
                switchButton.visibility = VISIBLE
                bagButton.visibility = VISIBLE
                setUpAttackButtons(activity, battle)
            }
        }
    }

    fun startWildBattle(activity : MainActivity, level : WildBattleLevelData){
        activity.updateMusic(R.raw.wild_battle)
        activity.setContentView(R.layout.battle_layout)
        dialogTextView = activity.findViewById(R.id.dialogTextView)
        val trainerBackSprite : ImageView = activity.findViewById(R.id.trainerBackSpriteView)
        loadMainTrainerSprite(trainerBackSprite, activity)
        loadBackgroundImage(level,activity)
        val wildBattle = WildBattle(activity, level)
        buttonSetUp(activity,wildBattle)
        wildBattle.generateRandomEncounter()
        dialogTextView!!.text = "You encountered a wild ${wildBattle.opponent.data.name}!"
        displayPokemonsInfos(activity, wildBattle)
    }

    fun startTrainerBattle(activity : MainActivity, level : TrainerBattleLevelData){
        MusicUtils.playMusic(activity, level.music)
        activity.setContentView(R.layout.battle_layout)
        dialogTextView = activity.findViewById(R.id.dialogTextView)
        dialogTextView!!.text = level.startDialog
        val trainerBackSprite : ImageView = activity.findViewById(R.id.trainerBackSpriteView)
        loadMainTrainerSprite(trainerBackSprite, activity)
        loadBackgroundImage(level,activity)
        val opponentTrainerSprite : ImageView = activity.findViewById(R.id.opponentTrainerSpriteView)
        opponentTrainerSprite.visibility = VISIBLE
        loadOpponentTrainerSprite(opponentTrainerSprite,activity,level.opponentTrainerData[0].sprite)
        val trainerBattle = TrainerBattle(activity, level)
        val rewardsButton : Button = activity.findViewById(R.id.getRewardsButton)
        rewardsButton.visibility = VISIBLE
        rewardsButton.text = "BATTLE"
        rewardsButton.setOnClickListener{
            dialogTextView!!.text = "${level.opponentTrainerData[0].name} wants to battle"
            buttonSetUp(activity,trainerBattle)
            displayPokemonsInfos(activity, trainerBattle)
            rewardsButton.visibility = GONE
        }
    }
}