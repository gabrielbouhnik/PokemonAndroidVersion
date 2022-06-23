package com.pokemon.android.version.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.entity.item.ItemEntity
import com.pokemon.android.version.entity.item.ItemsEntity
import com.pokemon.android.version.utils.JsonFileToString

class ItemsRepository {
    fun loadData(activity: MainActivity) : ItemsEntity? {
        val itemsJsonString: String = JsonFileToString.loadJsonStringFromAssets(
            activity,
            GameDataService.ITEMS_DATA_PATH
        ) ?: return null
        val gson = Gson()
        val result : ItemsEntity
        val items = object : TypeToken<ItemsEntity>() {}.type
        result = gson.fromJson(itemsJsonString, items)
        return result
    }
}