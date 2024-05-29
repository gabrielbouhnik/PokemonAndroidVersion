package com.pokemon.android.version.model.battle

import com.pokemon.android.version.model.Ability
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.model.item.HoldItem
import com.pokemon.android.version.model.move.*
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
            Type.FIGHTING to HoldItem.EXPERT_BELT,
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
                if (random < 13 * attacker.battleData!!.criticalRate)
                    return if (attacker.hasAbility(Ability.SNIPER)) 2.25f else 1.5f
            } else if (random < 6 * attacker.battleData!!.criticalRate)
                return if (attacker.hasAbility(Ability.SNIPER)) 2.25f else 1.5f
            return 1f
        }

        fun getEffectiveness(attacker: Pokemon, move: Move, opponent: Pokemon): Float {
            var type1 = if (opponent.isMegaEvolved) opponent.data.megaEvolutionData!!.type1 else opponent.data.type1
            var type2 = if (opponent.isMegaEvolved) opponent.data.megaEvolutionData!!.type2 else opponent.data.type2
            if (move.type == Type.GROUND && opponent.battleData!!.battleStatus.contains(Status.ROOSTED)) {
                if (type1 == Type.FLYING)
                    type1 = Type.NONE
                else
                    type2 = Type.NONE
            }
            if ((move.type == Type.NORMAL || move.type == Type.FIGHTING) && attacker.hasAbility(Ability.SCRAPPY) && opponent.hasType(Type.GHOST)) {
                if (type1 == Type.GHOST)
                    type1 = Type.NONE
                else
                    type2 = Type.NONE
            }
            var type: Float = move.type.isEffectiveAgainst(type2) * move.type.isEffectiveAgainst(type1)
            if (type > 1f && opponent.hasAbility(Ability.FILTER))
                type *= 0.75f
            if (opponent.hasType(Type.WATER) && move.id == 268)
                type *= 4f
            if (move.id == 263) {
                type *= Type.FLYING.isEffectiveAgainst(type2) * Type.FLYING.isEffectiveAgainst(type1)
            }
            return type
        }

        fun computeDamageWithoutAbility(attacker: Pokemon, move: Move, opponent: Pokemon): Int {
            var multiplicator = 1f
            var stab = if (attacker.data.type1 == move.type || attacker.data.type2 == move.type) 1.5f else 1f
            if (attacker.isMegaEvolved && (attacker.data.megaEvolutionData!!.type1 == move.type || attacker.data.megaEvolutionData!!.type2 == move.type))
                stab = 1.5f
            if (move.category == MoveCategory.PHYSICAL && !attacker.hasAbility(Ability.GUTS)) {
                if (attacker.status == Status.BURN)
                    multiplicator *= 0.5f
            }
            val power = if (move is MoveBasedOnHP) move.getPower(attacker) else move.power
            val type = getEffectiveness(attacker, move, opponent)
            val random: Float = Random.nextInt(85, 100).toFloat() / 100f
            val offensiveStat: Int =
                if (move.category == MoveCategory.PHYSICAL) (attacker.attack.toFloat() * if (attacker.hasAbility(
                        Ability.UNAWARE
                    )
                ) 1f else attacker.battleData!!.attackMultiplicator).roundToInt() else (attacker.spAtk.toFloat() * if (attacker.hasAbility(
                        Ability.UNAWARE
                    )
                ) 1f else attacker.battleData!!.spAtkMultiplicator).roundToInt()
            val defensiveStat: Int =
                if (move.category == MoveCategory.PHYSICAL) (opponent.defense.toFloat() * if (attacker.hasAbility(
                        Ability.UNAWARE
                    )
                ) 1f else opponent.battleData!!.defenseMultiplicator).roundToInt() else (opponent.spDef.toFloat() * if (attacker.hasAbility(
                        Ability.UNAWARE
                    )
                ) 1f else opponent.battleData!!.spDefMultiplicator).roundToInt()
            return ((((((attacker.level.toFloat() * 0.4f).roundToInt() + 2) * power * offensiveStat) / (defensiveStat * 50)) + 2) * type * stab * multiplicator * random).roundToInt()
        }

        private fun computePower(attacker: Pokemon, move: Move, opponent: Pokemon): Float {
            var power: Float =
                if (move is MoveBasedOnHP) move.getPower(attacker).toFloat() else move.power.toFloat()
            if (move.id == 240 && attacker.battleData!!.lastMoveFailed) {
                power *= 2
            }
            if (move.id == 241 && opponent.hp / opponent.currentHP >= 2) {
                power *= 2
            }
            if (move.id == 233) {
                power *= (1 + (opponent.speed * opponent.battleData!!.speedMultiplicator) / (attacker.speed * attacker.battleData!!.speedMultiplicator)).roundToInt()
            }
            if (attacker.hasAbility(Ability.SHEER_FORCE) && (move.status.isNotEmpty() || move is StatChangeMove))
                power *= 1.3f
            if (attacker.hasAbility(Ability.TOUGH_CLAW) && move.characteristics.contains(MoveCharacteristic.CONTACT))
                power *= 1.3f
            if (attacker.hasAbility(Ability.MEGA_LAUNCHER) && move.characteristics.contains(MoveCharacteristic.AURA))
                power *= 1.5f
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
                    power *= 1.5f
                if (opponent.hasAbility(Ability.DRY_SKIN))
                    power *= 2f
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
            val typeBoostItem = TYPE_ITEM_BOOST[move.type]
            if (typeBoostItem != null && attacker.hasItem(typeBoostItem))
                power *= 1.2f
            if (move.id == 272 && attacker.heldItem == null)
                power *= 2f
            if (move.id == 273) {
                val speedRatio = (attacker.speed / opponent.speed).toFloat()
                when {
                    speedRatio < 1f -> power *= 40f
                    speedRatio > 4f -> power *= 150f
                    speedRatio > 3f -> power *= 120f
                    speedRatio > 2f -> power *= 80f
                    speedRatio > 1f -> power *= 60f
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
            if (move.id == 247 && opponent.heldItem != null)
                power *= 1.5f
            if (attacker.battleData!!.lastHitReceived != null && move.id == 251)
                power *= 2f
            if (move.id == 260) {
                val statBoost =
                    attacker.battleData!!.attackMultiplicator * attacker.battleData!!.defenseMultiplicator * attacker.battleData!!.spAtkMultiplicator * attacker.battleData!!.spDefMultiplicator * attacker.battleData!!.speedMultiplicator
                if (statBoost > 1f)
                    return power * statBoost * 2f
            }
            return power
        }

        fun computeDamage(attacker: Pokemon, move: Move, opponent: Pokemon, criticalMultiplicator: Float): Int {
            if (opponent.currentHP == 0)
                return 0
            if (move.characteristics.contains(MoveCharacteristic.SOUND) && opponent.hasAbility(Ability.SOUNDPROOF))
                return 0
            if (move.type == Type.GROUND && opponent.hasAbility(Ability.LEVITATE))
                return 0
            if (move.characteristics.contains(MoveCharacteristic.BULLET) && opponent.hasAbility(Ability.BULLETPROOF))
                return 0
            if (move.type == Type.WATER && (opponent.hasAbility(Ability.WATER_ABSORB) || opponent.hasAbility(Ability.DRY_SKIN)))
                return 0
            if (move.type == Type.ELECTRIC && (opponent.hasAbility(Ability.LIGHTNING_ROD) || opponent.hasAbility(Ability.VOLT_ABSORB) || opponent.hasAbility(
                    Ability.MOTOR_DRIVE
                ))
            )
                return 0
            if (move.type == Type.FIRE && opponent.hasAbility(Ability.FLASH_FIRE))
                return 0
            if (move is RecoilMove && move.recoil == Recoil.ALL && opponent.hasAbility(Ability.DAMP))
                return 0
            if (move is RetaliationMove) {
                if (attacker.battleData!!.lastHitReceived != null && move.category == attacker.battleData!!.lastHitReceived!!.category)
                    return attacker.battleData!!.lastHitReceived!!.damage * 2
                return 0
            }
            var multiplicator = 1f
            var stab = if (attacker.data.type1 == move.type || attacker.data.type2 == move.type) 1.5f else 1f
            if (attacker.isMegaEvolved && (attacker.data.megaEvolutionData!!.type1 == move.type || attacker.data.megaEvolutionData!!.type2 == move.type))
                stab = 1.5f
            if (stab == 1.5f && attacker.hasAbility(Ability.ADAPTABILITY))
                stab = 2f
            if (move.category == MoveCategory.PHYSICAL && criticalMultiplicator == 1f && !attacker.hasAbility(Ability.GUTS)) {
                if (attacker.status == Status.BURN)
                    multiplicator *= 0.5f
            }
            if ((move.type == Type.FIRE || move.type == Type.ICE) && opponent.hasAbility(Ability.THICK_FAT))
                multiplicator *= 0.5f
            return try {
                var type = getEffectiveness(attacker, move, opponent)
                if (type >= 2f) {
                    if (opponent.hasAbility(Ability.SOLID_ROCK))
                        type *= 0.75f
                    if (attacker.hasItem(HoldItem.EXPERT_BELT))
                        type *= 1.2f
                }
                if (attacker.hasAbility(Ability.TINTED_LENS) && type < 1f)
                    type *= 2f
                val random: Float = Random.nextInt(85, 100).toFloat() / 100f
                var offensiveStat: Int =
                    if (move.category == MoveCategory.PHYSICAL) (attacker.attack.toFloat() * attacker.battleData!!.attackMultiplicator).roundToInt() else (attacker.spAtk.toFloat() * attacker.battleData!!.spAtkMultiplicator).roundToInt()
                if (move.id == 223) {
                    offensiveStat = (opponent.attack.toFloat() * opponent.battleData!!.attackMultiplicator).roundToInt()
                }
                if (move.id == 267) {
                    offensiveStat =
                        (attacker.defense.toFloat() * attacker.battleData!!.defenseMultiplicator).roundToInt()
                }
                var defensiveStat: Int =
                    if (move.category == MoveCategory.PHYSICAL || move.category == MoveCategory.SPECIAL_AND_PHYSICAL) (opponent.defense.toFloat() * opponent.battleData!!.defenseMultiplicator).roundToInt() else (opponent.spDef.toFloat() * opponent.battleData!!.spDefMultiplicator).roundToInt()
                if (criticalMultiplicator >= 1.5f) {
                    if (move.category == MoveCategory.PHYSICAL) {
                        offensiveStat = if (move.category == MoveCategory.PHYSICAL) attacker.attack else attacker.spAtk
                        if (move.id == 223) {
                            offensiveStat = opponent.attack
                        }
                        defensiveStat =
                            if (move.category == MoveCategory.PHYSICAL || move.category == MoveCategory.SPECIAL_AND_PHYSICAL) (opponent.defense.toFloat()).roundToInt() else (opponent.spDef.toFloat()).roundToInt()
                    } else {
                        offensiveStat = if (move.category == MoveCategory.PHYSICAL) attacker.attack else attacker.spAtk
                        defensiveStat =
                            if (move.category == MoveCategory.PHYSICAL || move.category == MoveCategory.SPECIAL_AND_PHYSICAL) opponent.defense else opponent.spDef
                    }
                }
                ((((((attacker.level.toFloat() * 0.4f).roundToInt() + 2) * computePower(
                    attacker,
                    move,
                    opponent
                ) * offensiveStat) / (defensiveStat * 50)) + 2) * type * criticalMultiplicator * stab * multiplicator * random).roundToInt()
            } catch (e: Exception) {
                0
            }
        }

        fun computeConfusionDamage(attacker: Pokemon): Int {
            var multiplicator = 1f
            if (attacker.status == Status.BURN)
                multiplicator *= 0.5f
            val random: Float = Random.nextInt(85, 100).toFloat() / 100f
            val offensiveStat: Int =
                (attacker.attack.toFloat() * attacker.battleData!!.attackMultiplicator).roundToInt()
            val defensiveStat: Int =
                (attacker.defense.toFloat() * attacker.battleData!!.defenseMultiplicator).roundToInt()
            return ((((((attacker.level.toFloat() * 0.4f).roundToInt() + 2) * 40 * offensiveStat) / (defensiveStat * 50)) + 2) * multiplicator * random).roundToInt()
        }
    }
}