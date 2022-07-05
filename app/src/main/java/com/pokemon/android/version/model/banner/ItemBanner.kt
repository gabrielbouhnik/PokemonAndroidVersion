package com.pokemon.android.version.model.banner

class ItemBanner(var id: Int, private var rate: Int) : Summonable {
    override fun getRate(): Int {
        return rate
    }
}