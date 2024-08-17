package com.pokemon.android.version.ui

import android.graphics.drawable.Drawable
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.model.*
import com.pokemon.android.version.model.battle.*
import com.pokemon.android.version.model.item.ItemQuantity
import com.pokemon.android.version.model.level.*
import com.pokemon.android.version.model.move.MoveCategory
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.ui.BattleFrontierMenu.Companion.FRONTIER_BRAIN_LEVEL_ID
import com.pokemon.android.version.ui.LevelMenu.Companion.ELITE_4_LAST_LEVEL_ID
import com.pokemon.android.version.ui.LevelMenu.Companion.ARMORED_MEWTWO_LEVEL_ID
import com.pokemon.android.version.ui.LevelMenu.Companion.ROUTE_3_LEVEL
import com.pokemon.android.version.utils.BattleUtils
import com.pokemon.android.version.utils.HealUtils
import com.pokemon.android.version.utils.ItemUtils
import com.pokemon.android.version.utils.MusicUtils
import java.io.InputStream
import kotlin.random.Random

class BattleUI {
    companion object {
        const val BOY_BACK_SPRITE = "images/HGSS_Ethan_Back.png"
        const val GIRL_BACK_SPRITE = "images/HGSS_Lyra_Back.png"
        const val MEGA_DISABLED_ICON = "images/mega/mega_disabled.png"
        const val MEGA_ENABLED_ICON = "images/mega/mega_enabled.png"
    }

    private var dialogTextView: TextView? = null
    private var rewardMenu: RewardMenu = RewardMenu()
    private var team: MutableList<Pokemon> = mutableListOf()
    private var megaEvolve = false

    private fun loadBackgroundImage(level: LevelData?, activity: MainActivity) {
        val background: ImageView = activity.findViewById(R.id.battleBackgroundImageView)
        if (level != null) {
            val img: InputStream = activity.assets.open(level.background)
            background.setImageDrawable(Drawable.createFromStream(img, level.background))
        } else {
            val img: InputStream = activity.assets.open(BattleFrontierBattle.BACKGROUND_IMAGE)
            background.setImageDrawable(Drawable.createFromStream(img, BattleFrontierBattle.BACKGROUND_IMAGE))
        }
    }

    private fun loadMainTrainerSprite(trainerBackSprite: ImageView, activity: MainActivity) {
        val backSprite: String = if (activity.trainer!!.gender!! == Gender.MALE) BOY_BACK_SPRITE else GIRL_BACK_SPRITE
        val img: InputStream = activity.assets.open(backSprite)
        trainerBackSprite.setImageDrawable(Drawable.createFromStream(img, backSprite))
    }

    private fun loadOpponentTrainerSprite(opponentTrainerSprite: ImageView, activity: MainActivity, filename: String) {
        val img: InputStream = activity.assets.open(filename)
        opponentTrainerSprite.setImageDrawable(Drawable.createFromStream(img, filename))
    }

