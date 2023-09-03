package com.pokemon.android.version.model.banner

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.SaveManager
import com.pokemon.android.version.entity.banner.BannerEntity
import java.time.DayOfWeek
import kotlin.random.Random


class Banner(
    var description: String, var image: String, var cost: Int,
    var pokemons: List<PokemonBanner>, var items: List<ItemBanner>, var days: List<DayOfWeek>
) {
    companion object {
        fun of(bannerEntity: BannerEntity, gameDataService: GameDataService): Banner {
            return Banner(bannerEntity.description,
                bannerEntity.image,
                bannerEntity.cost,
                bannerEntity.pokemons.map { PokemonBanner.of(it, gameDataService) },
                bannerEntity.items.map { ItemBanner(it.id, it.rate) },
                bannerEntity.days.map { DayOfWeek.valueOf(it) })
        }
    }

    private fun summon(): Summonable {
        val idx: Int = Random.nextInt(items.sumOf { it.getRate() })
        val summonables: List<Summonable> = items + pokemons
        var totalRates = 0
        for (s in summonables) {
            totalRates += s.getRate()
            if (idx <= totalRates)
                return s
        }
        return summonables.last()
    }

    fun summon(activity: MainActivity): Summonable? {
        if (activity.trainer!!.coins >= cost) {
            activity.trainer!!.coins -= cost
        } else {
            return null
        }
        val s: Summonable = summon()
        if (s is ItemBanner) {
            if (activity.trainer!!.items.contains(s.id) && ((description.contains("TM") && activity.trainer!!.items[s.id] == 10) || activity.trainer!!.items[s.id] == 99))
                activity.trainer!!.coins += cost
            else
                activity.trainer!!.addItem(s.id, 1)
        } else {
            val pokemonBanner = s as PokemonBanner
            activity.trainer!!.receivePokemon(activity.gameDataService.generatePokemonFromBanner(pokemonBanner))
        }
        SaveManager.save(activity)
        return s
    }
}