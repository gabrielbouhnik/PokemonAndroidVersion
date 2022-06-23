package com.pokemon.android.version.model.item

import com.pokemon.android.version.entity.item.ItemsEntity
import com.pokemon.android.version.model.move.Move

class ItemFactory {
    companion object {
        fun createItems(itemsEntity: ItemsEntity?, moves : List<Move>) : List<ItemData>{
            val res : ArrayList<ItemData> = ArrayList()
            if (itemsEntity != null) {
                itemsEntity.items.forEach {res.add(ItemData.of(it))}
                itemsEntity.TMs.forEach {res.add(TMData.of(it, moves[it.moveId - 1]))}
            }
            res.sortBy{it.id}
            return res
        }
    }
}