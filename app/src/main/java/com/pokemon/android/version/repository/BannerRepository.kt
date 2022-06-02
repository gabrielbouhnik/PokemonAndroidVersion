package com.pokemon.android.version.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.entity.banner.BannerEntity
import com.pokemon.android.version.utils.JsonFileToString

class BannerRepository : Repository<BannerEntity> {
    override fun loadData(activity: MainActivity) : List<BannerEntity> {
        val BannersJsonString: String = JsonFileToString.loadJsonStringFromAssets(
            activity,
            GameDataService.BANNER_DATA_PATH
        ) ?: return ArrayList()
        val gson = Gson()
        var result : List<BannerEntity> = ArrayList()
        val Banners = object : TypeToken<List<BannerEntity>>() {}.type
        result = gson.fromJson(BannersJsonString, Banners)
        result.forEach{ banner -> Log.d("Banner description:", banner.description) }
        return result
    }
}