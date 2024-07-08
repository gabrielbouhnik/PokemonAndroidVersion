package com.pokemon.android.version.model.battle

import android.widget.ImageView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.item.HoldItem
import com.pokemon.android.version.model.level.LeaderLevelData
import com.pokemon.android.version.ui.BattleFrontierMenu
import com.pokemon.android.version.ui.LevelMenu
import com.pokemon.android.version.utils.MoveUtils

class LeaderBattle() : Battle() {
    private lateinit var opponentTrainer: OpponentTrainer
    private lateinit var opponentTrainerSprite: ImageView

    constructor(activity: MainActivity, leaderLevelData: LeaderLevelData) : this() {
        this.activity = activity
        this.opponentTrainerSprite = activity.findViewById(R.id.opponentTrainerSpriteView)
        this.levelData = leaderLevelData
        this.pokemon = if (leaderLevelData.id == BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID)
            activity.trainer!!.battleTowerProgression!!.team[0]
        else activity.trainer!!.getFirstPokemonThatCanFight()!!
        this.pokemon.battleData = PokemonBattleData()
        val team = if (leaderLevelData.id == 99) {
            if (activity.trainer!!.battleTowerProgression!!.progression == 7)
                leaderLevelData.battle1
            else
                leaderLevelData.battle2
        } else {
            if (activity.trainer!!.progression < LevelMenu.ELITE_4_LAST_LEVEL_ID)
                leaderLevelData.battle1
            else
                leaderLevelData.battle2
        }
        this.opponentTrainer = OpponentTrainer(
            leaderLevelData.title,
            team.map {
                activity.gameDataService.generatePokemonWithMoves(
                    it.id, it.level,
                    it.moves,
                    it.holdItem
                )
            }, leaderLevelData.sprite, leaderLevelData.iaLevel
        )
        if (activity.hardMode && activity.trainer!!.progression < LevelMenu.ELITE_4_LAST_LEVEL_ID) {
            when {
                levelData.id == LevelMenu.MISTY_LEVEL -> {
                    opponentTrainer.team = ArrayList(opponentTrainer.team)
                    (opponentTrainer.team as ArrayList<Pokemon>).add(
                        2,
                        activity.gameDataService.generatePokemonWithMoves(
                            195, 22,//QUASGIRE
                            listOf(
                                activity.gameDataService.getMoveById(145),//MUD SHOT
                                activity.gameDataService.getMoveById(24),//WATER PULSE
                                activity.gameDataService.getMoveById(226),//HAZE
                                activity.gameDataService.getMoveById(221)//ROCK TOMB
                            ),
                            HoldItem.LEFTOVERS
                        )
                    )
                    (opponentTrainer.team as ArrayList<Pokemon>).add(
                        2,
                        activity.gameDataService.generatePokemonWithMoves(
                            170, 22,//CHINCHOU
                            listOf(
                                activity.gameDataService.getMoveById(52),//SPARK
                                activity.gameDataService.getMoveById(24),//WATER PULSE
                                activity.gameDataService.getMoveById(273),//ELECTRO BALL
                                activity.gameDataService.getMoveById(48)//CONFUSE RAY
                            ),
                            HoldItem.MYSTIC_WATER
                        )
                    )
                }
                levelData.id == LevelMenu.SURGE_LEVEL -> {
                    opponentTrainer.team = ArrayList(opponentTrainer.team.filter {
                        it.data.id != 25 && it.data.id != 81 && it.data.id != 100 })
                    (opponentTrainer.team as ArrayList<Pokemon>).add(
                        1,
                        activity.gameDataService.generatePokemonWithMoves(
                            171, 27,//LANTURN
                            listOf(
                                activity.gameDataService.getMoveById(53),//DISCHARGE
                                activity.gameDataService.getMoveById(43),//SURF
                                activity.gameDataService.getMoveById(59),//PSYBEAM
                                activity.gameDataService.getMoveById(48)//CONFUSE RAY
                            ),
                            HoldItem.LEFTOVERS
                        ))
                    (opponentTrainer.team as ArrayList<Pokemon>).add(
                        1,
                        activity.gameDataService.generatePokemonWithMoves(
                            135, 30,//JOLTEON
                            listOf(
                                activity.gameDataService.getMoveById(12),//THUNDERBOLT
                                activity.gameDataService.getMoveById(46),//SHADOW BALL
                                activity.gameDataService.getMoveById(55),//THUNDER WAVE
                                activity.gameDataService.getMoveById(3)//QUICK ATTACK
                            ),
                            HoldItem.AIR_BALLOON
                        ))
                    (opponentTrainer.team as ArrayList<Pokemon>).add(
                        1,
                        activity.gameDataService.generatePokemonWithMoves(
                            82, 30,//MAGNETON
                            listOf(
                                activity.gameDataService.getMoveById(12),//THUNDERBOLT
                                activity.gameDataService.getMoveById(117),//TRI ATTACK
                                activity.gameDataService.getMoveById(111),//FLASH CANNON
                                activity.gameDataService.getMoveById(52)//SPARK
                            ),
                            HoldItem.AIR_BALLOON
                        ))
                    (opponentTrainer.team as ArrayList<Pokemon>).add(
                        1,
                        activity.gameDataService.generatePokemonWithMoves(
                            10101, 30,//HISUAN ELECTRODE
                            listOf(
                                activity.gameDataService.getMoveById(12),//THUNDERBOLT
                                activity.gameDataService.getMoveById(280),//CHLOROBLAST
                                activity.gameDataService.getMoveById(105),//ENERGY BALL
                                activity.gameDataService.getMoveById(273)//ELECTRO BALL
                            ),
                            HoldItem.MIRACLE_SEED
                        ))
                }
                this.levelData.id == LevelMenu.KOGA_LEVEL -> {
                    opponentTrainer.team = ArrayList(opponentTrainer.team.filter { it.data.id != 168
                            && it.data.id != 89})
                    (opponentTrainer.team as ArrayList<Pokemon>).add(
                        1,
                        activity.gameDataService.generatePokemonWithMoves(
                            472, 50,//GLISCOR
                            listOf(
                                activity.gameDataService.getMoveById(120),//THUNDER FANG
                                activity.gameDataService.getMoveById(62),//POISON JAB
                                activity.gameDataService.getMoveById(16),//WING ATTACK
                                activity.gameDataService.getMoveById(93)//EARTHQUAKE
                            ),
                            HoldItem.TOXIC_ORB
                        ))
                    (opponentTrainer.team as ArrayList<Pokemon>).add(
                        1,
                        activity.gameDataService.generatePokemonWithMoves(
                            10089, 50,//ALOLAN MUK
                            listOf(
                                activity.gameDataService.getMoveById(62),//POISON JAB
                                activity.gameDataService.getMoveById(107),//ICE PUNCH
                                activity.gameDataService.getMoveById(27),//CRUNCH
                                activity.gameDataService.getMoveById(25)//ROCK SLIDE
                            ),
                            HoldItem.BLACK_SLUDGE
                        ))
                }
                this.levelData.id == LevelMenu.SABRINA_LEVEL -> {
                    (this.levelData as LeaderLevelData).megaPokemonId = 65
                    (this.levelData as LeaderLevelData).megaPokemonIdOnRematch = 65
                }
            }
        }
        opponentTrainer.team.forEach {it.trainer = this.opponentTrainer}
        this.opponent = this.opponentTrainer.getFirstPokemonThatCanFight()!!
        activity.trainer!!.updatePokedex(opponent)
    }

    override fun updateOpponent() {
        if (opponentTrainer.canStillBattle())
            opponent = opponentTrainer.getFirstPokemonThatCanFight()!!
        activity.trainer!!.updatePokedex(opponent)
    }

    override fun getBattleState(): State {
        if (!opponentTrainer.canStillBattle()) {
            return State.TRAINER_VICTORY
        }
        if (levelData.id == 99) {
            return if (!activity.trainer!!.battleTowerProgression!!.team.any { it.currentHP > 0 })
                State.TRAINER_LOSS
            else State.IN_PROGRESS
        }
        if (!activity.trainer!!.canStillBattle() || MoveUtils.getMoveList(opponent).none { it.pp > 0 }) {
            return State.TRAINER_LOSS
        }
        return State.IN_PROGRESS
    }
}