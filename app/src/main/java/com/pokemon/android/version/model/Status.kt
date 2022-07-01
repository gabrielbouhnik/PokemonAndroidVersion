package com.pokemon.android.version.model

import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.StatusMove
import kotlin.random.Random

enum class Status {
    OK,
    BURN,
    PARALYSIS,
    POISON,
    ASLEEP,
    FROZEN,
    //Only for pokemon battle data:
    CONFUSED,
    FLINCHED,
    TRAPPED;

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
                val randomForStatus: Int = Random.nextInt(100)
                if (randomForStatus <= it.probability) {
                    if ((it.status == FLINCHED || it.status == CONFUSED || it.status == TRAPPED) && !opponent.battleData!!.battleStatus.contains(
                            it.status
                        )
                    )
                        opponent.battleData!!.battleStatus.add(it.status)
                    else if (isAffectedByStatus(it.status, opponent) && opponent.status == OK) {
                        opponent.status = it.status
                    }
                }
            }
        }

        fun isAffectedByStatus(status: Status, opponent: Pokemon): Boolean {
            if ((status == FLINCHED || status == CONFUSED || status == TRAPPED) && !opponent.battleData!!.battleStatus.contains( status))
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