    private fun displayPokemonsInfos(activity: MainActivity, battle: Battle) {
        val opponentPokemonSprite: ImageView = activity.findViewById(R.id.opponentPokemonImageView)
        opponentPokemonSprite.visibility = VISIBLE
        if (battle.opponent.isMegaEvolved) {
            activity.displayMegaPokemon(battle.opponent.data.id, battle.opponent.shiny, opponentPokemonSprite)
        } else
            activity.displayPokemon(battle.opponent.data.id, battle.opponent.shiny, opponentPokemonSprite)
        val pokemonBackSprite: ImageView = activity.findViewById(R.id.pokemonBackSpriteView)
        val megaEvolutionImageView: ImageView = activity.findViewById(R.id.megaEvolutionImageView)
        pokemonBackSprite.visibility = VISIBLE
        if (battle.pokemon.isMegaEvolved) {
            val filename =
                if (battle.pokemon.shiny) "images/mega/" + battle.pokemon.data.id + "_back_shiny.png" else "images/mega/" + battle.pokemon.data.id + "_back.png"
            val img: InputStream = activity.assets.open(filename)
            pokemonBackSprite.setImageDrawable(Drawable.createFromStream(img, filename))
        } else {
            activity.displayPokemonBack(battle.pokemon.data.id, battle.pokemon.shiny, pokemonBackSprite)
            if (battle.pokemon.canMegaEvolve() && !battle.trainerHasUsedMegaEvolution) {
                megaEvolutionImageView.visibility = VISIBLE
                megaEvolutionImageView.setImageDrawable(
                    Drawable.createFromStream(
                        activity.assets.open(
                            MEGA_DISABLED_ICON
                        ), MEGA_DISABLED_ICON
                    )
                )
                megaEvolutionImageView.setOnClickListener {
                    if (megaEvolve) {
                        megaEvolve = false
                        megaEvolutionImageView.setImageDrawable(
                            Drawable.createFromStream(
                                activity.assets.open(
                                    MEGA_DISABLED_ICON
                                ), MEGA_DISABLED_ICON
                            )
                        )
                    } else {
                        megaEvolve = true
                        megaEvolutionImageView.setImageDrawable(
                            Drawable.createFromStream(
                                activity.assets.open(
                                    MEGA_ENABLED_ICON
                                ), MEGA_ENABLED_ICON
                            )
                        )
                    }
                }
            } else {
                megaEvolutionImageView.visibility = GONE
            }
        }
        if (battle is BattleFrontierBattle && activity.trainer!!.battleFactoryProgression != null &&
            activity.trainer!!.battleFactoryProgression!!.team.contains(battle.pokemon)
        )
            megaEvolutionImageView.visibility = GONE
        val trainerPokemonName: TextView = activity.findViewById(R.id.myPokemonNameTextView)
        trainerPokemonName.text =
            if (battle.pokemon.isMegaEvolved) "Mega " + battle.pokemon.data.name else battle.pokemon.data.name
        val trainerPokemonHPLevel: TextView = activity.findViewById(R.id.myPokemonHPLevelTextView)
        trainerPokemonHPLevel.text = activity.getString(
            R.string.pokemon_battle_info,
            battle.pokemon.level,
            battle.pokemon.currentHP,
            battle.pokemon.hp
        )
        trainerPokemonHPLevel.setTextColor(ColorUtils.getColorByHP(battle.pokemon))
        val pokemonStatusTextView: TextView = activity.findViewById(R.id.pokemonBattleStatusTextView)
        if (battle.pokemon.status != Status.OK) {
            pokemonStatusTextView.visibility = VISIBLE
            pokemonStatusTextView.text = battle.pokemon.status.toBattleIcon()
            pokemonStatusTextView.setTextColor(ColorUtils.getColorByStatus(battle.pokemon.status))
        } else {
            pokemonStatusTextView.visibility = GONE
        }

        val opponentPokemonHPLevel: TextView = activity.findViewById(R.id.opponentPokemonHPLevelTextView)
        opponentPokemonHPLevel.text = activity.getString(
            R.string.pokemon_battle_info,
            battle.opponent.level,
            battle.opponent.currentHP,
            battle.opponent.hp
        )
        opponentPokemonHPLevel.setTextColor(ColorUtils.getColorByHP(battle.opponent))

        val opponentPokemonName: TextView = activity.findViewById(R.id.opponentPokemonNameTextView)
        opponentPokemonName.text =
            if (battle.opponent.isMegaEvolved) "Mega " + battle.opponent.data.name else battle.opponent.data.name

        val opponentStatusTextView: TextView = activity.findViewById(R.id.opponentStatusTextView)
        if (battle.opponent.status != Status.OK) {
            opponentStatusTextView.visibility = VISIBLE
            opponentStatusTextView.text = battle.opponent.status.toBattleIcon()
            opponentStatusTextView.setTextColor(ColorUtils.getColorByStatus(battle.opponent.status))
        } else {
            opponentStatusTextView.visibility = GONE
        }
    }

    private fun disableAttackButton(activity: MainActivity, id: Int, ppId: Int) {
        val button: Button = activity.findViewById(id)
        button.visibility = GONE
        val ppTextView: TextView = activity.findViewById(ppId)
        ppTextView.visibility = GONE
    }

    private fun disableButton(activity: MainActivity, id: Int) {
        val button: Button = activity.findViewById(id)
        button.visibility = GONE
    }

    private fun disableAttackButtons(activity: MainActivity) {
        disableAttackButton(activity, R.id.attack1Button, R.id.attack1PPTextView)
        disableAttackButton(activity, R.id.attack2Button, R.id.attack2PPTextView)
        disableAttackButton(activity, R.id.attack3Button, R.id.attack3PPTextView)
        disableAttackButton(activity, R.id.attack4Button, R.id.attack4PPTextView)
    }

    private fun disableBattleButtons(activity: MainActivity) {
        disableAttackButtons(activity)
        disableButton(activity, R.id.bagButton)
        disableButton(activity, R.id.switchPokemonButton)
    }

