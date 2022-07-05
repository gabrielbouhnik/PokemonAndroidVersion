package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.StatusEntity
import com.pokemon.android.version.model.Status

class StatusMove(
    var status: Status,
    var probability: Int?
) {
    companion object {
        fun of(statusEntity: StatusEntity): StatusMove {
            return StatusMove(Status.valueOf(statusEntity.status), statusEntity.probability)
        }
    }
}