package com.pokemon.android.version.model.banner

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.banner.PokemonBannerEntity
import com.pokemon.android.version.model.PokemonData
import com.pokemon.android.version.model.move.Move

class PokemonBanner(
    var pokemonId: Int,
    var name: String,
    var moves : List<Move>,
    private var rate: Int
) : Summonable {
    companion object {
        fun of(pokemonBannerEntity: PokemonBannerEntity, gameDataService: GameDataService): PokemonBanner {
            return PokemonBanner(pokemonBannerEntity.id, gameDataService.getPokemonDataById(pokemonBannerEntity.id).name, pokemonBannerEntity.moveIds.map{gameDataService.getMoveById(it)},pokemonBannerEntity.rate)
        }
    }

    override fun getRate(): Int {
        return rate
    }
}