    private fun endEliteBattle(activity: MainActivity, battle: Battle) {
        val rewardsButton: Button = activity.findViewById(R.id.getRewardsButton)
        rewardsButton.visibility = VISIBLE
        if (activity.trainer!!.eliteProgression == 5) {
            rewardsButton.text = activity.getString(R.string.hall_of_fame)
            activity.trainer!!.eliteProgression = 0
            activity.eliteMode = false
            if (activity.trainer!!.progression == LevelMenu.ELITE_4_FIRST_LEVEL_ID) {
                activity.trainer!!.progression += 5
                activity.trainer!!.coins += 5000
                activity.trainer!!.achievements = Achievements()
            } else {
                activity.trainer!!.achievements!!.leagueDefeatedSecondTime = true
                if (activity.trainer!!.team.size <= 4)
                    activity.trainer!!.achievements!!.leagueWithTeamOfFourAchievement = true
            }
            HealUtils.healTeam(activity.trainer!!.team)
            rewardsButton.setOnClickListener {
                activity.trainer!!.receiveExp((battle.levelData.exp * 0.5).toInt())
                battle.pokemon.gainExp((battle.levelData.exp * 0.5).toInt())
                SaveManager.save(activity)
                rewardMenu.loadHallOfFameMenu(activity)
            }
        } else {
            rewardsButton.text = activity.getString(R.string.go_forward)
            rewardsButton.setOnClickListener {
                activity.trainer!!.coins += 150
                activity.trainer!!.receiveExp((battle.levelData.exp * 0.5).toInt())
                battle.pokemon.gainExp((battle.levelData.exp * 0.5).toInt())
                SaveManager.save(activity)
                if (activity.eliteMode)
                    activity.mainMenu.levelMenu.loadEliteLevels(activity)
                else
                    activity.mainMenu.loadGameMenu(activity)
            }
        }
    }

