package com.pokemon.android.version.model

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.save.PokemonSave
import com.pokemon.android.version.model.battle.*
import com.pokemon.android.version.model.item.HoldItem
import com.pokemon.android.version.model.level.PokemonBoss
import com.pokemon.android.version.model.move.*
import com.pokemon.android.version.model.move.Target
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.utils.BattleUtils
import com.pokemon.android.version.utils.ItemUtils.Companion.MEGA_RING_ID
import com.pokemon.android.version.utils.MoveUtils
import com.pokemon.android.version.utils.StatUtils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.random.nextInt

open class Pokemon(
    var data: PokemonData,
    var trainer: ITrainer?,
    var level: Int,
    var move1: PokemonMove,
    var move2: PokemonMove?,
    var move3: PokemonMove?,
    var move4: PokemonMove?,
    var status: Status = Status.OK,
    var hp: Int = 0,
    var attack: Int = 0,
    var defense: Int = 0,
    var spAtk: Int = 0,
    var spDef: Int = 0,
    var speed: Int = 0,
    var currentHP: Int = 0,
    var currentExp: Int = 0,
    var shiny: Boolean,
    var battleData: PokemonBattleData?,
    var heldItem: HoldItem?,
    var isFromBanner: Boolean = false,
    var movesLearnedByTM: ArrayList<Move> = arrayListOf(),
    var isMegaEvolved: Boolean = false,
    var itemDisabled: Boolean = false
) {

    constructor(
        data: PokemonData, trainer: Trainer?, level: Int,
        move1: PokemonMove, move2: PokemonMove?, move3: PokemonMove?, move4: PokemonMove?,
        hp: Int, attack: Int, defense: Int, spAtk: Int, spDef: Int, speed: Int, currentHP: Int,
        shiny: Boolean
    )
            : this(
        data,
        trainer,
        level,
        move1,
        move2,
        move3,
        move4,
        Status.OK,
        hp,
        attack,
        defense,
        spAtk,
        spDef,
        speed,
        currentHP,
        0,
        shiny,
        PokemonBattleData(),
        null
    )

    companion object {
        fun of(pokemonSave: PokemonSave, gameDataService: GameDataService, trainer: Trainer): Pokemon {
            val data = gameDataService.getPokemonDataById(pokemonSave.id)
            return PokemonBuilder()
                .data(data)
                .level(pokemonSave.level)
                .trainer(trainer)
                .status(Status.valueOf(pokemonSave.status))
                .hp(StatUtils.computeHP(data, pokemonSave.level))
                .attack(StatUtils.computeStat(data, pokemonSave.level, Stats.ATTACK))
                .defense(StatUtils.computeStat(data, pokemonSave.level, Stats.DEFENSE))
                .spAtk(StatUtils.computeStat(data, pokemonSave.level, Stats.SPATK))
                .spDef(StatUtils.computeStat(data, pokemonSave.level, Stats.SPDEF))
                .speed(StatUtils.computeStat(data, pokemonSave.level, Stats.SPEED))
                .currentHP(pokemonSave.currentHP)
                .currentExp(pokemonSave.currentExp)
                .move1(PokemonMove(gameDataService.getMoveById(pokemonSave.moveids[0].id), pokemonSave.moveids[0].pp, 0))
                .move2(
                    if (pokemonSave.moveids.size > 1) PokemonMove(
                        gameDataService.getMoveById(pokemonSave.moveids[1].id),
                        pokemonSave.moveids[1].pp,
                        0
                    ) else null
                )
                .move3(
                    if (pokemonSave.moveids.size > 2) PokemonMove(
                        gameDataService.getMoveById(pokemonSave.moveids[2].id),
                        pokemonSave.moveids[2].pp,
                        0
                    ) else null
                )
                .move4(
                    if (pokemonSave.moveids.size > 3) PokemonMove(
                        gameDataService.getMoveById(pokemonSave.moveids[3].id),
                        pokemonSave.moveids[3].pp,
                        0
                    ) else null
                )
                .shiny(pokemonSave.shiny)
                .holdItem(
                    if (pokemonSave.holdItem == null || pokemonSave.holdItem == "") null else HoldItem.valueOf(
                        pokemonSave.holdItem!!
                    )
                )
                .isFromBanner(pokemonSave.isFromBanner)
                .movesLearnedByTM(ArrayList(pokemonSave.movesLearnedByTM.map { gameDataService.getMoveById(it) }))
                .build()
        }
    }

    fun canAttack(move: PokemonMove): AttackResponse {
        var statusChange = ""
        if (this.status == Status.FROZEN) {
            if (move.move.type != Type.FIRE && Random.nextInt(100) > 20)
                return AttackResponse(false, "${this.data.name} is frozen solid!\n")
            else {
                status = Status.OK
                statusChange = "${data.name} thawed out!\n"
            }
        }
        if (this.status == Status.PARALYSIS && this.battleData!!.haxxCounter < 3) {
            if (Random.nextInt(100) < 25) {
                this.battleData!!.haxxCounter += 1
                return AttackResponse(false, "${this.data.name} can't move because it is paralysed!\n")
            }
        }
        if (this.status == Status.ASLEEP) {
            if (battleData!!.sleepCounter == 3 || (hasAbility(Ability.EARLY_BIRD) && battleData!!.sleepCounter == 2)) {
                battleData!!.sleepCounter = 0
                this.status = Status.OK
                statusChange = "${data.name} woke up!\n"
            } else if (battleData!!.sleepCounter > 1) {
                if (Random.nextInt(100) < 30) {
                    battleData!!.sleepCounter = 0
                    this.status = Status.OK
                    statusChange = "${data.name} woke up!\n"
                } else
                    return AttackResponse(false, this.data.name + " is fast asleep!\n")
            } else
                return AttackResponse(false, this.data.name + " is fast asleep!\n")
        }
        if (this.battleData!!.battleStatus.contains(Status.UNABLE_TO_MOVE))
            return AttackResponse(false, "${this.data.name} needs to rest!\n")
        if (this.battleData!!.battleStatus.contains(Status.FLINCHED) && this.battleData!!.haxxCounter < 3) {
            this.battleData!!.haxxCounter += 1
            var reason = "${this.data.name} flinched and couldn't move\n"
            if (this.hasAbility(Ability.STEADFAST)) {
                reason += "${data.name}'s Steadfast: ${this.data.name}'s speed rose!\n"
                this.battleData!!.statsMultiplier.updateStat(StatChange.SPEED_ONE_LEVEL_RAISE)
            }
            return AttackResponse(false, reason)
        }
        if (this.battleData!!.battleStatus.contains(Status.CONFUSED) && this.battleData!!.haxxCounter < 3) {
            if (Random.nextInt(100) < 33) {
                this.battleData!!.haxxCounter += 1
                this.takeDamage(DamageCalculator.computeConfusionDamage(this))
                return AttackResponse(
                    false,
                    "But ${this.data.name} hits hurt itself in its confusion\n"
                )
            }
        }
        this.battleData!!.haxxCounter = 0
        return AttackResponse(true, statusChange)
    }

    fun attack(
        move: PokemonMove,
        opponent: Pokemon,
        battleField: BattleField,
        pokemonSide: BattleSide,
        opponentSide: BattleSide
    ): AttackResponse {
        val hadATurn = this.battleData!!.hadATurn
        this.battleData!!.hadATurn = true
        val attackResponse = canAttack(move)
        this.battleData!!.lastMoveFailed = true
        if (!attackResponse.success) {
            return attackResponse
        }
        var details = attackResponse.reason
        val moldBreaker = this.hasAbility(Ability.MOLD_BREAKER)
        if (move.move.category != MoveCategory.OTHER && opponent.hasAbility(Ability.PRESSURE) && opponent.currentHP > 0)
            move.pp = if (move.pp > 1) move.pp - 2 else 0
        else
            move.pp = if (move.pp == 0) 0 else move.pp - 1
        if (move.move.category == MoveCategory.OTHER && battleData!!.battleStatus.contains(Status.TAUNTED)) {
            return AttackResponse(
                false,
                "${this.data.name} can't use ${move.move.name} after the taunt!\n"
            )
        }
        details += "${this.data.name} uses ${move.move.name}!\n"
        if (opponent.currentHP == 0) {
            details += if (move.move is StatChangeMove
                && move.move.category == MoveCategory.OTHER
                && (move.move as StatChangeMove).target == Target.SELF) {
                Stats.updateStat(this, move.move as StatChangeMove, Target.SELF,false)
            } else if (move.move is BattleFieldSideMove) {
                if ((move.move as BattleFieldSideMove).target === Target.SELF) {
                    pokemonSide.addBattleSideEffect(
                        this,
                        opponent,
                        BattleSideEffect.moveNameToTeamEffect(move.move.name)
                    )
                } else {
                    opponentSide.addBattleSideEffect(
                        this,
                        opponent,
                        BattleSideEffect.moveNameToTeamEffect(move.move.name)
                    )
                }
            } else if (move.move is HealMove) {
                details += healMove(move)
            } else {
                "But there is no target!\n"
            }
            return AttackResponse(true, details)
        }
        if (move.disabledCountdown > 0) {
            details += "${move.move.name} can't be used!\n"
            return AttackResponse(false, details)
        }
        if (move.move.id == 296
            && ((this.battleData!!.lastMoveUsed != null && this.battleData!!.lastMoveUsed!!.move.id == move.move.id)
                    || opponent is PokemonBoss)) {
            return AttackResponse(
                false,
                details + "But it failed!\n"
            )
        }
        if (move.move.id == 19 && opponent is PokemonBoss) {
            return AttackResponse(
                false,
                details + "But ${opponent.data.name} didn't seem affected!\n"
            )
        }
        if (!moldBreaker) {
            if (move.move.characteristics.contains(MoveCharacteristic.SOUND) && opponent.hasAbility(Ability.SOUNDPROOF))
                return AttackResponse(
                    false,
                    details + "${opponent.data.name}'s Soundproof: It does not affect ${opponent.data.name}!\n"
                )
            if (move.move.type == Type.ELECTRIC) {
                if (opponent.hasAbility(Ability.LIGHTNING_ROD)) {
                    opponent.battleData!!.statsMultiplier.updateStat(StatChange.SPATK_ONE_LEVEL_RAISE)
                    return AttackResponse(
                        false,
                        details + "${opponent.data.name}'s Lightning Rod: ${opponent.data.name}'s Sp. Atk rose!\n"
                    )
                }
                if (opponent.hasAbility(Ability.MOTOR_DRIVE)) {
                    opponent.battleData!!.statsMultiplier.updateStat(StatChange.SPEED_ONE_LEVEL_RAISE)
                    return AttackResponse(
                        false,
                        details + "${opponent.data.name}'s Motor Drive: ${opponent.data.name}'s speed rose!\n"
                    )
                }
            }
            if (move.move.characteristics.contains(MoveCharacteristic.BULLET) && opponent.hasAbility(Ability.BULLETPROOF)) {
                return AttackResponse(
                    false,
                    details + "${opponent.data.name}'s Bulletproof: It does not affect ${opponent.data.name}\n"
                )
            }
            if (move.move is RecoilMove && (move.move as RecoilMove).recoil == Recoil.ALL && opponent.hasAbility(Ability.DAMP)) {
                return AttackResponse(
                    false,
                    details + "${opponent.data.name}'s Damp: ${this.data.name} cannot use ${move.move.name}!\n"
                )
            }
            if (move.move.type == Type.FIRE && opponent.hasAbility(Ability.FLASH_FIRE)) {
                if (!opponent.battleData!!.battleStatus.contains(Status.FIRED_UP))
                    opponent.battleData!!.battleStatus.add(Status.FIRED_UP)
                return AttackResponse(
                    false,
                    details + "${opponent.data.name}'s Flash Fire: ${opponent.data.name}'s fire power is increased!\n"
                )
            }
            if (move.move.type == Type.WATER && opponent.hasAbility(Ability.STORM_DRAIN)) {
                opponent.battleData!!.statsMultiplier.updateStat(StatChange.SPATK_ONE_LEVEL_RAISE)
                return AttackResponse(
                    false,
                    details + "${opponent.data.name}'s Storm Drain: ${opponent.data.name}'s Sp. Atk rose!\n"
                )
            }
            if (move.move.type == Type.GRASS && move.move.category != MoveCategory.OTHER && opponent.hasAbility(Ability.SAP_SIPPER)) {
                opponent.battleData!!.statsMultiplier.updateStat(StatChange.ATTACK_ONE_LEVEL_RAISE)
                return AttackResponse(
                    false,
                    details + "${opponent.data.name}'s Sap Sipper: ${opponent.data.name}'s attack rose!\n"
                )
            }
            if ((move.move.type == Type.WATER && (opponent.hasAbility(Ability.WATER_ABSORB) || opponent.hasAbility(
                    Ability.DRY_SKIN
                )))
                || (move.move.type == Type.ELECTRIC && opponent.hasAbility(Ability.VOLT_ABSORB))
            ) {
                opponent.heal(DamageCalculator.computeDamageWithoutAbility(this, move.move, opponent, battleField))
                return AttackResponse(
                    false,
                    details + "" + BattleUtils.getEffectiveness(
                        this,
                        move.move,
                        opponent,
                        battleField
                    )
                )
            }
            if (move.move.power > 0
                && move.move.type == Type.GROUND
                && opponent.hasAbility(Ability.LEVITATE)
                && battleField.gravityCounter == 0) {
                return AttackResponse(
                    false,
                    details + "${opponent.data.name}'s Levitate: It does not affect ${opponent.data.name}!\n"
                )
            }
        }
        if (move.move.power > 0
            && move.move.type == Type.GROUND
            && battleField.gravityCounter == 0) {
            if (opponent.hasItem(HoldItem.AIR_BALLOON)) {
                return AttackResponse(
                    false,
                    details + "${opponent.data.name}'s Air Balloon: It does not affect ${opponent.data.name}!\n"
                )
            }
            if (opponent.battleData!!.magnetRiseCounter > 0) {
                return AttackResponse(
                    false,
                    details + "It does not affect ${opponent.data.name}!\n"
                )
            }
        }
        if ((move.move.id == 210 || move.move.id == 244 || move.move.id == 283) && !this.hasAbility(Ability.SCRAPPY) && opponent.hasType(Type.GHOST) ) {
            this.takeDamage(this.hp / 2)
            return AttackResponse(
                false,
                details + "It does not affect ${opponent.data.name}!\n${this.data.name} kept going and crashed!\n"
            )
        }
        if (move.move is RetaliationMove && (this.battleData!!.lastHitReceived == null || move.move.category != this.battleData!!.lastHitReceived!!.category)) {
            return AttackResponse(false, details + "But it failed!\n")
        }
        if (((move.move.id == 246 || move.move.id == 282) && hadATurn) || (move.move.id == 213 && opponent.status != Status.ASLEEP))
            return AttackResponse(
                false,
                details + "But it failed!\n"
            )
        if (move.move.characteristics.contains(MoveCharacteristic.JUMP) && battleField.gravityCounter > 0) {
            return AttackResponse(
                false,
                details + "But it failed!\n"
            )
        }
        if (move.move.id == 331 && (opponent.heldItem == null || opponent.itemDisabled)) { //Poltergeist
            return AttackResponse(
                false,
                details + "But it failed!\n"
            )
        }
        if (move.move !is MoveBasedOnLevel && move.move.category != MoveCategory.OTHER && DamageCalculator.getEffectiveness(
                this,
                move.move,
                opponent,
                battleField
            ) == 0f
        )
            return AttackResponse(
                false,
                details + "It does not affect ${opponent.data.name}!\n"
            )
        if (move.move.accuracy != null && !this.hasAbility(Ability.NO_GUARD)) {
            val random: Int = if (battleField.weather == Weather.SNOW && move.move.id == 300) 1 else Random.nextInt(100)
            var accuracy = move.move.accuracy!! * battleData!!.statsMultiplier.accuracyMultiplicator
            if (opponent.hasAbility(Ability.WONDER_SKIN) && move.move.category == MoveCategory.OTHER && move.move.status.isNotEmpty())
                accuracy *= 0.5f
            if (battleField.gravityCounter > 0)
                accuracy *= 1.5f
            if (random > accuracy) {
                var reason = details + "${this.data.name}'s attack missed!\n"
                if (move.move.id == 210 || move.move.id == 244 || move.move.id == 283) {
                    reason += "${this.data.name} kept going and crashed!\n"
                    this.takeDamage(this.hp / 2)
                }
                return AttackResponse(
                    false,
                    reason
                )
            }
        }
        this.battleData!!.lastMoveFailed = false
        this.battleData!!.lastMoveUsed = move
        if (this.battleData!!.battleStatus.contains(Status.TORMENTED)) {
            move.disabledCountdown = 2
        }
        if (move.move is HealMove) {
            details += healMove(move)
        }
        if (move.move is BattleFieldSideMove) {
            if ((move.move as BattleFieldSideMove).target == Target.OPPONENT) {
                if (opponent.hasAbility(Ability.MAGIC_BOUNCE)) {
                    details += "${opponent.data.name}'s Magic Bounce: ${opponent.data.name} bounces the attack back!\n"
                    details += pokemonSide.addBattleSideEffect(opponent, this, BattleSideEffect.moveNameToTeamEffect(move.move.name))
                } else {
                    details += opponentSide.addBattleSideEffect(
                        this,
                        opponent,
                        BattleSideEffect.moveNameToTeamEffect(move.move.name)
                    )
                }
            } else {
                details += if (battleField.weather != Weather.SNOW && move.move.id == 323) {
                    "But it failed!\n"
                } else {
                    pokemonSide.addBattleSideEffect(this, opponent, BattleSideEffect.moveNameToTeamEffect(move.move.name))
                }
            }
        }
        if (move.move.id == 295)
            this.battleData!!.battleStatus.add(Status.CHARGED)
        if (move.move.id == 329)
            this.battleData!!.magnetRiseCounter = 5
        if (move.move.id == 330) {
            details += "A bell chimed!\n"
            this.status = Status.OK
            if (this.trainer != null) {
                this.trainer!!.getTrainerTeam().forEach { it.status = Status.OK }
            }
        }
        if (move.move is UltimateMove)
            this.battleData!!.battleStatus.add(Status.UNABLE_TO_MOVE)
        if (battleData!!.lastMoveUsed == move && !battleData!!.lastMoveFailed) {
            this.battleData!!.sameMoveCounter += 1
        } else {
            this.battleData!!.sameMoveCounter = 0
        }
        var damage = 0
        var crit = 1f
        if (move.move.power > 0) {
            if (move.move is MoveBasedOnLevel) {
                damage = this.level
            } else if (move.move is VariableHitMove) {
                val timesItHits = if (this.hasAbility(Ability.SKILL_LINK) || this.hasItem(HoldItem.LOADED_DICE)) 5 else Random.nextInt(2..5)
                var i = 0
                while (i < timesItHits && opponent.currentHP > damage) {
                    crit = DamageCalculator.getCriticalMultiplicator(this, move.move, opponent)
                    if (crit == 1.5f)
                        details += "A critical hit!\n"
                    details += BattleUtils.contactMovesCheck(this, move.move, opponent)
                    damage += DamageCalculator.computeDamage(
                        this,
                        move.move,
                        opponent,
                        crit,
                        battleField,
                        opponentSide
                    )
                    i++
                }
                opponent.battleData!!.numberOfHitTaken += i - 1
                details +=
                    if (i > 1) "${opponent.data.name} was hit $i times!\n" else "${opponent.data.name} was hit 1 time!\n"
            } else if (move.move is MultipleHitMove) {
                crit = DamageCalculator.getCriticalMultiplicator(this, move.move, opponent)
                if (crit == 1.5f)
                    details += "A critical hit!\n"
                damage += DamageCalculator.computeDamage(
                    this,
                    move.move,
                    opponent,
                    crit,
                    battleField,
                    opponentSide
                )
                details += BattleUtils.contactMovesCheck(this, move.move, opponent)
                if (damage >= opponent.currentHP) {
                    details += "${opponent.data.name} was hit 1 time!\n"
                } else {
                    DamageCalculator.getCriticalMultiplicator(this, move.move, opponent)
                    if (crit == 1.5f)
                        details += "A critical hit!\n"
                    details += BattleUtils.contactMovesCheck(this, move.move, opponent)
                    damage += DamageCalculator.computeDamage(
                        this,
                        move.move,
                        opponent,
                        crit,
                        battleField,
                        opponentSide
                    )
                    opponent.battleData!!.numberOfHitTaken += 1
                    details += "${opponent.data.name} was hit 2 times!\n"
                }
            } else {
                crit = DamageCalculator.getCriticalMultiplicator(this, move.move, opponent)
                if (crit == 1.5f)
                    details += "A critical hit!\n"
                details += BattleUtils.contactMovesCheck(this, move.move, opponent)
                damage = DamageCalculator.computeDamage(
                    this,
                    move.move,
                    opponent,
                    crit,
                    battleField,
                    opponentSide
                )
            }
        }
        if (move.move.characteristics.contains(MoveCharacteristic.WIND) && opponent.hasAbility(Ability.WIND_POWER)) {
            details += "${opponent.data.name}'s Wind Power: ${opponent.data.name} is charged with power!\n"
            opponent.battleData!!.battleStatus.add(Status.CHARGED)
        }
        if (move.move.power > 1 && move.move !is RetaliationMove)
            details += BattleUtils.getEffectiveness(this, move.move, opponent, battleField)
        val damageDone: Int
        if (damage >= opponent.currentHP) {
            if (opponent.currentHP == opponent.hp
                && move.move !is MultipleHitMove
                && move.move !is VariableHitMove
                && (opponent.hasAbility(Ability.STURDY) || opponent.hasItem(HoldItem.FOCUS_SASH))
            ) {
                damageDone = opponent.currentHP - 1
                opponent.currentHP = 1
                if (opponent.hasAbility(Ability.STURDY))
                    details += "${opponent.data.name}'s Sturdy: ${opponent.data.name} endured the hit!\n"
                else {
                    details += "${opponent.data.name} hung on thanks to his Focus Sash!\n"
                    opponent.consumeItem()
                }
            } else {
                damageDone = opponent.currentHP
                opponent.currentHP = 0
                opponent.status = Status.OK
                if (opponent.hasAbility(Ability.AFTERMATH)
                    && !this.hasItem(HoldItem.PROTECTIVE_PADS)
                    && move.move.characteristics.contains(MoveCharacteristic.CONTACT)) {
                    details += "${opponent.data.name}'s Aftermath: ${this.data.name} loses some hp!\n"
                    this.takeDamage(hp / 4)
                }
            }
        } else {
            damageDone = damage
            opponent.takeDamage(damage)
            if (damage > 0 && opponent.hasItem(HoldItem.AIR_BALLOON)) {
                details += "${opponent.data.name}'s Air Balloon popped out!\n"
                opponent.consumeItem()
            }
            if (crit == 1.5f && opponent.hasAbility(Ability.ANGER_POINT)) {
                details += "${opponent.data.name}'s Anger Point: ${opponent.data.name} maxed its Attack!\n"
                opponent.battleData!!.statsMultiplier.attackMultiplicator = 4f
            }
            if (move.move.type == Type.FIRE && opponent.status == Status.FROZEN) {
                opponent.status = Status.OK
                details += "${opponent.data.name} thawed out!\n"
            }
            if (this.hasAbility(Ability.STENCH) && Random.nextInt(10) < 2 && !opponent.hasAbility(Ability.INNER_FOCUS))
                    opponent.battleData!!.battleStatus.add(Status.FLINCHED)
            if (move.move.status.isNotEmpty()) {
                if (!this.hasAbility(Ability.SHEER_FORCE) && !opponent.hasAbility(Ability.SHIELD_DUST) && damage > 0) {
                    details += Status.updateStatus(this, opponent, move.move, pokemonSide, opponentSide)
                } else if (move.move.category == MoveCategory.OTHER) {
                    if (opponent.hasAbility(Ability.MAGIC_BOUNCE) && !moldBreaker) {
                        details += "${opponent.data.name}'s Magic Bounce: ${opponent.data.name} bounces the attack back!\n"
                        details += Status.updateStatus(opponent, this, move.move, opponentSide, pokemonSide)
                    } else
                        details += Status.updateStatus(this, opponent, move.move, pokemonSide, opponentSide)
                }
            }
        }
        if ((!this.hasAbility(Ability.SHEER_FORCE) || move.move.category == MoveCategory.OTHER) && move.move is StatChangeMove
            && !opponent.hasAbility(Ability.SHIELD_DUST) && (move.move.power == 0 || damage > 0)) {
            var randomForStats: Int = Random.nextInt(100)
            if (this.hasAbility(Ability.SERENE_GRACE))
                randomForStats /= 2
            if ((move.move as StatChangeMove).probability == null || randomForStats <= (move.move as StatChangeMove).probability!!) {
                val statChangeMove = move.move as StatChangeMove
                details +=
                    if (statChangeMove.target == Target.SELF)
                        Stats.updateStat(this, statChangeMove, Target.SELF, false)
                    else {
                        if (move.move.category == MoveCategory.OTHER && opponent.hasAbility(Ability.MAGIC_BOUNCE) && !moldBreaker) {
                            "${opponent.data.name}'s Magic Bounce: ${opponent.data.name} bounced the attack back!\n" + Stats.updateStat(
                                this,
                                statChangeMove,
                                Target.OPPONENT,
                                false
                            )
                        } else if (opponent.currentHP > 0) {
                            if (move.move.id == 302) {
                                this.heal((opponent.attack * opponent.battleData!!.statsMultiplier.attackMultiplicator).toInt())
                                "The opposing ${opponent.data.name} had its energy drained!\n" + Stats.updateStat(
                                    opponent,
                                    statChangeMove,
                                    Target.OPPONENT,
                                    false
                                )
                            } else {
                                Stats.updateStat(opponent, statChangeMove, Target.OPPONENT, moldBreaker)
                            }
                        } else ""
                    }
            }
        }
        if (damage > 0 && move.move.category != MoveCategory.OTHER) {
            opponent.battleData!!.numberOfHitTaken += 1
            opponent.battleData!!.lastHitReceived = LastHitReceived(damage, move.move.category)
        }
        if (opponent.currentHP > 0 && move.move.type == Type.DARK && move.move.category != MoveCategory.OTHER && opponent.hasAbility(
                Ability.JUSTIFIED
            )
        ) {
            details += "${opponent.data.name}'s Justified: ${opponent.data.name}'s attack rose!\n"
            if (opponent.battleData!!.statsMultiplier.attackMultiplicator > 4)
                details += "But ${opponent.data.name}'s attack cannot go higher!\n"
            else
                this.battleData!!.statsMultiplier.updateStat(StatChange.ATTACK_ONE_LEVEL_RAISE)
        }
        if (move.move is DrainMove) {
            var drainedDamage : Float = if (move.move.id == 238) damageDone * 0.75f else damageDone * 0.5f
            if (hasItem(HoldItem.BIG_ROOT))
                drainedDamage *= 1.3f
            details += if (opponent.hasAbility(Ability.LIQUID_OOZE)) {
                this.takeDamage(drainedDamage.toInt())
                "${opponent.data.name}'s Liquid Ooze: ${this.data.name} loses some hp.\n"
            } else {
                this.heal(drainedDamage.toInt())
                "The opposing ${opponent.data.name} had its energy drained!\n"
            }
        }
        if (move.move is RecoilMove) {
            if ((move.move as RecoilMove).recoil == Recoil.ALL
                || (move.move as RecoilMove).recoil == Recoil.FINAL_GAMBIT) {
                this.currentHP = 0
                if (move.move.id == 337) {
                    details += pokemonSide.addBattleSideEffect(this, opponent, BattleSideEffect.HEALING_WISH)
                }
            } else if (damageDone > 0) {
                details += if (!this.hasAbility(Ability.ROCK_HEAD)) {
                    this.takeDamage(1.coerceAtLeast((damageDone * (move.move as RecoilMove).recoil.damage).toInt()))
                    "${this.data.name} is damaged by recoil!\n"
                } else {
                    "${this.data.name}'s Rock Head: ${this.data.name} does not receive recoil damage!\n"
                }
            }
        }
        if (move.move is RemoveStatChangesMove) {
            if (opponent is PokemonBoss)
                details += "${opponent.data.name}'s stats changes can't be removed!\n"
            else if (damage > 0 || move.move.category == MoveCategory.OTHER) {
                if (move.move.category == MoveCategory.OTHER) {
                    details += "All stats changes were removed!\n"
                    this.recomputeStatMultiplier(battleField.weather, pokemonSide)
                } else {
                    details += "${opponent.data.name}'s stats changes were removed!\n"
                }
                opponent.recomputeStatMultiplier(battleField.weather, pokemonSide)
            }
        }
        if (move.move.id == 247 && opponent.heldItem != null) {
            if (!moldBreaker && opponent.hasAbility(Ability.STICKY_HOLD)) {
                details += "${opponent.data.name}'s Sticky Hold: ${opponent.data.name}'s item cannot be removed!\n"
            } else {
                details += "${this.data.name} knocked off ${opponent.data.name}'s item!\n"
                opponent.itemDisabled = true
            }
        }
        if (damage > 0) {
            if (currentHP > 0 && move.move.characteristics.contains(MoveCharacteristic.SOUND) && this.hasItem(HoldItem.THROAT_SPRAY)) {
                this.battleData!!.statsMultiplier.updateStat(StatChange.SPATK_ONE_LEVEL_RAISE)
                details += "The Throat Spray raised ${this.data.name}'s Sp. Atk!\n"
            }
            if (move.move.id == 249) {
                if (this.battleData!!.battleStatus.contains(Status.LEECH_SEEDED)) {
                    this.battleData!!.battleStatus.remove(Status.LEECH_SEEDED)
                    details += "${this.data.name} is no longer affected by Leech Seed!\n"
                }
                if (this.battleData!!.battleStatus.contains(Status.TRAPPED_WITH_DAMAGE)) {
                    this.battleData!!.battleStatus.remove(Status.TRAPPED_WITH_DAMAGE)
                    details += "${this.data.name} is no longer trapped!\n"
                }
                if (pokemonSide.battleSideEffects.remove(BattleSideEffect.STICKY_WEB)) {
                    details += "The sticky web around ${this.data.name} disappeared!\n"
                }
            }
            if (opponent.currentHP > 0 && opponent.hasAbility(Ability.WEAK_ARMOR) && move.move.category == MoveCategory.PHYSICAL) {
                opponent.battleData!!.statsMultiplier.updateStat(StatChange.DEFENSE_ONE_LEVEL_DECREASE)
                opponent.battleData!!.statsMultiplier.updateStat(StatChange.SPEED_TWO_LEVEL_RAISE)
                details += "${opponent.data.name}'s Weak Armor: ${opponent.data.name}'s Defense fell\n${opponent.data.name}'s Speed rose sharply!\n"
            }
            if (opponent.hasAbility(Ability.RATTLED)
                && (move.move.type == Type.GHOST || move.move.type == Type.DARK || move.move.type == Type.BUG)) {
                opponent.battleData!!.statsMultiplier.updateStat(StatChange.SPEED_ONE_LEVEL_RAISE)
                details += "${opponent.data.name}'s Rattled: ${opponent.data.name}'s Speed rose!\n"
            }
            if (opponent.hasAbility(Ability.CURSED_BODY) && Random.nextInt(100) < 30) {
                details += "${opponent.data.name}'s Cursed Body: ${this.data.name}'s ${move.move.name} is disabled!\n"
                move.disabledCountdown = 4
            }
            if (opponent.currentHP > 0 && opponent.hasAbility(Ability.ELECTROMORPHOSIS)) {
                details += "${opponent.data.name}'s Electromorphosis: ${move.move.name} charged ${opponent.data.name}!\n"
                this.battleData!!.battleStatus.add(Status.CHARGED)
            }
            if (move.move.id == 69) {//Brick Break
                if (opponentSide.battleSideEffects.remove(BattleSideEffect.LIGHT_SCREEN)) {
                    details += "${opponent.data.name}'s team Light Screen wore off!\n"
                }
                if (opponentSide.battleSideEffects.remove(BattleSideEffect.REFLECT)) {
                    details += "${opponent.data.name}'s team Reflect wore off!\n"
                }
                if (opponentSide.battleSideEffects.remove(BattleSideEffect.AURORA_VEIL)) {
                    details += "${opponent.data.name}'s team Aurora Veil wore off!\n"
                }
            }
            if (move.move.id == 310) {
                //GIGATON HAMMER
                move.disabledCountdown = 2
            }
        }
        if (move.move.id == 186 && !opponent.isMegaEvolved) {
            this.battleData!!.transformData = TransformData.transform(this, opponent)
            details += "${data.name} transformed into ${opponent.data.name}!\n"
        }
        if (move.move.id == 338) {
            details += "${data.name} copied ${opponent.data.name}'s type!\n"
            battleData!!.battleType1 = opponent.getBattleType1()
            battleData!!.battleType1 = opponent.getBattleType2()
        }
        if (move.move.id == 339) {
            if (opponent.hasType(Type.WATER)) {
                details += "But it failed!\n"
            } else {
                opponent.battleData!!.battleType1 = Type.WATER
                details += "${opponent.data.name} transformed into a Water type!\n"
            }
        }
        if (move.move.id == 314 || move.move.id == 315) {
            when {
                opponent.hasAbility(Ability.AIR_LOCK) -> {
                    details += "${opponent.data.name}'s Air Lock: ${opponent.data.name} made the weather disappeared\n"
                }
                opponent.hasAbility(Ability.CLOUD_NINE) -> {
                    details += "${opponent.data.name}'s Cloud Nine: ${opponent.data.name} made the weather disappeared\n"
                }
                move.move.id == 314 -> {
                    details += "A sandstorm kicked up!\n"
                    battleField.setWeather(this, Weather.SANDSTORM, opponent)
                }
                move.move.id == 315 -> {
                    details += "It started to snow!\n"
                    battleField.setWeather(this, Weather.SNOW, opponent)
                }
            }
        }
        if (move.move.id == 324) {
            battleField.trickRoomCounter = 5
            details += "${this.data.name} twisted the dimensions!\n"
        }
        if (move.move.id == 327) {
            battleField.gravityCounter = 5
            details += "Gravity intensified!\n"
        }
        if (move.move.id == 265) { //DISABLE
            if (opponent.battleData!!.lastMoveUsed != null) {
                opponent.battleData!!.lastMoveUsed!!.disabledCountdown = 4
                details += "${opponent.data.name}'s ${opponent.battleData!!.lastMoveUsed!!.move.name} is disabled!\n"
            } else{
                details += "But it failed\n"
            }
        }
        if (move.move.id == 311) { //IMPRISON
            details += "${this.data.name} sealed any moves its target shares with it!\n"
            val attackerMoves = MoveUtils.getMoveList(this).map { it.move }
            val opponentMoves = MoveUtils.getMoveList(opponent)
            opponentMoves.forEach {
                if (attackerMoves.contains(it.move)) {
                    it.disabledCountdown = 4
                }
            }
        }
        if (opponent.currentHP > 0
            && damageDone > 0
            && move.move !is MoveBasedOnLevel && move.move !is RetaliationMove
            && BattleUtils.getEffectiveness(this, move.move, opponent, battleField).contains("super")
            && opponent.hasItem(HoldItem.WEAKNESS_POLICY)) {
                opponent.battleData!!.statsMultiplier.updateStat(StatChange.ATTACK_TWO_LEVEL_RAISE)
                opponent.battleData!!.statsMultiplier.updateStat(StatChange.SPATK_TWO_LEVEL_RAISE)
            opponent.consumeItem()
            details += "${opponent.data.name}'s Attack and Sp. Atk rose thanks to its Weakness Policy!\n"
        }

        if (this.hasAbility(Ability.PARENTAL_BOND) && !this.battleData!!.child) {
            if (opponent.currentHP == 0) {
                details += "${opponent.data.name} was hit 1 time!\n"
            } else {
                this.battleData!!.child = true
                details += this.attack(move, opponent, battleField, pokemonSide, opponentSide).reason
                this.battleData!!.child = false
                details += "${opponent.data.name} was hit 2 times!\n"
            }
        }
        if (currentHP > 0
            && damage > 0
            && move.move.category != MoveCategory.OTHER
            && this.hasItem(HoldItem.LIFE_ORB)
            && !this.hasAbility(Ability.SHEER_FORCE)) {
            this.takeDamage(this.hp / 10)
            details += this.data.name + " lost some of its hp!\n"
        }

        if (opponent.currentHP == 0 && this.currentHP > 0) {
            if (battleData!!.battleStatus.contains(Status.BOUNDED)) {
                this.currentHP = 0
                details += "${opponent.data.name} took down his opponent with it!\n"
            }
            else if (opponent.currentHP == 0 && this.hasAbility(Ability.MOXIE)) {
                this.battleData!!.statsMultiplier.updateStat(StatChange.ATTACK_ONE_LEVEL_RAISE)
                details += "${this.data.name}'s Moxie: ${this.data.name}'s attack rose!\n"
            } else if (opponent.currentHP == 0 && this.hasAbility(Ability.SOUL_HEART)) {
                this.battleData!!.statsMultiplier.updateStat(StatChange.SPATK_ONE_LEVEL_RAISE)
                details += "${this.data.name}'s Soul Heart: ${this.data.name}'s sp. Atk rose!\n"
            }
        }

        return AttackResponse(true, details)
    }

    private fun healMove(move: PokemonMove) : String {
        if (this.currentHP == this.hp) {
            return "But ${this.data.name}'s HP is full!\n"
        }
        var details = "${this.data.name}'s HP was restored!\n"
        if (move.move.id == 193) {//ROOST
            this.battleData!!.battleStatus.add(Status.ROOSTED)
        }
        if (move.move.id == 275) {//REST
            this.status = Status.ASLEEP
            details += "${this.data.name} fell asleep!\n"
            this.currentHP = hp
        }
        else
            HealMove.heal(this)
        return details
    }

    private fun recomputeStatMultiplier(weather: Weather, battleSide: BattleSide) {
        this.battleData!!.statsMultiplier = StatsMultiplier()
        if (this.hasItem(HoldItem.ASSAULT_VEST)  && !MoveUtils.getMoveList(this).map{it.move.category}.contains(MoveCategory.OTHER)) {
            this.battleData!!.statsMultiplier.updateStat(StatChange.SPDEF_ONE_LEVEL_RAISE)
        }
        if (this.hasItem(HoldItem.EVIOLITE) && this.data.evolutions.isNotEmpty()) {
            this.battleData!!.statsMultiplier.updateStat(StatChange.DEFENSE_ONE_LEVEL_RAISE)
            this.battleData!!.statsMultiplier.updateStat(StatChange.SPDEF_ONE_LEVEL_RAISE)
        }
        if (this.hasItem(HoldItem.WIDE_LENS)) {
            this.battleData!!.statsMultiplier.updateStat(StatChange.ACCURACY_ONE_LEVEL_RAISE)
        }
        if (weather == Weather.SANDSTORM) {
            if (this.hasType(Type.ROCK))
                this.battleData!!.statsMultiplier.updateStat(StatChange.SPDEF_ONE_LEVEL_RAISE)
            if (this.hasAbility(Ability.SAND_RUSH))
                this.battleData!!.statsMultiplier.updateStat(StatChange.SPEED_TWO_LEVEL_RAISE)
        }
        if (weather == Weather.SNOW && this.hasType(Type.ICE)) {
            this.battleData!!.statsMultiplier.updateStat(StatChange.DEFENSE_ONE_LEVEL_RAISE)
        }
        if (battleSide.battleSideEffects.contains(BattleSideEffect.TAILWIND)) {
            this.battleData!!.statsMultiplier.updateStat(StatChange.SPEED_ONE_LEVEL_RAISE)
        }
        if (this.status != Status.OK) {
            BattleUtils.checkForStatusStatsRaiseAbility(this)
        }
    }

    fun canMegaEvolve(): Boolean {
        return data.megaEvolutionData != null && battleData != null && !isMegaEvolved && (this is PokemonBoss || (trainer != null
                && trainer!!.getTrainerTeam().none { it.isMegaEvolved } && (trainer is OpponentTrainer ||
                (heldItem == null && (trainer as Trainer).items.containsKey(MEGA_RING_ID) && (trainer as Trainer).items.containsKey(
                    this.data.megaEvolutionData!!.stoneId
                )))))
    }

    fun megaEvolve() {
        if (data.megaEvolutionData != null && !isMegaEvolved) {
            this.attack = StatUtils.computeMegaEvolutionStat(data.megaEvolutionData!!, level, Stats.ATTACK)
            this.defense = StatUtils.computeMegaEvolutionStat(data.megaEvolutionData!!, level, Stats.DEFENSE)
            this.spAtk = StatUtils.computeMegaEvolutionStat(data.megaEvolutionData!!, level, Stats.SPATK)
            this.spDef = StatUtils.computeMegaEvolutionStat(data.megaEvolutionData!!, level, Stats.SPDEF)
            this.speed = StatUtils.computeMegaEvolutionStat(data.megaEvolutionData!!, level, Stats.SPEED)
            isMegaEvolved = true
        }
    }

    fun recomputeStat() {
        isMegaEvolved = false
        val addHP: Boolean = hp == currentHP
        this.hp = StatUtils.computeHP(data, level)
        this.attack = StatUtils.computeStat(data, level, Stats.ATTACK)
        this.defense = StatUtils.computeStat(data, level, Stats.DEFENSE)
        this.spAtk = StatUtils.computeStat(data, level, Stats.SPATK)
        this.spDef = StatUtils.computeStat(data, level, Stats.SPDEF)
        this.speed = StatUtils.computeStat(data, level, Stats.SPEED)
        if (addHP)
            currentHP = hp
        removeDisableCountdown()
        itemDisabled = false
    }

    fun removeDisableCountdown() {
        this.move1.disabledCountdown = 0
        if (this.move2 != null)
            this.move2!!.disabledCountdown = 0
        if (this.move3 != null)
            this.move3!!.disabledCountdown = 0
        if (this.move4 != null)
            this.move4!!.disabledCountdown = 0
    }

    fun learnMove(moveToLearn: Move, moveToDeleteNumber: Int) {
        when (moveToDeleteNumber) {
            1 -> move1 = PokemonMove(moveToLearn)
            2 -> move2 = PokemonMove(moveToLearn)
            3 -> move3 = PokemonMove(moveToLearn)
            4 -> move4 = PokemonMove(moveToLearn)
        }
    }

    fun autoLearnMove(move: Move): Boolean {
        if (MoveUtils.getMoveList(this).map { it.move }.contains(move))
            return false
        if (move2 == null) {
            this.move2 = PokemonMove(move, move.pp, 0)
            return true
        }
        if (move3 == null) {
            this.move3 = PokemonMove(move, move.pp, 0)
            return true
        }
        if (move4 == null) {
            this.move4 = PokemonMove(move, move.pp, 0)
            return true
        }
        return false
    }

    fun gainLevel() {
        this.level += 1
        (this.trainer!! as Trainer).coins += 10
        recomputeStat()
        val moveFiltered = data.movesByLevel.filter { it.level == this.level }
        if (moveFiltered.size == 1)
            autoLearnMove(data.movesByLevel.first { it.level == this.level }.move)
    }

    fun gainExp(value: Int) {
        val maxLevel = (trainer!! as Trainer).getMaxLevel()
        if (this.level >= maxLevel)
            return
        var exp = value

        while (this.currentExp + exp >= ExpGaugeType.getExpToNextLevel(this)) {
            exp -= ExpGaugeType.getExpToNextLevel(this) - this.currentExp
            gainLevel()
            if (level == maxLevel)
                return
        }
        this.currentExp += exp
    }

    private fun meetsEvolutionCondition(evolution: Evolution): Boolean {
        val condition = evolution.evolutionCondition
        if (condition.level != null && level >= condition.level!!) {
            if (condition.dayTime != null) {
                val rightNow = Calendar.getInstance()
                val currentHourIn24Format: Int = rightNow.get(Calendar.HOUR_OF_DAY)
                if (condition.dayTime == "DAY")
                    return currentHourIn24Format in 6..18
                if (condition.dayTime == "NIGHT")
                    return currentHourIn24Format in 19..24 || currentHourIn24Format in 0..6
            }
            return true
        }
        if (condition.itemId != null && (this.trainer!! as Trainer).items.containsKey(condition.itemId))
            return true
        if (condition.moveId != null
            && MoveUtils.getMoveList(this).map { it.move.id }.contains(condition.moveId)
        )
            return true
        return false
    }

    fun canEvolve(): Boolean {
        if (data.evolutions.isEmpty())
            return false
        return if (data.evolutions.size == 1) {
            meetsEvolutionCondition(data.evolutions[0])
        } else {
            data.evolutions.any { meetsEvolutionCondition(it) }
        }
    }

    fun getPossibleEvolutions(): List<Int> {
        return data.evolutions.filter { meetsEvolutionCondition(it) }.map { it.evolutionId }
    }

    fun evolve(gameDataService: GameDataService, evolutionId: Int) {
        if (canEvolve()) {
            val evolution = data.evolutions.first { it.evolutionId == evolutionId }
            if (evolution.evolutionCondition.itemId != null) {
                (this.trainer!! as Trainer).useItem(evolution.evolutionCondition.itemId!!, this)
            }
            this.data = gameDataService.getPokemonDataById(evolutionId)
            (this.trainer!! as Trainer).pokedex[this.data.id] = true
            recomputeStat()
            if (currentHP > hp)
                currentHP = hp
            val moveFiltered = data.movesByLevel.filter { it.level == this.level }
            if (moveFiltered.size == 1)
                autoLearnMove(data.movesByLevel.first { it.level == this.level }.move)
        }
    }

    fun getPokemonData(): PokemonData {
        if (battleData != null && battleData!!.transformData != null) {
            return battleData!!.transformData!!.pokemonData
        }
        return data
    }

    fun getBattleMove1() : PokemonMove {
        if (battleData != null && battleData!!.transformData != null) {
            return battleData!!.transformData!!.move1
        }
        return move1
    }

    fun getBattleMove2() : PokemonMove? {
        if (battleData != null && battleData!!.transformData != null) {
            return battleData!!.transformData!!.move2
        }
        return move2
    }

    fun getBattleMove3() : PokemonMove? {
        if (battleData != null && battleData!!.transformData != null) {
            return battleData!!.transformData!!.move3
        }
        return move3
    }

    fun getBattleMove4() : PokemonMove? {
        if (battleData != null && battleData!!.transformData != null) {
            return battleData!!.transformData!!.move4
        }
        return move4
    }

    fun hasAbility(ability: Ability): Boolean {
        return (if (isMegaEvolved) data.megaEvolutionData!!.ability == ability
        else data.abilities.contains(ability)) || (battleData != null && battleData!!.abilities.contains(ability))
    }

    fun hasItem(item: HoldItem): Boolean {
        if (this.battleData != null && this.itemDisabled) {
            return false
        }
        return heldItem != null && heldItem == item
    }

    fun consumeItem() {
        if (this.battleData != null) {
            this.itemDisabled = true
            if (this.hasAbility(Ability.UNBURDEN)) {
                this.battleData!!.statsMultiplier.updateStat(StatChange.SPEED_TWO_LEVEL_RAISE)
            }
        }
    }

    fun getBattleType1(): Type {
        if (battleData != null) {
            if (battleData!!.battleType1 != null) {
                return battleData!!.battleType1!!
            }
            if (battleData!!.transformData != null) {
                return battleData!!.transformData!!.pokemonData.type1
            }
        }
        if (isMegaEvolved) {
            return data.megaEvolutionData!!.type1
        }
        return data.type1
    }

    fun getBattleType2(): Type {
        if (battleData != null) {
            if (battleData!!.battleType2 != null) {
                return battleData!!.battleType2!!
            }
            if (battleData!!.transformData != null) {
                return battleData!!.transformData!!.pokemonData.type2
            }
        }
        if (isMegaEvolved) {
            return data.megaEvolutionData!!.type2
        }
        return data.type2
    }

    fun hasType(type: Type): Boolean {
        if (battleData != null) {
            if (battleData!!.battleType1 != null) {
                return battleData!!.battleType1 == type || battleData!!.battleType2 == type
            }
            if (battleData!!.transformData != null) {
                return battleData!!.transformData!!.pokemonData.type1 == type
                        || battleData!!.transformData!!.pokemonData.type2 == type
            }
        }
        if (isMegaEvolved) {
            return data.megaEvolutionData!!.type1 == type || data.megaEvolutionData!!.type2 == type
        }
        return data.type1 == type || data.type2 == type
    }

    fun takeDamage(damage: Int) {
        var damageDone = damage
        if (this.currentHP == this.hp && this.hasAbility(Ability.MULTISCALE))
            damageDone /= 2
        if (this.currentHP > damageDone)
            this.currentHP -= damageDone
        else {
            currentHP = 0
            status = Status.OK
        }
    }

    fun heal(quantity: Int) {
        if (this.currentHP + quantity > this.hp)
            this.currentHP = this.hp
        else
            currentHP += quantity
    }

    fun canBeKOdBy(opponent: Pokemon, battleField: BattleField, battleSide: BattleSide): Boolean {
        val offensiveMove =
            MoveUtils.getUsableMoves(opponent).filter { it.move.power > 0 && it.move !is ChargedMove }
        for (move in offensiveMove) {
            var damage: Int = DamageCalculator.computeDamage(opponent, move.move, this, 1f, battleField, battleSide)
            if (move.move is VariableHitMove) {
                damage *= if (opponent.hasAbility(Ability.SKILL_LINK) || opponent.hasItem(HoldItem.LOADED_DICE)) 5 else 3
            } else if (move.move is MultipleHitMove) {
                damage *= 2
            } else if (this.currentHP == this.hp
                && (this.hasAbility(Ability.STURDY) || this.hasItem(HoldItem.FOCUS_SASH))) {
                return false
            }
            if (damage >= this.currentHP)
                return true
        }
        return false
    }

    data class PokemonBuilder(
        var data: PokemonData? = null,
        var trainer: Trainer? = null,
        var level: Int = 1,
        var move1: PokemonMove? = null,
        var move2: PokemonMove? = null,
        var move3: PokemonMove? = null,
        var move4: PokemonMove? = null,
        var status: Status = Status.OK,
        var hp: Int = 0,
        var attack: Int = 0,
        var defense: Int = 0,
        var spAtk: Int = 0,
        var spDef: Int = 0,
        var speed: Int = 0,
        var currentHP: Int = 0,
        var currentExp: Int = 0,
        var shiny: Boolean = false,
        var isFromBanner: Boolean = false,
        var movesLearnedByTM: ArrayList<Move> = arrayListOf(),
        var holdItem: HoldItem? = null,
    ) {
        fun data(data: PokemonData) = apply { this.data = data }
        fun trainer(trainer: Trainer) = apply { this.trainer = trainer }
        fun level(level: Int) = apply { this.level = level }
        fun status(status: Status) = apply { this.status = status }
        fun move1(move: PokemonMove) = apply { this.move1 = move }
        fun move2(move: PokemonMove?) = apply { this.move2 = move }
        fun move3(move: PokemonMove?) = apply { this.move3 = move }
        fun move4(move: PokemonMove?) = apply { this.move4 = move }
        fun hp(hp: Int) = apply { this.hp = hp }
        fun attack(attack: Int) = apply { this.attack = attack }
        fun defense(defense: Int) = apply { this.defense = defense }
        fun spAtk(spAtk: Int) = apply { this.spAtk = spAtk }
        fun spDef(spDef: Int) = apply { this.spDef = spDef }
        fun speed(speed: Int) = apply { this.speed = speed }
        fun currentHP(currentHP: Int) = apply { this.currentHP = currentHP }
        fun currentExp(currentExp: Int) = apply { this.currentExp = currentExp }
        fun shiny(shiny: Boolean) = apply { this.shiny = shiny }
        fun isFromBanner(isFromBanner: Boolean) = apply { this.isFromBanner = isFromBanner }
        fun movesLearnedByTM(movesLearnedByTM: ArrayList<Move>) = apply { this.movesLearnedByTM = movesLearnedByTM }
        fun holdItem(holdItem: HoldItem?) = apply { this.holdItem = holdItem }
        fun build() = Pokemon(
            data!!,
            trainer,
            level,
            move1!!,
            move2,
            move3,
            move4,
            status,
            hp,
            attack,
            defense,
            spAtk,
            spDef,
            speed,
            currentHP,
            currentExp,
            shiny,
            PokemonBattleData(),
            holdItem,
            isFromBanner,
            movesLearnedByTM
        )

    }
}
