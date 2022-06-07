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

    companion object{
        fun updateStatus(opponent : Pokemon, statusMove : StatusMove){
            val randomForStatus : Int = Random.nextInt(100)
            if (randomForStatus < statusMove.probability) {
                if (statusMove.status == FLINCHED || statusMove.status == CONFUSED || statusMove.status == TRAPPED)
                    opponent.battleData!!.battleStatus.add(statusMove.status)
                else {
                    if (opponent.status == OK) {
                        if (statusMove.status == BURN && (opponent.data.type1 != Type.FIRE && opponent.data.type2 != Type.FIRE))
                            opponent.status = statusMove.status
                        if (statusMove.status == PARALYSIS && (opponent.data.type1 != Type.ELECTRIC && opponent.data.type2 != Type.ELECTRIC))
                            opponent.status = statusMove.status
                        if (statusMove.status == POISON && (opponent.data.type1 != Type.POISON && opponent.data.type2 != Type.POISON))
                            opponent.status = statusMove.status
                    }
                }
            }
        }
    }
}