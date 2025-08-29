package com.pokemon.android.version.model.battle

import com.pokemon.android.version.model.Ability
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.model.item.HoldItem
import com.pokemon.android.version.model.move.*
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

class DamageCalculator {
    companion object {
        private val TYPE_ITEM_BOOST = mapOf(
            Type.GRASS to HoldItem.MIRACLE_SEED,
            Type.FIRE to HoldItem.CHARCOAL,
            Type.WATER to HoldItem.MYSTIC_WATER,
            Type.ELECTRIC to HoldItem.MAGNET,
            Type.NORMAL to HoldItem.SILK_SCARF,
            Type.FLYING to HoldItem.SHARP_BEAK,
            Type.BUG to HoldItem.SILVER_POWDER,
            Type.POISON to HoldItem.POISON_BARB,
            Type.ROCK to HoldItem.HARD_STONE,
            Type.GROUND to HoldItem.SOFT_SAND,
            Type.FIGHTING to HoldItem.BLACK_BELT,
            Type.PSYCHIC to HoldItem.TWISTED_SPOON,
            Type.GHOST to HoldItem.SPELL_TAG,
            Type.ICE to HoldItem.NEVER_MELT_ICE,
            Type.DRAGON to HoldItem.DRAGON_FANG,
            Type.DARK to HoldItem.BLACK_GLASSES,
        )

        fun getCriticalMultiplicator(attacker: Pokemon, move: Move, opponent: Pokemon): Float {
            if (opponent.hasAbility(Ability.BATTLE_ARMOR) || opponent.hasAbility(Ability.SHELL_ARMOR))
                return 1f
            val random = Random.nextInt(100)
            if (move.highCritRate) {
                if (random < 13 * attacker.battleData!!.statsMultiplier.criticalRate)
                    return if (attacker.hasAbility(Ability.SNIPER)) 2.25f else 1.5f
            } else if (random < 6 * attacker.battleData!!.statsMultiplier.criticalRate)
                return if (attacker.hasAbility(Ability.SNIPER)) 2.25f else 1.5f
            return 1f
        }

        fun getEffectiveness(attacker: Pokemon, move: Move, opponent: Pokemon, battleField: BattleField): Float {
            var type1 = opponent.getBattleType1()
            var type2 = opponent.getBattleType2()
            var moveType = move.type
            if (move.id == 316) {
                if (battleField.weather == Weather.SANDSTORM) {
                    moveType = Type.ROCK
                } else if (battleField.weather == Weather.SNOW) {
                    moveType = Type.ICE
                }
            }
            if (moveType == Type.NORMAL && attacker.hasAbility(Ability.AERILATE)) {
                moveType = Type.FLYING
            }
            if (moveType == Type.NORMAL && attacker.hasAbility(Ability.PIXILATE)) {
                moveType = Type.FAIRY
            }
            if (moveType == Type.GROUND) {
                if (opponent.battleData!!.battleStatus.contains(Status.ROOSTED)) {
                    if (type1 == Type.FLYING)
                        type1 = Type.NONE
                    else
                        type2 = Type.NONE
                }
                if (opponent.hasType(Type.FLYING) && battleField.gravityCounter > 0) {
                    if (type1 == Type.FLYING)
                        type1 = Type.NONE
                    else
                        type2 = Type.NONE
                }
                if (opponent.battleData!!.magnetRiseCounter > 0
                    && battleField.gravityCounter == 0) {
                    return 0f
                }
            }
            if ((moveType == Type.NORMAL || moveType == Type.FIGHTING) && attacker.hasAbility(Ability.SCRAPPY) && opponent.hasType(
                    Type.GHOST
                )
            ) {
                if (type1 == Type.GHOST)
                    type1 = Type.NONE
                else
                    type2 = Type.NONE
            }
            var type: Float = moveType.isEffectiveAgainst(type2) * moveType.isEffectiveAgainst(type1)
            if (opponent.hasType(Type.WATER) && move.id == 268) { // FREEZE-DRY
                type *= 4f
            }
            if (move.id == 263) { //FLYING PRESS
                type *= Type.FLYING.isEffectiveAgainst(type2) * Type.FLYING.isEffectiveAgainst(type1)
            }
            if (type >= 2f && (opponent.hasAbility(Ability.FILTER) || opponent.hasAbility(Ability.SOLID_ROCK) || opponent.hasAbility(
                    Ability.DELTA_STREAM
                )) && !attacker.hasAbility(Ability.MOLD_BREAKER)
            )
                type *= 0.75f
            if (attacker.hasItem(HoldItem.EXPERT_BELT))
                type *= 1.2f
            if (attacker.hasAbility(Ability.TINTED_LENS) && type < 1f)
                type *= 2f
            return type
        }

        fun computeDamageWithoutAbility(attacker: Pokemon, move: Move, opponent: Pokemon, battleField: BattleField): Int {
            var multiplicator = 1f
            val stab = if (attacker.hasType(move.type)) 1.5f else 1f
            if (move.category == MoveCategory.PHYSICAL
                && !attacker.hasAbility(Ability.GUTS)
                && attacker.status == Status.BURN) {
                multiplicator *= 0.5f
            }
            val power = if (move is MoveBasedOnHP) move.getPower(attacker) else move.power
            val type = getEffectiveness(attacker, move, opponent, battleField)
            val random: Float = Random.nextInt(85, 100).toFloat() / 100f
            val ignoreOffensiveStatChange: Boolean =
                !attacker.hasAbility(Ability.MOLD_BREAKER) && opponent.hasAbility(Ability.UNAWARE)
            val offensiveStat: Int =
                if (move.category == MoveCategory.PHYSICAL) (attacker.attack.toFloat() * if (ignoreOffensiveStatChange) 1f else attacker.battleData!!.statsMultiplier.attackMultiplicator).roundToInt() else (attacker.spAtk.toFloat() * if (ignoreOffensiveStatChange) 1f else attacker.battleData!!.statsMultiplier.spAtkMultiplicator).roundToInt()
            val ignoreDefensiveStatChange: Boolean =
                attacker.hasAbility(Ability.UNAWARE) && !opponent.hasAbility(Ability.MOLD_BREAKER)
            val defensiveStat: Int =
                if (move.category == MoveCategory.PHYSICAL) (opponent.defense.toFloat() * if (ignoreDefensiveStatChange) 1f else opponent.battleData!!.statsMultiplier.defenseMultiplicator).roundToInt() else (opponent.spDef.toFloat() * if (ignoreDefensiveStatChange) 1f else opponent.battleData!!.statsMultiplier.spDefMultiplicator).roundToInt()
            return ((((((attacker.level.toFloat() * 0.4f).roundToInt() + 2) * power * offensiveStat) / (defensiveStat * 50)) + 2) * type * stab * multiplicator * random).roundToInt()
        }

        private fun computePower(
            attacker: Pokemon,
            move: Move,
            opponent: Pokemon,
            battleField: BattleField,
            opponentBattleSide: BattleSide,
            crit: Boolean
        ): Float {
            var power: Float =
                if (move is MoveBasedOnHP) move.getPower(attacker).toFloat() else move.power.toFloat()
            if (move.id == 240 && attacker.battleData!!.lastMoveFailed) {
                power *= 2
            }
            if (move.id == 305 && attacker.trainer != null) {
                power *= attacker.trainer!!.getTrainerTeam().filter { it.currentHP == 0}.size + 1
            }
            if (move.id == 241 && opponent.hp / opponent.currentHP >= 2) {
                power *= 2
            }
            if (move.id == 335) {
                //FURY CUTTER
                power = min(160f, move.power.toDouble().pow(attacker.battleData!!.sameMoveCounter + 1).toFloat())
            }
            if (move.id == 233) {
                //GYRO BALL
                power *= (1 + (opponent.speed * opponent.battleData!!.statsMultiplier.speedMultiplicator) / (attacker.speed * attacker.battleData!!.statsMultiplier.speedMultiplicator)).roundToInt()
            }
            if (attacker.hasAbility(Ability.SHEER_FORCE) && (move.status.isNotEmpty() || move is StatChangeMove))
                power *= 1.3f
            if (attacker.hasAbility(Ability.TOUGH_CLAW) && move.characteristics.contains(MoveCharacteristic.CONTACT))
                power *= 1.3f
            if (attacker.hasAbility(Ability.MEGA_LAUNCHER) && move.characteristics.contains(MoveCharacteristic.AURA))
                power *= 1.5f
            if (attacker.hasAbility(Ability.SAND_FORCE) && (move.type == Type.ROCK || move.type == Type.GROUND ||move.type == Type.STEEL))
                power *= 1.3f
            if (move.type == Type.NORMAL && (attacker.hasAbility(Ability.PIXILATE) || attacker.hasAbility(Ability.AERILATE))) {
                    power *= 1.2f
            }
            if (move.power <= 60 && attacker.hasAbility(Ability.TECHNICIAN))
                power *= 1.5f
            if ((move is RecoilMove || move.id == 210 || move.id == 244 || move.id == 283) && attacker.hasAbility(
                    Ability.RECKLESS
                )
            )
                power *= 1.2f
            if (move.characteristics.contains(MoveCharacteristic.PUNCH) && attacker.hasAbility(Ability.IRON_FIST))
                power *= 1.2f
            if (move.characteristics.contains(MoveCharacteristic.SLICE) && attacker.hasAbility(Ability.SHARPNESS))
                power *= 1.5f
            if (move.type == Type.FIRE) {
                if (attacker.battleData!!.battleStatus.contains(Status.FIRED_UP))
                    power *= 2f
                if (opponent.hasAbility(Ability.DRY_SKIN) || opponent.hasAbility(Ability.FLUFFY))
                    power *= 2f
            }
            if (move.type == Type.ELECTRIC && attacker.battleData!!.battleStatus.contains(Status.CHARGED)) {
                power *= 1.5f
            }
            if (move.type == Type.WATER && attacker.hasAbility(Ability.WATER_BUBBLE)) {
                power *= 2f
            }
            if (move.type == Type.WATER && opponent.hasAbility(Ability.WATER_BUBBLE)) {
                power *= 0.5f
            }
            if ((move.id == 224 || move.id == 278) && (opponent.status != Status.OK || opponent.battleData!!.battleStatus.contains(
                    Status.CONFUSED
                ))
            ) {
                power *= 2f
            }
            if (move.id == 232 && (opponent.status == Status.POISON || opponent.status == Status.BADLY_POISON)) {
                power *= 2f
            }
            if (move.id == 306 && attacker.status != Status.OK) {
                //FACADE
                power *= 2f
            }
            if (move.id == 307) {
                //RAGE FIST
                power += 50 * attacker.battleData!!.numberOfHitTaken
            }
            if (move.id == 316 && battleField.weather != Weather.NONE) {
                //WEATHER BALL
                power *= 2
            }
            val typeBoostItem = TYPE_ITEM_BOOST[move.type]
            if (typeBoostItem != null && attacker.hasItem(typeBoostItem))
                power *= 1.2f
            if (move.id == 272 && (attacker.heldItem == null || attacker.itemDisabled))
                power *= 2f
            if (move.id == 273) {
                //ELECTRO BALL
                val speedRatio = (attacker.speed / opponent.speed).toFloat()
                when {
                    speedRatio < 1f -> power *= 40f
                    speedRatio > 4f -> power *= 150f
                    speedRatio > 3f -> power *= 120f
                    speedRatio > 2f -> power *= 80f
                    speedRatio >= 1f -> power *= 60f
                }
            }
            if (attacker.hasItem(HoldItem.LIFE_ORB))
                power *= 1.3f
            if ((attacker.currentHP / attacker.hp) < 0.4) {
                if (move.type == Type.GRASS && attacker.hasAbility(Ability.OVERGROW))
                    power *= 1.5f
                if (move.type == Type.FIRE && attacker.hasAbility(Ability.BLAZE))
                    power *= 1.5f
                if (move.type == Type.WATER && attacker.hasAbility(Ability.TORRENT))
                    power *= 1.5f
                if (move.type == Type.BUG && attacker.hasAbility(Ability.SWARM))
                    power *= 1.5f
            }
            if (move.id == 247 && opponent.heldItem != null && !opponent.itemDisabled)
                power *= 1.5f
            if (move.type == Type.STEEL && attacker.hasAbility(Ability.STEELWORKER))
                power *= 1.5f
            if (attacker.battleData!!.lastHitReceived != null && (move.id == 251 || move.id == 299))
                power *= 2f
            if (move.id == 260) {
                var statBoost = 1f
                if (attacker.battleData!!.statsMultiplier.attackMultiplicator > 1f)
                    statBoost += 20f * attacker.battleData!!.statsMultiplier.attackMultiplicator
                if (attacker.battleData!!.statsMultiplier.defenseMultiplicator > 1f)
                    statBoost += 20f * attacker.battleData!!.statsMultiplier.defenseMultiplicator
                if (attacker.battleData!!.statsMultiplier.spAtkMultiplicator > 1f)
                    statBoost += 20f * attacker.battleData!!.statsMultiplier.spAtkMultiplicator
                if (attacker.battleData!!.statsMultiplier.spDefMultiplicator > 1f)
                    statBoost += 20f * attacker.battleData!!.statsMultiplier.spDefMultiplicator
                if (attacker.battleData!!.statsMultiplier.speedMultiplicator > 1f)
                    statBoost += 20f * attacker.battleData!!.statsMultiplier.speedMultiplicator
                return power + statBoost
            }
            if (attacker.hasAbility(Ability.PARENTAL_BOND) && opponent.battleData!!.child) {
                power *= 0.25f
            }
            if (move.characteristics.contains(MoveCharacteristic.CONTACT) && opponent.hasAbility(Ability.FLUFFY)) {
                power *= 0.5f
            }
            if (!crit) {
                if (opponentBattleSide.battleSideEffects.contains(BattleSideEffect.AURORA_VEIL)
                    && !attacker.hasAbility(Ability.INFILTRATOR)) {
                    power *= 0.5f
                }
                if (opponentBattleSide.battleSideEffects.contains(BattleSideEffect.REFLECT)
                    && move.category == MoveCategory.PHYSICAL
                    && !attacker.hasAbility(Ability.INFILTRATOR)
                ) {
                    power *= 0.5f
                }
                if (opponentBattleSide.battleSideEffects.contains(BattleSideEffect.LIGHT_SCREEN)
                    && (move.category == MoveCategory.SPECIAL || move.category == MoveCategory.SPECIAL_AND_PHYSICAL)
                    && !attacker.hasAbility(Ability.INFILTRATOR)
                ) {
                    power *= 0.5f
                }
            }
            return power
        }

        fun computeDamage(
            attacker: Pokemon,
            move: Move,
            opponent: Pokemon,
            criticalMultiplicator: Float,
            battleField: BattleField,
            opponentBattleSide: BattleSide
        ): Int {
            if (opponent.currentHP == 0)
                return 0
            if (!attacker.hasAbility(Ability.MOLD_BREAKER)) {
                if (move.characteristics.contains(MoveCharacteristic.SOUND) && opponent.hasAbility(Ability.SOUNDPROOF))
                    return 0
                if (move.type == Type.GROUND
                    && battleField.gravityCounter == 0
                    && opponent.hasAbility(Ability.LEVITATE))
                    return 0
                if (move.characteristics.contains(MoveCharacteristic.BULLET) && opponent.hasAbility(Ability.BULLETPROOF))
                    return 0
                if (move.type == Type.WATER && (opponent.hasAbility(Ability.WATER_ABSORB)
                                                || opponent.hasAbility(Ability.STORM_DRAIN)
                                                || opponent.hasAbility(Ability.DRY_SKIN)))
                    return 0
                if (move.type == Type.ELECTRIC && (opponent.hasAbility(Ability.LIGHTNING_ROD) || opponent.hasAbility(
                        Ability.VOLT_ABSORB
                    ) || opponent.hasAbility(
                        Ability.MOTOR_DRIVE
                    ))
                )
                    return 0
                if (move.type == Type.FIRE && opponent.hasAbility(Ability.FLASH_FIRE))
                    return 0
                if (move.type == Type.GRASS && opponent.hasAbility(Ability.SAP_SIPPER))
                    return 0
                if (move is RecoilMove && move.recoil == Recoil.ALL && opponent.hasAbility(Ability.DAMP))
                    return 0
            }
            if (move.characteristics.contains(MoveCharacteristic.JUMP) && battleField.gravityCounter > 0)
                return 0
            if (move.type == Type.GROUND
                && battleField.gravityCounter == 0
                && opponent.hasItem(HoldItem.AIR_BALLOON))
                return 0
            if (move.id == 331 && (opponent.heldItem == null || opponent.itemDisabled)) //Poltergeist
                return 0
            if (move is RetaliationMove) {
                if (attacker.battleData!!.lastHitReceived != null && move.category == attacker.battleData!!.lastHitReceived!!.category)
                    return attacker.battleData!!.lastHitReceived!!.damage * 2
                return 0
            }
            var multiplicator = 1f
            var stab = if (attacker.hasType(move.type)) 1.5f else 1f
            if (stab == 1.5f && attacker.hasAbility(Ability.ADAPTABILITY))
                stab = 2f
            if (move.category == MoveCategory.PHYSICAL
                && !attacker.hasAbility(Ability.GUTS)
                && criticalMultiplicator == 1f
                && attacker.status == Status.BURN) {
                multiplicator *= 0.5f
            }
            if (!attacker.hasAbility(Ability.MOLD_BREAKER)) {
                if ((move.type == Type.FIRE || move.type == Type.ICE) && opponent.hasAbility(Ability.THICK_FAT))
                    multiplicator *= 0.5f
                if (move.type == Type.FIRE
                    && (opponent.hasAbility(Ability.WATER_BUBBLE) || opponent.hasAbility(Ability.HEATPROOF))
                )
                    multiplicator *= 0.5f
            }
            return try {
                val type = getEffectiveness(attacker, move, opponent, battleField)
                if (type > 0) {
                    if (move is MoveBasedOnLevel) {
                        return attacker.level
                    } else if (move.id == 308) {
                        return attacker.currentHP
                    }
                }
                val random: Float = Random.nextInt(85, 100).toFloat() / 100f
                val ignoreOffensiveStatChange: Boolean =
                    !attacker.hasAbility(Ability.MOLD_BREAKER) && opponent.hasAbility(Ability.UNAWARE) && criticalMultiplicator == 1f
                val ignoreDefensiveStatChange: Boolean =
                    attacker.hasAbility(Ability.UNAWARE) && !opponent.hasAbility(Ability.MOLD_BREAKER) && criticalMultiplicator == 1f
                var offensiveStat: Int =
                    if (move.category == MoveCategory.PHYSICAL) (attacker.attack.toFloat() * if (ignoreOffensiveStatChange) 1f else attacker.battleData!!.statsMultiplier.attackMultiplicator).roundToInt() else (attacker.spAtk.toFloat() * if (ignoreOffensiveStatChange) 1f else attacker.battleData!!.statsMultiplier.spAtkMultiplicator).roundToInt()
                if (move.id == 223) {
                    //FOUL PLAY
                    offensiveStat = (opponent.attack.toFloat() * opponent.battleData!!.statsMultiplier.attackMultiplicator).roundToInt()
                }
                if (move.id == 267) {
                    //BODY PRESS
                    offensiveStat =
                        (attacker.defense.toFloat() * if (ignoreOffensiveStatChange) 1f else  attacker.battleData!!.statsMultiplier.defenseMultiplicator).roundToInt()
                }
                val defensiveStat: Int =
                    if (move.category == MoveCategory.PHYSICAL || move.category == MoveCategory.SPECIAL_AND_PHYSICAL) (opponent.defense.toFloat() * if (ignoreDefensiveStatChange) 1f else opponent.battleData!!.statsMultiplier.defenseMultiplicator).roundToInt() else (opponent.spDef.toFloat() * if (ignoreDefensiveStatChange) 1f else opponent.battleData!!.statsMultiplier.spDefMultiplicator).roundToInt()
                ((((((attacker.level.toFloat() * 0.4f).roundToInt() + 2) * computePower(
                    attacker,
                    move,
                    opponent,
                    battleField,
                    opponentBattleSide,
                    criticalMultiplicator > 1f
                ) * offensiveStat) / (defensiveStat * 50)) + 2) * type * criticalMultiplicator * stab * multiplicator * random).roundToInt()
            } catch (e: Exception) {
                0
            }
        }

        fun computeConfusionDamage(attacker: Pokemon): Int {
            var multiplicator = 1f
            if (attacker.status == Status.BURN && !attacker.hasAbility(Ability.GUTS))
                multiplicator *= 0.5f
            val random: Float = Random.nextInt(85, 100).toFloat() / 100f
            val offensiveStat: Int =
                (attacker.attack.toFloat() * attacker.battleData!!.statsMultiplier.attackMultiplicator).roundToInt()
            val defensiveStat: Int =
                (attacker.defense.toFloat() * attacker.battleData!!.statsMultiplier.defenseMultiplicator).roundToInt()
            return ((((((attacker.level.toFloat() * 0.4f).roundToInt() + 2) * 40 * offensiveStat) / (defensiveStat * 50)) + 2) * multiplicator * random).roundToInt()
        }
    }
}