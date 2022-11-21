package com.pokemon.android.version.model

import com.pokemon.android.version.model.move.HealMove
import com.pokemon.android.version.model.move.Move
import kotlin.random.Random

enum class Status(var activeOutsideBattle: Boolean) {
    OK(true),
    BURN(true),
    PARALYSIS(true),
    POISON(true),
    BADLY_POISON(true),
    ASLEEP(true),
    FROZEN(true),
    CONFUSED(false),
    FLINCHED(false),
    TIRED(false),
    UNABLE_TO_MOVE(false),
    TRAPPED_WITH_DAMAGE(false),
    TRAPPED_WITHOUT_DAMAGE(false),
    FIRED_UP(false),
    ROOSTED(false);

    fun toDetails(): String {
        return when (this) {
            BURN -> "is burned!"
            PARALYSIS -> "is paralyzed!"
            POISON -> "is poisoned!"
            BADLY_POISON -> "is badly poisoned!"
            ASLEEP -> "fell asleep!"
            FROZEN -> "is frozen!"
            else -> ""
        }
    }

    fun toBattleIcon(): String {
        return when (this) {
            BURN -> "BRN"
            PARALYSIS -> "PAR"
            POISON, BADLY_POISON -> "PSN"
            ASLEEP -> "SLP"
            FROZEN -> "FRZ"
            else -> ""
        }
    }

    companion object {
        fun updateStatus(attacker: Pokemon, opponent: Pokemon, move: Move): String {
            var details = ""
            move.status.forEach {
                if (isAffectedByStatus(move.id, it.status, opponent)) {
                    var randomForStatus: Int = Random.nextInt(100)
                    if (attacker.hasAbility(Ability.SERENE_GRACE))
                        randomForStatus /= 2
                    if (it.probability == null || randomForStatus <= it.probability!!) {
                        if (it.status.activeOutsideBattle) {
                            opponent.status = it.status
                            details += "${opponent.data.name} " + it.status.toDetails() + "\n"
                            if (opponent.hasAbility(Ability.GUTS))
                                opponent.battleData!!.attackMultiplicator *= 1.5f
                            if (opponent.hasAbility(Ability.QUICK_FEET))
                                opponent.battleData!!.speedMultiplicator *= 1.5f
                            if (opponent.hasAbility(Ability.MARVEL_SCALE))
                                opponent.battleData!!.defenseMultiplicator *= 1.5f
                            if (opponent.hasAbility(Ability.COMPETITIVE))
                                opponent.battleData!!.spAtkMultiplicator *= 1.5f
                            if (opponent.hasAbility(Ability.SYNCHRONIZE)
                                && isAffectedByStatus(0, it.status, attacker)
                            ) {
                                attacker.status = it.status
                                details += "Synchronize: ${attacker.data.name} " + it.status.toDetails() + "\n"
                            }

                        } else {
                            if (it.status == CONFUSED) {
                                details = if (opponent.hasAbility(Ability.OWN_TEMPO))
                                    "Own Tempo: ${opponent.data.name} cannot be confused!\n"
                                else {
                                    opponent.battleData!!.battleStatus.add(it.status)
                                    "${opponent.data.name} became confused!\n"
                                }
                            } else if (it.status == FLINCHED) {
                                if (!opponent.hasAbility(Ability.INNER_FOCUS))
                                    opponent.battleData!!.battleStatus.add(it.status)
                            } else if (move is HealMove) {
                                attacker.battleData!!.battleStatus.add(it.status)
                            } else {
                                opponent.battleData!!.battleStatus.add(it.status)
                                if (it.status == TRAPPED_WITH_DAMAGE || it.status == TRAPPED_WITHOUT_DAMAGE)
                                    details = "${opponent.data.name} is trapped!\n"
                                if (it.status == TIRED && opponent.status == OK)
                                    details = "${opponent.data.name} gets drowsy!\n"
                            }
                        }
                    }
                } else if (opponent.status == OK) {
                    if (move.id == 83 && opponent.hasAbility(Ability.IMMUNITY))
                        details = "Immunity: It does not affect ${opponent.data.name}!\n"
                    if (move.id == 55 && opponent.hasAbility(Ability.LIMBER))
                        details = "Limber: It does not affect ${opponent.data.name}!\n"
                    if (move.id == 138 && opponent.hasAbility(Ability.WATER_VEIL))
                        details = "Water Veil: It does not affect ${opponent.data.name}!\n"
                    if (it.status == ASLEEP) {
                        if (opponent.hasAbility(Ability.INSOMNIA))
                            details = "Insomnia: It does not affect ${opponent.data.name}!\n"
                        if (opponent.hasAbility(Ability.VITAL_SPIRIT))
                            details = "Vital Spirit: It does not affect ${opponent.data.name}!\n"
                    }
                }
            }
            return details
        }

        fun isAffectedByStatus(id: Int, status: Status, opponent: Pokemon): Boolean {
            if ((id == 34 || id == 35) && (opponent.data.type1 == Type.GRASS || opponent.data.type2 == Type.GRASS))
                return false
            if (id == 55 && (opponent.data.type1 == Type.GROUND || opponent.data.type2 == Type.GROUND))
                return false
            if (status == TIRED && opponent.status != OK)
                return false
            if (!status.activeOutsideBattle && !opponent.battleData!!.battleStatus.contains(status))
                return true
            if (opponent.status != OK)
                return false
            if (status == ASLEEP && !opponent.hasAbility(Ability.INSOMNIA) && !opponent.hasAbility(Ability.VITAL_SPIRIT))
                return true
            val type1 = if (opponent.isMegaEvolved()) opponent.data.megaEvolutionData!!.type1 else opponent.data.type1
            val type2 = if (opponent.isMegaEvolved()) opponent.data.megaEvolutionData!!.type2 else opponent.data.type2
            if (status == FROZEN && (type1 != Type.ICE && type2 != Type.ICE) && !opponent.hasAbility(Ability.MAGMA_ARMOR))
                return true
            if (status == BURN && (type1 != Type.FIRE && type2 != Type.FIRE) && !opponent.hasAbility(Ability.WATER_VEIL))
                return true
            if (status == PARALYSIS && (type1 != Type.ELECTRIC && type2 != Type.ELECTRIC) && !opponent.hasAbility(
                    Ability.LIMBER
                )
            )
                return true
            if ((status == POISON || status == BADLY_POISON) && (type1 != Type.POISON && type2 != Type.POISON) && (type1 != Type.STEEL && type2 != Type.STEEL)
                && !opponent.hasAbility(Ability.IMMUNITY)
            )
                return true
            return false
        }
    }
}