    private fun updateByBattleState(activity: MainActivity, battle: Battle) {
        when (battle.getBattleState()) {
            State.TRAINER_LOSS -> {
                if (battle is BattleFrontierBattle) {
                    if (battle.area == BattleFrontierArea.BATTLE_FACTORY)
                        activity.trainer!!.battleFactoryProgression = null
                    else
                        activity.trainer!!.battleTowerProgression = null
                    SaveManager.save(activity)
                } else if (battle.levelData.id == FRONTIER_BRAIN_LEVEL_ID) {
                    activity.trainer!!.battleTowerProgression = null
                    SaveManager.save(activity)
                }
                if (activity.eliteMode) {
                    activity.eliteMode = false
                    HealUtils.dailyHeal(activity.trainer!!)
                    activity.trainer!!.eliteProgression = 0
                }
                disableBattleButtons(activity)
                if (battle is TrainerBattle)
                    dialogTextView!!.text = (battle.levelData as TrainerBattleLevelData).endDialogLoose
                if (battle is LeaderBattle)
                    dialogTextView!!.text = (battle.levelData as LeaderLevelData).endDialogLoose
                val rewardsButton: Button = activity.findViewById(R.id.getRewardsButton)
                rewardsButton.visibility = VISIBLE
                rewardsButton.text = activity.getString(R.string.exit)
                rewardsButton.setOnClickListener {
                    if (battle is BattleFrontierBattle) {
                        activity.updateMusic(R.raw.main_menu)
                        activity.mainMenu.battleFrontierMenu.loadMenu(activity)
                    } else
                        activity.mainMenu.loadGameMenu(activity)
                }
            }
            State.TRAINER_VICTORY -> {
                if (battle is BattleFrontierBattle) {
                    HealUtils.healTeam(team)
                    team.forEach {
                        it.recomputeStat()
                        it.battleData = null
                    }
                    activity.updateMusic(R.raw.victory_theme)
                    if (battle.area == BattleFrontierArea.BATTLE_FACTORY) {
                        activity.trainer!!.battleFactoryProgression!!.progression += 1
                        if (activity.trainer!!.progression > LevelMenu.ELITE_4_LAST_LEVEL_ID && activity.trainer!!.battleFactoryProgression!!.progression >= 25)
                            activity.trainer!!.achievements!!.winstreak25Factory = true
                        activity.trainer!!.coins += 30 * (1 + (activity.trainer!!.battleFactoryProgression!!.progression / 5))
                    } else {
                        activity.trainer!!.battleTowerProgression!!.progression += 1
                        activity.trainer!!.coins += 30 * (1 + (activity.trainer!!.battleTowerProgression!!.progression / 5))
                    }
                    SaveManager.save(activity)
                    val rewardsButton: Button = activity.findViewById(R.id.getRewardsButton)
                    rewardsButton.visibility = VISIBLE
                    rewardsButton.text = activity.getString(R.string.go_forward)
                    rewardsButton.setOnClickListener {
                        activity.updateMusic(R.raw.main_menu)
                        activity.mainMenu.battleFrontierMenu.loadMenu(activity)
                    }
                    disableBattleButtons(activity)
                    return
                }
                activity.trainer!!.team.forEach {
                    it.recomputeStat()
                    if (it.hasAbility(Ability.PICKUP) && it.currentHP > 0) {
                        val random = Random.nextInt(10)
                        if (activity.hardMode) {
                            when {
                                random > 3 -> activity.trainer!!.addItem(1, 1)
                                random == 3 -> activity.trainer!!.addItem(8, 1)
                                random == 2 -> activity.trainer!!.addItem(2, 1)
                                random == 1 -> activity.trainer!!.addItem(4, 1)
                                random == 0 -> activity.trainer!!.addItem(9, 1)
                            }
                        } else {
                            when {
                                random > 7 -> activity.trainer!!.addItem(11, 1)
                                random in 4..6 -> activity.trainer!!.addItem(1, 1)
                                random == 3 -> activity.trainer!!.addItem(2, 1)
                                random == 2 -> activity.trainer!!.addItem(4, 1)
                                random == 1 -> activity.trainer!!.addItem(9, 1)
                                random == 0 -> activity.trainer!!.addItem(12, 1)
                            }
                        }
                    }
                }
                battle.pokemon.battleData = null
                if (battle.pokemon.hasAbility(Ability.NATURAL_CURE))
                    battle.pokemon.status = Status.OK
                if (battle.pokemon.hasAbility(Ability.REGENERATOR) && battle.pokemon.currentHP > 0) {
                    battle.pokemon.heal(battle.pokemon.hp / 3)
                }
                val firstTime: Boolean = activity.trainer!!.progression == battle.levelData.id
                if (activity.trainer!!.eliteProgression == 4)
                    activity.updateMusic(R.raw.hall_of_fame)
                else if (activity.trainer!!.eliteProgression > 0 || battle is LeaderBattle
                    || battle.levelData.id == LevelMenu.STEVEN_LEVEL_ID
                    || battle.levelData.id == LevelMenu.CYNTHIA_LEVEL_ID)
                    activity.updateMusic(R.raw.victory_theme2)
                else
                    activity.updateMusic(R.raw.victory_theme)
                if (activity.eliteMode)
                    activity.trainer!!.eliteProgression++
                else if (firstTime && battle.levelData.mandatory) {
                    activity.trainer!!.progression++
                }
                disableBattleButtons(activity)
                val opponentPokemonSprite: ImageView = activity.findViewById(R.id.opponentPokemonImageView)
                opponentPokemonSprite.visibility = GONE
                val opponentPokemonName: TextView = activity.findViewById(R.id.opponentPokemonNameTextView)
                opponentPokemonName.visibility = GONE
                val opponentPokemonHPLevel: TextView = activity.findViewById(R.id.opponentPokemonHPLevelTextView)
                opponentPokemonHPLevel.visibility = GONE
                if (battle is TrainerBattle)
                    dialogTextView!!.text = (battle.levelData as TrainerBattleLevelData).endDialogWin
                if (battle is LeaderBattle) {
                    if (battle.levelData.id == FRONTIER_BRAIN_LEVEL_ID) {
                        HealUtils.healTeam(team)
                        if (activity.trainer!!.battleTowerProgression!!.progression == 7)
                            dialogTextView!!.text = (battle.levelData as LeaderLevelData).endDialogWin1
                        else
                            dialogTextView!!.text = (battle.levelData as LeaderLevelData).endDialogWin2
                    } else {
                        if (activity.trainer!!.progression > LevelMenu.ELITE_4_LAST_LEVEL_ID)
                            dialogTextView!!.text = (battle.levelData as LeaderLevelData).endDialogWin2
                        else
                            dialogTextView!!.text = (battle.levelData as LeaderLevelData).endDialogWin1
                    }
                }
                if (battle.levelData.id == FRONTIER_BRAIN_LEVEL_ID) {
                    activity.trainer!!.coins += 5000
                    activity.trainer!!.battleTowerProgression!!.progression += 1
                }
                if (activity.eliteMode)
                    endEliteBattle(activity, battle)
                else {
                    val rewardsButton: Button = activity.findViewById(R.id.getRewardsButton)
                    rewardsButton.visibility = VISIBLE
                    rewardsButton.text = activity.getString(R.string.see_rewards)
                    rewardsButton.setOnClickListener {
                        activity.trainer!!.receiveExp((battle.levelData.exp * 0.5).toInt())
                        battle.pokemon.gainExp((battle.levelData.exp * 0.5).toInt())
                        rewardMenu.loadRewardMenu(activity, battle.levelData, firstTime)
                    }
                    SaveManager.save(activity)
                }
            }
            else -> {
                if (battle.pokemon.currentHP <= 0) {
                    disableBattleButtons(activity)
                    val switchButton: Button = activity.findViewById(R.id.switchPokemonButton)
                    switchButton.visibility = VISIBLE
                    switchButton.setOnClickListener {
                        megaEvolve = false
                        switchButton.visibility = GONE
                        val recyclerView = activity.findViewById<RecyclerView>(R.id.battleRecyclerView)
                        recyclerView.visibility = VISIBLE
                        val blackImageView: ImageView = activity.findViewById(R.id.blackImageView)
                        blackImageView.visibility = VISIBLE
                        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                        val myItemClickListener = View.OnClickListener {
                            val position = it.tag as Int
                            val clickedPokemon: Pokemon = team[position]
                            if (clickedPokemon.currentHP > 0) {
                                recyclerView.visibility = GONE
                                blackImageView.visibility = GONE
                                activity.findViewById<Button>(R.id.bagButton).visibility = VISIBLE
                                switchButton.visibility = VISIBLE
                                disableAttackButtons(activity)
                                switchButtonSetUp(activity, battle)
                                battle.switchPokemon(clickedPokemon)
                                setUpAttackButtons(activity, battle)
                                displayPokemonsInfos(activity, battle)
                                val sb = StringBuilder()
                                sb.append(dialogTextView!!.text.toString())
                                sb.append(BattleUtils.abilitiesCheck(battle.pokemon, battle.opponent))
                                dialogTextView!!.text = sb.toString()
                            }
                        }
                        val adapter = PokemonRecyclerAdapter(activity, team, myItemClickListener, false)
                        recyclerView.adapter = adapter
                    }
                }
            }
        }
    }

