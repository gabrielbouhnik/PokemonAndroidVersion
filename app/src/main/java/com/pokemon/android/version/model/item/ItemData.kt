package com.pokemon.android.version.model.item

import com.pokemon.android.version.entity.ItemEntity

data class ItemData(var id : Int, var name : String) {
    companion object {
        fun of(itemEntity: ItemEntity): ItemData {
            return ItemData(itemEntity.id, itemEntity.name)
        }
    }
}