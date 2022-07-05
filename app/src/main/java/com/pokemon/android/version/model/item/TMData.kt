package com.pokemon.android.version.model.item

import com.pokemon.android.version.entity.item.TMEntity
import com.pokemon.android.version.model.move.Move

class TMData(id: Int, name: String, var move: Move) : ItemData(id, name) {
    companion object {
        fun of(tmEntity: TMEntity, move: Move): ItemData {
            return TMData(tmEntity.id, tmEntity.name, move)
        }
    }
}