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
            HoldItem.values().forEach{ item ->
                res.add(ItemData(item.id,
                    item.toString().replace("_", " ").lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }))
            }
            res.sortBy { it.id }
            return res
        }
    }
}