    private fun updateBattleUI(activity: MainActivity, battle: Battle) {
        displayPokemonsInfos(activity, battle)
        setUpAttackButtons(activity, battle)
        updateByBattleState(activity, battle)
    }

    private fun setupAttackButton(activity: MainActivity, battle: Battle, move: PokemonMove?, id: Int, ppId: Int) {
        val attackButton: Button = activity.findViewById(id)
        val ppTextView: TextView = activity.findViewById(ppId)
        if (move != null && move.pp > 0) {
            attackButton.visibility = VISIBLE
            attackButton.text = move.move.name
            attackButton.setBackgroundColor(ColorUtils.getColorByType(move.move.type))
            ppTextView.visibility = VISIBLE
            ppTextView.text = activity.getString(R.string.move_pp, move.pp, move.move.pp)
            attackButton.setOnClickListener {
                /*if (activity.trainer!!.name == PokedexMenu.ADMIN) {
                    Toast.makeText(
                        activity,
                        MoveUtils.getMoveList(battle.opponent).map{it.move.name}.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }*/
                if (move.disabledCountdown > 0) {
                    Toast.makeText(
                        activity,
                        "${move.move.name} is disabled.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (move.move.category == MoveCategory.OTHER && battle.pokemon.battleData!!.battleStatus.contains(
                        Status.TAUNTED
                    )
                ) {
                    Toast.makeText(
                        activity,
                        "${battle.pokemon.data.name} can't use ${battle.pokemon.move1.move.name} afterthe taunt",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (battle.pokemon.currentHP > 0 && battle.opponent.currentHP > 0) {
                    if (move.move.id == 185) {
                        activity.trainer!!.team.forEach { it.recomputeStat() }
                        battle.pokemon.battleData = null
                        activity.mainMenu.loadGameMenu(activity)
                    } else {
                        if (megaEvolve && battle.pokemon.canMegaEvolve()) {
                            val megaEvolutionImageView: ImageView = activity.findViewById(R.id.megaEvolutionImageView)
                            megaEvolutionImageView.visibility = GONE
                            dialogTextView!!.text = battle.turn(move, true)
                        } else {
                            megaEvolve = false
                            dialogTextView!!.text = battle.turn(move, false)
                        }
                        ppTextView.text = activity.getString(R.string.move_pp, move.pp, move.move.pp)
                        updateBattleUI(activity, battle)
                    }
                }
            }
        } else {
            attackButton.visibility = GONE
            ppTextView.visibility = GONE
        }
    }

    private fun setUpAttackButtons(activity: MainActivity, battle: Battle) {
        val attack1Button: Button = activity.findViewById(R.id.attack1Button)
        val ppTextView: TextView = activity.findViewById(R.id.attack1PPTextView)
        if (battle.pokemon.move1.pp > 0) {
            attack1Button.visibility = VISIBLE
            attack1Button.text = battle.pokemon.move1.move.name
            attack1Button.setBackgroundColor(ColorUtils.getColorByType(battle.pokemon.move1.move.type))
            ppTextView.visibility = VISIBLE
            ppTextView.text =
                activity.getString(R.string.move_pp, battle.pokemon.move1.pp, battle.pokemon.move1.move.pp)
            attack1Button.setOnClickListener {
                if (battle.pokemon.move1.isDisabled()) {
                    Toast.makeText(
                        activity,
                        "${battle.pokemon.move1.move.name} is disabled.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (battle.pokemon.move1.move.category == MoveCategory.OTHER && battle.pokemon.battleData!!.battleStatus.contains(
                        Status.TAUNTED
                    )
                ) {
                    Toast.makeText(
                        activity,
                        "${battle.pokemon.data.name} can't use ${battle.pokemon.move1.move.name} afterthe taunt",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (battle.pokemon.currentHP > 0 && battle.opponent.currentHP > 0) {
                    if (battle.pokemon.move1.move.id == 185) {
                        activity.trainer!!.team.forEach { it.recomputeStat() }
                        battle.pokemon.battleData = null
                        activity.mainMenu.loadGameMenu(activity)
                    } else {
                        if (megaEvolve && battle.pokemon.canMegaEvolve()) {
                            val megaEvolutionImageView: ImageView = activity.findViewById(R.id.megaEvolutionImageView)
                            megaEvolutionImageView.visibility = GONE
                            dialogTextView!!.text = battle.turn(battle.pokemon.move1, true)
                        } else {
                            megaEvolve = false
                            dialogTextView!!.text = battle.turn(battle.pokemon.move1, false)
                        }
                        ppTextView.text =
                            activity.getString(R.string.move_pp, battle.pokemon.move1.pp, battle.pokemon.move1.move.pp)
                        updateBattleUI(activity, battle)
                    }
                }
            }
        } else {
            attack1Button.visibility = GONE
            ppTextView.visibility = GONE
        }
        setupAttackButton(activity, battle, battle.pokemon.move2, R.id.attack2Button, R.id.attack2PPTextView)
        setupAttackButton(activity, battle, battle.pokemon.move3, R.id.attack3Button, R.id.attack3PPTextView)
        setupAttackButton(activity, battle, battle.pokemon.move4, R.id.attack4Button, R.id.attack4PPTextView)
    }

    private fun switchButtonSetUp(activity: MainActivity, battle: Battle) {
        val bagButton: Button = activity.findViewById(R.id.bagButton)
        val switchButton: Button = activity.findViewById(R.id.switchPokemonButton)
        switchButton.setOnClickListener {
            if ((!battle.pokemon.battleData!!.battleStatus.contains(Status.TRAPPED_WITH_DAMAGE)
                        && !battle.pokemon.battleData!!.battleStatus.contains(Status.TRAPPED_WITHOUT_DAMAGE)
                        || (battle.pokemon.hasType(Type.GHOST)))
            ) {
                megaEvolve = false
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
                    val clickedPokemon: Pokemon = team[position]
                    if (clickedPokemon.currentHP > 0 && clickedPokemon != battle.pokemon) {
                        recyclerView.visibility = GONE
                        blackImageView.visibility = GONE
                        dialogTextView!!.text = battle.turnWithSwitch(clickedPokemon)
                        closeButton.visibility = GONE
                        updateBattleUI(activity, battle)
                        setUpAttackButtons(activity, battle)
                        if (clickedPokemon.currentHP == 0) {
                            disableAttackButtons(activity)
                        } else {
                            switchButton.visibility = VISIBLE
                            bagButton.visibility = VISIBLE
                        }
                    }
                }
                val adapter = PokemonRecyclerAdapter(activity, team, myItemClickListener, false)
                recyclerView.adapter = adapter
                closeButton.setOnClickListener {
                    recyclerView.visibility = GONE
                    blackImageView.visibility = GONE
                    closeButton.visibility = GONE
                    switchButton.visibility = VISIBLE
                    bagButton.visibility = VISIBLE
                    setUpAttackButtons(activity, battle)
                }
            } else
                Toast.makeText(activity, "Your Pokemon is trapped and cannot be switched.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buttonSetUp(activity: MainActivity, battle: Battle) {
        setUpAttackButtons(activity, battle)
        switchButtonSetUp(activity, battle)
        val switchButton: Button = activity.findViewById(R.id.switchPokemonButton)
        val bagButton: Button = activity.findViewById(R.id.bagButton)
        if (battle !is BattleFrontierBattle && battle.levelData.id != FRONTIER_BRAIN_LEVEL_ID) {
            bagButton.setOnClickListener {
                if (battle is LeaderBattle
                    || battle.levelData.id == ARMORED_MEWTWO_LEVEL_ID - 1
                    || battle.levelData.id in ELITE_4_LAST_LEVEL_ID..ELITE_4_LAST_LEVEL_ID){
                    Toast.makeText(activity, "You can't use items in an official Pokémon League battle!", Toast.LENGTH_SHORT).show()
                } else {
                    megaEvolve = false
                    val closeButton: Button = activity.findViewById(R.id.closeBagButton)
                    closeButton.visibility = VISIBLE
                    switchButton.visibility = GONE
                    bagButton.visibility = GONE
                    disableAttackButtons(activity)
                    val blackImageView: ImageView = activity.findViewById(R.id.blackImageView)
                    blackImageView.visibility = VISIBLE
                    val recyclerView = activity.findViewById<RecyclerView>(R.id.battleRecyclerView)
                    recyclerView.visibility = VISIBLE
                    recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                    val items: ArrayList<ItemQuantity> = ArrayList(
                        ItemQuantity.createItemQuantityFromHashMap(activity.trainer!!.items)
                            .filter { battle.itemIsUsable(it.itemId) })
                    val myItemClickListener = View.OnClickListener {
                        val position = it.tag as Int
                        val clickedItem: ItemQuantity = items[position]
                        recyclerView.visibility = GONE
                        blackImageView.visibility = GONE
                        closeButton.visibility = GONE
                        if (ItemUtils.getItemById(clickedItem.itemId)
                                .isUsable(battle.pokemon) || (battle is WildBattle && ItemUtils.isBall(clickedItem.itemId))
                        ) {
                            dialogTextView!!.text = battle.turnWithItemUsed(clickedItem.itemId)
                            updateBattleUI(activity, battle)
                            if (battle.pokemon.currentHP > 0 && battle.getBattleState() == State.IN_PROGRESS) {
                                setUpAttackButtons(activity, battle)
                                switchButton.visibility = VISIBLE
                                bagButton.visibility = VISIBLE
                            }
                        } else {
                            setUpAttackButtons(activity, battle)
                            switchButton.visibility = VISIBLE
                            bagButton.visibility = VISIBLE
                        }
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
        }
    }

    private fun setDialogTextView(activity: MainActivity) {
        val dialogTextView: TextView = activity.findViewById(R.id.dialogTextView)
        this.dialogTextView = dialogTextView
        dialogTextView.movementMethod = ScrollingMovementMethod()
    }

    fun startWildBattle(activity: MainActivity, level: WildBattleLevelData) {
        if (activity.trainer!!.progression == 2)
            activity.showCustomDialog(activity.getString(R.string.tutorial_wild_battle))
        team = activity.trainer!!.team
        activity.updateMusic(R.raw.wild_battle)
        activity.setContentView(R.layout.battle_layout)
        setDialogTextView(activity)
        val trainerBackSprite: ImageView = activity.findViewById(R.id.trainerBackSpriteView)
        loadMainTrainerSprite(trainerBackSprite, activity)
        loadBackgroundImage(level, activity)
        val wildBattle = WildBattle(activity, level)
        buttonSetUp(activity, wildBattle)
        wildBattle.generateRandomEncounter()
        val sb = StringBuilder()
        sb.append(activity.getString(R.string.wild_encounter, wildBattle.opponent.data.name) + '\n')
        if (wildBattle.opponent.shiny)
            activity.showCustomDialog("It's shiny!\n")
        sb.append(BattleUtils.abilitiesCheck(wildBattle.pokemon, wildBattle.opponent))
        sb.append(BattleUtils.abilitiesCheck(wildBattle.opponent, wildBattle.pokemon))
        dialogTextView!!.text = sb.toString()
        displayPokemonsInfos(activity, wildBattle)
    }

    fun startTrainerBattle(activity: MainActivity, level: TrainerBattleLevelData) {
        if (activity.trainer!!.progression == 10 && level.id == ROUTE_3_LEVEL)
            activity.showCustomDialog(activity.getString(R.string.tutorial_trainer_battle))
        this.team = activity.trainer!!.team
        MusicUtils.playMusic(activity, level.music)
        activity.setContentView(R.layout.battle_layout)
        setDialogTextView(activity)
        dialogTextView!!.text = level.startDialog
        val trainerBackSprite: ImageView = activity.findViewById(R.id.trainerBackSpriteView)
        loadMainTrainerSprite(trainerBackSprite, activity)
        loadBackgroundImage(level, activity)
        val opponentTrainerSprite: ImageView = activity.findViewById(R.id.opponentTrainerSpriteView)
        opponentTrainerSprite.visibility = VISIBLE
        loadOpponentTrainerSprite(opponentTrainerSprite, activity, level.opponentTrainerData[0].sprite)
        val trainerBattle = TrainerBattle(activity, level)
        val rewardsButton: Button = activity.findViewById(R.id.getRewardsButton)
        rewardsButton.visibility = VISIBLE
        rewardsButton.text = activity.getString(R.string.battle)
        rewardsButton.setOnClickListener {
            val sb = StringBuilder()
            sb.append(activity.getString(R.string.trainer_encounter, level.opponentTrainerData[0].name) + "\n")
            sb.append("${level.opponentTrainerData[0].name} sends out ${trainerBattle.opponent.data.name}\n")
            sb.append(BattleUtils.abilitiesCheck(trainerBattle.pokemon, trainerBattle.opponent))
            sb.append(BattleUtils.abilitiesCheck(trainerBattle.opponent, trainerBattle.pokemon))
            dialogTextView!!.text = sb.toString()
            buttonSetUp(activity, trainerBattle)
            displayPokemonsInfos(activity, trainerBattle)
            rewardsButton.visibility = GONE
        }
    }

    fun startGymLeaderBattle(activity: MainActivity, level: LeaderLevelData) {
        if (activity.trainer!!.progression == 9)
            activity.showCustomDialog(activity.getString(R.string.tutorial_gym_leader))
        this.team = if (level.id == FRONTIER_BRAIN_LEVEL_ID)
            activity.trainer!!.battleTowerProgression!!.team.toMutableList()
        else
            activity.trainer!!.team
        MusicUtils.playMusic(activity, level.music)
        activity.setContentView(R.layout.battle_layout)
        setDialogTextView(activity)
        if (activity.trainer!!.progression > LevelMenu.ELITE_4_LAST_LEVEL_ID)
            dialogTextView!!.text = level.startDialog2
        else
            dialogTextView!!.text = level.startDialog1
        val trainerBackSprite: ImageView = activity.findViewById(R.id.trainerBackSpriteView)
        loadMainTrainerSprite(trainerBackSprite, activity)
        loadBackgroundImage(level, activity)
        val opponentTrainerSprite: ImageView = activity.findViewById(R.id.opponentTrainerSpriteView)
        opponentTrainerSprite.visibility = VISIBLE
        loadOpponentTrainerSprite(opponentTrainerSprite, activity, level.sprite)
        val gymLeaderBattle = LeaderBattle(activity, level)
        val rewardsButton: Button = activity.findViewById(R.id.getRewardsButton)
        rewardsButton.visibility = VISIBLE
        rewardsButton.text = activity.getString(R.string.battle)
        rewardsButton.setOnClickListener {
            val sb = StringBuilder()
            sb.append(activity.getString(R.string.trainer_encounter, level.title) + "\n")
            sb.append("${level.title} sends out ${gymLeaderBattle.opponent.data.name}\n")
            sb.append(BattleUtils.abilitiesCheck(gymLeaderBattle.pokemon, gymLeaderBattle.opponent))
            sb.append(BattleUtils.abilitiesCheck(gymLeaderBattle.opponent, gymLeaderBattle.pokemon))
            dialogTextView!!.text = sb.toString()
            buttonSetUp(activity, gymLeaderBattle)
            displayPokemonsInfos(activity, gymLeaderBattle)
            rewardsButton.visibility = GONE
        }
    }

    fun startBossBattle(activity: MainActivity, level: BossBattleLevelData) {
        if (activity.trainer!!.progression == LevelMenu.DUGTRIO_LEVEL)
            activity.showCustomDialog(activity.getString(R.string.tutorial_boss_battle))
        team = activity.trainer!!.team
        MusicUtils.playMusic(activity, level.music)
        activity.setContentView(R.layout.battle_layout)
        setDialogTextView(activity)
        val trainerBackSprite: ImageView = activity.findViewById(R.id.trainerBackSpriteView)
        loadMainTrainerSprite(trainerBackSprite, activity)
        loadBackgroundImage(level, activity)
        val bossBattle = BossBattle(activity, level)
        buttonSetUp(activity, bossBattle)
        val sb = StringBuilder()
        sb.append(activity.getString(R.string.boss_encounter, bossBattle.opponent.data.name) + '\n')
        sb.append(BattleUtils.abilitiesCheck(bossBattle.pokemon, bossBattle.opponent))
        sb.append(BattleUtils.abilitiesCheck(bossBattle.opponent, bossBattle.pokemon))
        dialogTextView!!.text = sb.toString()
        displayPokemonsInfos(activity, bossBattle)
    }

    fun startBattleFrontierBattle(activity: MainActivity, area: BattleFrontierArea) {
        if (area == BattleFrontierArea.BATTLE_FACTORY)
            this.team = activity.trainer!!.battleFactoryProgression!!.team.toMutableList()
        else
            this.team = activity.trainer!!.battleTowerProgression!!.team.toMutableList()
        MusicUtils.playMusic(activity, R.raw.trainer_battle)
        activity.setContentView(R.layout.battle_layout)
        MusicUtils.playMusic(activity, 2)
        val trainerBackSprite: ImageView = activity.findViewById(R.id.trainerBackSpriteView)
        loadMainTrainerSprite(trainerBackSprite, activity)
        loadBackgroundImage(null, activity)
        val opponentTrainerSprite: ImageView = activity.findViewById(R.id.opponentTrainerSpriteView)
        opponentTrainerSprite.visibility = VISIBLE
        val battleFrontierBattle = BattleFrontierBattle(activity, team, area)
        loadOpponentTrainerSprite(opponentTrainerSprite, activity, battleFrontierBattle.opponentTrainer.sprite)
        val rewardsButton: Button = activity.findViewById(R.id.getRewardsButton)
        rewardsButton.visibility = VISIBLE
        rewardsButton.text = activity.getString(R.string.battle)
        setDialogTextView(activity)
        rewardsButton.setOnClickListener {
            val sb = StringBuilder()
            sb.append(activity.getString(R.string.battle_frontier_encounter) + "\n")
            sb.append("The opposing trainer sends out ${battleFrontierBattle.opponent.data.name}\n")
            sb.append(BattleUtils.abilitiesCheck(battleFrontierBattle.pokemon, battleFrontierBattle.opponent))
            sb.append(BattleUtils.abilitiesCheck(battleFrontierBattle.opponent, battleFrontierBattle.pokemon))
            dialogTextView!!.text = sb.toString()
            buttonSetUp(activity, battleFrontierBattle)
            displayPokemonsInfos(activity, battleFrontierBattle)
            rewardsButton.visibility = GONE
        }
    }
}