package com.pokemon.android.version.model

import com.pokemon.android.version.model.move.StatusMove
import kotlin.random.Random

enum class Status {
    OK,
    BURN,
    PARALYSIS,
    POISON,
    ASLEEP,
    //Only for pokemon battle data
    CONFUSED,
    FLINCHED,
    TRAPPED;

    fun toBattleIcon() : String {
        when(this){
            BURN -> return "BRN"
            PARALYSIS -> return "PAR"
            POISON -> return "PSN"
            ASLEEP -> return "SLP"
            else -> "OK"
        }
        return ""
    }

    companion object{
        fun updateStatus(opponent : Pokemon, statusMove : StatusMove){
            val randomForStatus : Int = Random.nextInt(100)
            if (randomForStatus <= statusMove.probability) {
                if ((statusMove.status == FLINCHED || statusMove.status == CONFUSED || statusMove.status == TRAPPED) && !opponent.battleData!!.battleStatus.contains(statusMove.status))
                    opponent.battleData!!.battleStatus.add(statusMove.status)
                else if (isAffectedByStatus(statusMove.status, opponent)){
                    opponent.status = statusMove.status
                }
            }
        }

        fun isAffectedByStatus(status: Status, opponent : Pokemon) : Boolean{
            if ((status == FLINCHED || status == CONFUSED || status == TRAPPED) && !opponent.battleData!!.battleStatus.contains(status))
                return true
            if (opponent.status != OK)
                return false
            if (status == BURN && (opponent.data.type1 != Type.FIRE && opponent.data.type2 != Type.FIRE))
                return true
            if (status == PARALYSIS && (opponent.data.type1 != Type.ELECTRIC && opponent.data.type2 != Type.ELECTRIC))
                return true
            if (status == POISON && (opponent.data.type1 != Type.POISON && opponent.data.type2 != Type.POISON))
                return true
            return false
        }
    }
}