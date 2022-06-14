package com.pokemon.android.version.model.banner

import android.annotation.SuppressLint
import android.widget.Toast
import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.entity.banner.BannerEntity
import java.time.DayOfWeek
import kotlin.random.Random


class Banner(var description : String, var image : String, var cost : Int,
             var pokemons : List<PokemonBanner>, var items :List<ItemBanner>, var days : List<DayOfWeek>) {
    companion object{
        fun of(bannerEntity: BannerEntity, gameDataService: GameDataService) : Banner{
            return Banner(bannerEntity.description, bannerEntity.image, bannerEntity.cost,
            bannerEntity.pokemons.map{PokemonBanner.of(it, gameDataService)}, bannerEntity.items.map{ItemBanner(it.id, it.rate)},
            bannerEntity.days.map{DayOfWeek.valueOf(it)})
        }
    }

    private fun summon() : Summonable{
        var idx : Int = Random.nextInt(100)
        var summonables : List<Summonable> = items + pokemons
        var totalRates : Int = 0
        for (s in summonables){
            totalRates += s.getRate()
            if (idx <= totalRates)
                return s
        }
        return summonables.last()
    }

    @SuppressLint("ShowToast")
    fun summon(activity: MainActivity) : Summonable?{
        if (activity.trainer!!.coins >= cost){
            activity.trainer!!.coins -= cost
        }
        else
            return null
        val s : Summonable = summon()
        if (s is ItemBanner){
            val itemBanner = s as ItemBanner
            Toast.makeText(activity, activity.gameDataService.items[itemBanner.id.minus(1)].name, Toast.LENGTH_LONG)
            activity.trainer!!.addItem(itemBanner.id, 1)
        } else{
            val pokemonBanner = s as PokemonBanner
            Toast.makeText(activity, pokemonBanner.pokemonData.name, Toast.LENGTH_LONG)
            activity.trainer!!.receivePokemon(activity.gameDataService.generatePokemonFromBanner(pokemonBanner))
        }
        SaveManager.save(activity)
        return s
    }
}