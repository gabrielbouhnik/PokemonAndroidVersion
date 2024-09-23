package com.pokemon.android.version.model.battle

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.item.HoldItem
import com.pokemon.android.version.model.level.TrainerBattleLevelData
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.ui.LevelMenu
import com.pokemon.android.version.utils.IAUtils
import com.pokemon.android.version.utils.MoveUtils
import java.io.InputStream

class TrainerBattle() : Battle() {
    private var numberOfTrainers: Int = 0
    private var trainersIdx: Int = 0
    private lateinit var opponentTrainer: OpponentTrainer
    private lateinit var opponentTrainerSprite: ImageView

    constructor(activity: MainActivity, trainerBattleLevelData: TrainerBattleLevelData) : this() {
        this.activity = activity
        this.opponentTrainerSprite = activity.findViewById(R.id.opponentTrainerSpriteView)
        this.levelData = trainerBattleLevelData
        this.numberOfTrainers = trainerBattleLevelData.opponentTrainerData.size
        this.pokemon = activity.trainer!!.getFirstPokemonThatCanFight()!!
        this.pokemon.battleData = PokemonBattleData()
        this.opponentTrainer = OpponentTrainerFactory.createOpponentTrainer(
            trainerBattleLevelData.opponentTrainerData.first(),
            activity.gameDataService
        )
        if (activity.hardMode) {
            if (this.levelData.id == LevelMenu.KAREN_LEVEL) {
                (this.levelData as TrainerBattleLevelData).megaPokemonId = 229
                opponentTrainer.team.forEach {
                    if (it.data.id == 229) {
                        it.heldItem = null
                    }
                }
            }
            if (this.levelData.id == LevelMenu.ARCHER_LEVEL) {
                (this.levelData as TrainerBattleLevelData).megaPokemonId = 248
                opponentTrainer.team = ArrayList(opponentTrainer.team.filter { it.data.id == 229 || it.data.id == 110})
                (opponentTrainer.team as ArrayList<Pokemon>).add(
                    1,
                    activity.gameDataService.generatePokemonWithMoves(
                        10101, 57,//HISUAN ELECTRODE
                        listOf(
                            activity.gameDataService.getMoveById(12),//THUNDERBOLT
                            activity.gameDataService.getMoveById(105),//ENERGY BALL
                            activity.gameDataService.getMoveById(280),//CHLOROBLAST
                            activity.gameDataService.getMoveById(273)//ELECTRO BALL
                        ),
                        HoldItem.MIRACLE_SEED
                    ))
                (opponentTrainer.team as ArrayList<Pokemon>).add(
                    1,
                    activity.gameDataService.generatePokemonWithMoves(
                        604, 57,//Eelektross
                        listOf(
                            activity.gameDataService.getMoveById(12),//THUNDERBOLT
                            activity.gameDataService.getMoveById(31),//FLAMETHROWER
                            activity.gameDataService.getMoveById(25),//ROCK SLIDE
                            activity.gameDataService.getMoveById(66)//GIGA DRAIN
                        ),
                        HoldItem.LIFE_ORB
                    ))
                (opponentTrainer.team as ArrayList<Pokemon>).add(
                    1,
                    activity.gameDataService.generatePokemonWithMoves(
                        169, 57,//CROBAT
                        listOf(
                            activity.gameDataService.getMoveById(48),//CONFUSE RAY
                            activity.gameDataService.getMoveById(129),//CROSS POISON
                            activity.gameDataService.getMoveById(128),//LEECH LIFE
                            activity.gameDataService.getMoveById(16)//WING ATTACK
                        ),
                        HoldItem.BLACK_SLUDGE
                    ))
                (opponentTrainer.team as ArrayList<Pokemon>).add(
                    1,
                    activity.gameDataService.generatePokemonWithMoves(
                        248, 58,//TYRANITAR
                        listOf(
                            activity.gameDataService.getMoveById(27),//CRUNCH
                            activity.gameDataService.getMoveById(25),//ROCK SLIDE
                            activity.gameDataService.getMoveById(93),//EARTHQUAKE
                            activity.gameDataService.getMoveById(31)//FLAMETHROWER
                        ),
                        null
                    ))
            }
            if (this.levelData.id == LevelMenu.GIOVANNI_LEVEL) {
                (this.levelData as TrainerBattleLevelData).megaPokemonId = 115
                opponentTrainer.team.forEach {
                    if (it.data.id == 115) {
                        it.heldItem = null
                    }
                }
                (opponentTrainer.team as ArrayList<Pokemon>).add(
                    activity.gameDataService.generatePokemonWithMoves(
                        130, 57,//GYARADOS
                        listOf(
                            activity.gameDataService.getMoveById(136),//WATERFALL
                            activity.gameDataService.getMoveById(27),//CRUNCH
                            activity.gameDataService.getMoveById(125),//DRAGON DANCE
                            activity.gameDataService.getMoveById(93)//EARTHQUAKE
                        ),
                        HoldItem.LEFTOVERS
                    ))
                opponentTrainer.team = ArrayList(opponentTrainer.team.filter { it.data.id != 34})
            }
            if (this.levelData.id == LevelMenu.GIOVANNI_2_LEVEL) {
                (this.levelData as TrainerBattleLevelData).megaPokemonId = 130
                (opponentTrainer.team as ArrayList<Pokemon>).add(
                    activity.gameDataService.generatePokemonWithMoves(
                        472, 67,//GLISCOR
                        listOf(
                            activity.gameDataService.getMoveById(120),//THUNDER FANG
                            activity.gameDataService.getMoveById(62),//POISON JAB
                            activity.gameDataService.getMoveById(16),//WING ATTACK
                            activity.gameDataService.getMoveById(93)//EARTHQUAKE
                        ),
                        HoldItem.TOXIC_ORB
                    ))
                (opponentTrainer.team as ArrayList<Pokemon>).add(
                    activity.gameDataService.generatePokemonWithMoves(
                        130, 67,//GYARADOS
                        listOf(
                            activity.gameDataService.getMoveById(136),//WATERFALL
                            activity.gameDataService.getMoveById(27),//CRUNCH
                            activity.gameDataService.getMoveById(125),//DRAGON DANCE
                            activity.gameDataService.getMoveById(93)//EARTHQUAKE
                        ),
                        HoldItem.LEFTOVERS
                    ))
                opponentTrainer.team = ArrayList(opponentTrainer.team.filter { it.data.id != 31 && it.data.id != 112})
            }
            if (this.levelData.id == LevelMenu.ARMORED_MEWTWO_LEVEL_ID) {
                this.opponentTrainer.team[0].move2 = PokemonMove(activity.gameDataService.getMoveById(218))//PSYSTRIKE
                this.opponentTrainer.team[0].move4 = PokemonMove(activity.gameDataService.getMoveById(124))//AURA SPHERE
            }
            opponentTrainer.team.forEach {it.trainer = this.opponentTrainer}
        }
        this.opponent = this.opponentTrainer.getFirstPokemonThatCanFight()!!
        activity.trainer!!.updatePokedex(opponent)
    }

