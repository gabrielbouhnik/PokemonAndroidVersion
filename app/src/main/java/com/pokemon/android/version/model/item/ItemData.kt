package com.pokemon.android.version.model.item

import com.pokemon.android.version.entity.item.ItemEntity
import com.pokemon.android.version.model.move.Move

open class ItemData(var id: Int, var name: String) {
    companion object {
        fun of(itemEntity: ItemEntity): ItemData {
            return ItemData(itemEntity.id, itemEntity.name)
        }

        fun of(id: Int, move: Move): ItemData {
            var name = "TM"
            if (id - 50 < 10)
                name += "0"
            name += (id - 50).toString() + " " + move.name
            return ItemData(id,name)
        }
    }
}