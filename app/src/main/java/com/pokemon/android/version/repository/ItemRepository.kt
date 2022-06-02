package com.pokemon.android.version.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.entity.ItemEntity
import com.pokemon.android.version.utils.JsonFileToString

class ItemRepository : Repository<ItemEntity> {
    override fun loadData(activity: MainActivity) : List<ItemEntity> {
        val itemsJsonString: String = JsonFileToString.loadJsonStringFromAssets(
            activity,
            GameDataService.ITEMS_DATA_PATH
        ) ?: return ArrayList()
        val gson = Gson()
        var result : List<ItemEntity> = ArrayList()
        val items = object : TypeToken<List<ItemEntity>>() {}.type
        result = gson.fromJson(itemsJsonString, items)
        result.forEach{ item -> Log.d("item name:", item.name) }
        return result
    }
}