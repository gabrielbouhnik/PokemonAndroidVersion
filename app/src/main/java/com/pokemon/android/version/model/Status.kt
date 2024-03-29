package com.pokemon.android.version.model

import com.pokemon.android.version.model.move.Move
import kotlin.random.Random

enum class Status(var activeOutsideBattle: Boolean) {
    OK(true),
    BURN(true),
    PARALYSIS(true),
    POISON(true),
    ASLEEP(true),
    FROZEN(true),
    CONFUSED(false),
    FLINCHED(false),
    TRAPPED(false);

    fun toBattleIcon(): String {
        when (this) {
            BURN -> return "BRN"
            PARALYSIS -> return "PAR"
            POISON -> return "PSN"
            ASLEEP -> return "SLP"
            FROZEN -> return "FRZ"
        }
        return ""
    }

    companion object {
        fun updateStatus(opponent: Pokemon, move: Move) {
            move.status.forEach {
                if (isAffectedByStatus(it.status, opponent)) {
                    val randomForStatus: Int = Random.nextInt(100)
                    if (it.probability == null || randomForStatus <= it.probability!!) {
                        if (it.status.activeOutsideBattle)
                            opponent.status = it.status
                        else
                            opponent.battleData!!.battleStatus.add(it.status)
                    }
                }
            }
        }

        fun isAffectedByStatus(status: Status, opponent: Pokemon): Boolean {
            if (!status.activeOutsideBattle && !opponent.battleData!!.battleStatus.contains(status))
                return true
            if (opponent.status != OK)
                return false
            if (status == ASLEEP)
                return true
            if (status == FROZEN && (opponent.data.type1 != Type.ICE && opponent.data.type2 != Type.ICE))
                return true
            if (status == BURN && (opponent.data.type1 != Type.FIRE && opponent.data.type2 != Type.FIRE))
                return true
            if (status == PARALYSIS && (opponent.data.type1 != Type.ELECTRIC && opponent.data.type2 != Type.ELECTRIC))
                return true
            if (status == POISON && (opponent.data.type1 != Type.POISON && opponent.data.type2 != Type.POISON) && (opponent.data.type1 != Type.STEEL && opponent.data.type2 != Type.STEEL))
                return true
            return false
        }
    }
}