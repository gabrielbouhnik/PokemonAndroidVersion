package com.pokemon.android.version.model.item

import com.pokemon.android.version.entity.item.ItemsEntity
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.utils.ItemUtils
import java.util.*
import kotlin.collections.ArrayList

class ItemFactory {
    companion object {
        fun createItems(itemsEntity: ItemsEntity?, moves: List<Move>): List<ItemData> {
            val res: ArrayList<ItemData> = ArrayList()
            itemsEntity?.items?.forEach { res.add(ItemData.of(it)) }
            var idx = 50
            ItemUtils.TM_MOVE_ID.forEach {
                idx++
                res.add(ItemData.of(idx,moves[it - 1]))
            }
            HoldItem.values().forEach{ res.add(ItemData(it.id, it.heldItemToString()))}
            res.sortBy { it.id }
            return res
        }
    }
}