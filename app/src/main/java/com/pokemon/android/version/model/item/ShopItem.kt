package com.pokemon.android.version.model.item

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.shop.ShopItemEntity

class ShopItem(var itemId: Int, var name: String, var cost: Int, var badgeCountNeeded: Int) {
    companion object {
        fun of(entity: ShopItemEntity, gameDataService: GameDataService): ShopItem{
            val itemName = gameDataService.items.first { it.id == entity.itemId }.name
            return ShopItem(entity.itemId,itemName,entity.cost,entity.badgeCountNeeded)
        }
    }
}