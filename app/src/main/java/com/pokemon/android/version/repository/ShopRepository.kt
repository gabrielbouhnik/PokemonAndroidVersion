package com.pokemon.android.version.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.entity.shop.ShopItemEntity
import com.pokemon.android.version.entity.shop.ShopItemsEntity
import com.pokemon.android.version.utils.JsonFileToString

class ShopRepository: Repository<ShopItemEntity> {
    override fun loadData(activity: MainActivity): List<ShopItemEntity> {
        val shopJsonString: String = JsonFileToString.loadJsonStringFromAssets(
            activity,
            GameDataService.SHOP_DATA_PATH
        ) ?: return ArrayList()
        val gson = Gson()
        val result: ShopItemsEntity
        val items = object : TypeToken<ShopItemsEntity>() {}.type
        result = gson.fromJson(shopJsonString, items)
        return result.items
    }
}