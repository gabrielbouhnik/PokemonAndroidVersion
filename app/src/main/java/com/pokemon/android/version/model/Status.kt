package com.pokemon.android.version.model

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
    TRAPPED(false);

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
        fun updateStatus(opponent: Pokemon, move: Move): String {
            var details = ""
            move.status.forEach {
                if (isAffectedByStatus(move.id, it.status, opponent)) {
                    val randomForStatus: Int = Random.nextInt(100)
                    if (it.probability == null || randomForStatus <= it.probability!!) {
                        if (it.status.activeOutsideBattle) {
                            opponent.status = it.status
                            if (opponent.data.abilities.contains(Ability.GUTS))
                                opponent.battleData!!.attackMultiplicator *= 1.5f
                        }
                        else {
                            if (it.status == CONFUSED) {
                                details = if (opponent.data.abilities.contains(Ability.OWN_TEMPO))
                                    "Own Tempo: ${opponent.data.name} cannot be confused!\n"
                                else {
                                    opponent.battleData!!.battleStatus.add(it.status)
                                    "${opponent.data.name} became confused!\n"
                                }
                            }
                            else if (it.status == FLINCHED){
                                if (!opponent.data.abilities.contains(Ability.INNER_FOCUS))
                                    opponent.battleData!!.battleStatus.add(it.status)
                            }
                            else {
                                opponent.battleData!!.battleStatus.add(it.status)
                                if (it.status == TRAPPED)
                                    details = "${opponent.data.name} is trapped!\n"
                                if (it.status == TIRED && opponent.status == OK)
                                    details = "${opponent.data.name} gets drowsy!\n"
                            }
                        }
                    }
                }
                else{
                    if (move.id == 83 && opponent.data.abilities.contains(Ability.IMMUNITY))
                        details = "Immunity: It does not affect ${opponent.data.name}!\n"
                    if (it.status == PARALYSIS && opponent.data.abilities.contains(Ability.LIMBER))
                        details = "Limber: It does not affect ${opponent.data.name}!\n"
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
            if (status == ASLEEP && !opponent.data.abilities.contains(Ability.INSOMNIA))
                return true
            val type1 = if (opponent.isMegaEvolved()) opponent.data.megaEvolutionData!!.type1 else opponent.data.type1
            val type2 = if (opponent.isMegaEvolved()) opponent.data.megaEvolutionData!!.type2 else opponent.data.type2
            if (status == FROZEN && (type1 != Type.ICE && type2 != Type.ICE))
                return true
            if (status == BURN && (type1 != Type.FIRE && type2 != Type.FIRE))
                return true
            if (status == PARALYSIS && (type1 != Type.ELECTRIC && type2 != Type.ELECTRIC) && !opponent.data.abilities.contains(Ability.LIMBER))
                return true
            if ((status == POISON || status ==  BADLY_POISON) && (type1 != Type.POISON && type2 != Type.POISON) && (type1 != Type.STEEL && type2 != Type.STEEL)
                && !opponent.data.abilities.contains(Ability.IMMUNITY))
                return true
            return false
        }
    }
}