    override fun updateOpponent() {
        opponent.recomputeStat()
        if (opponentTrainer.canStillBattle()) {
            opponent = if (opponentTrainer.iaLevel != 1 && pokemon.currentHP > 0)
                IAUtils.getBestPokemonToSentAfterKo(pokemon, opponentTrainer.getTrainerTeam())!!
            else
                opponentTrainer.getFirstPokemonThatCanFight()!!
        }
        else
            nextTrainer()
        activity.trainer!!.updatePokedex(opponent)
    }

    override fun getBattleState(): State {
        if (numberOfTrainers == trainersIdx) {
            return State.TRAINER_VICTORY
        }
        if (!activity.trainer!!.canStillBattle() || MoveUtils.getMoveList(opponent).none { it.pp > 0 }) {
            return State.TRAINER_LOSS
        }
        return State.IN_PROGRESS
    }

    private fun nextTrainer() {
        trainersIdx++
        if (numberOfTrainers > trainersIdx) {
            opponentTrainer = OpponentTrainerFactory.createOpponentTrainer(
                (levelData as TrainerBattleLevelData).opponentTrainerData[trainersIdx],
                activity.gameDataService
            )
            opponent = opponentTrainer.getFirstPokemonThatCanFight()!!
            val img: InputStream = activity.assets.open(opponentTrainer.sprite)
            opponentTrainerSprite.setImageDrawable(Drawable.createFromStream(img, opponentTrainer.sprite))
        }
    }
}