package com.pokemon.android.version.model.banner

import android.annotation.SuppressLint
import android.widget.Toast
import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.entity.banner.BannerEntity
import kotlin.random.Random


class Banner(var description : String, var image : String, var cost : Int,
             var pokemons : List<PokemonBanner>, var items :List<ItemBanner>) {
    companion object{
        fun of(bannerEntity: BannerEntity, gameDataService: GameDataService) : Banner{
            return Banner(bannerEntity.description, bannerEntity.image, bannerEntity.cost,
            bannerEntity.pokemons.map{PokemonBanner.of(it, gameDataService)}, bannerEntity.items.map{ItemBanner(it.id, it.rate)})
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
    fun summon(activity: MainActivity){
        if (activity.trainer!!.coins >= cost){
            activity.trainer!!.coins -= cost
        }
        else
            return
        var s : Summonable = summon()
        if (s is ItemBanner){
            var itemBanner = s as ItemBanner
            Toast.makeText(activity, activity.gameDataService.items[itemBanner.id.minus(1)].name, Toast.LENGTH_LONG)
            activity.trainer!!.addItem(itemBanner.id)
        } else{
            var pokemonBanner = s as PokemonBanner
            Toast.makeText(activity, pokemonBanner.pokemonData.name, Toast.LENGTH_LONG)
            activity.trainer!!.catchPokemon(activity.gameDataService.generatePokemonFromBanner(pokemonBanner))
        }
    }
}