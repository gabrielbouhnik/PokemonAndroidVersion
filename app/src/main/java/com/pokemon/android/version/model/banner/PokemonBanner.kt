package com.pokemon.android.version.model.banner

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.banner.PokemonBannerEntity
import com.pokemon.android.version.model.PokemonData
import com.pokemon.android.version.model.move.Move

class PokemonBanner(var pokemonData: PokemonData,
                    var move1 : Move,
                    var move2 : Move?,
                    var move3 : Move?,
                    var move4 : Move?,
                    private var rate : Int )  : Summonable{
    companion object{
        fun of(pokemonBannerEntity : PokemonBannerEntity, gameDataService: GameDataService): PokemonBanner {
            var data = gameDataService.getPokemonDataById(pokemonBannerEntity.id)
            var move1 = gameDataService.getMoveById(pokemonBannerEntity.moveIds[0])
            var move2 = if (pokemonBannerEntity.moveIds.size > 1) gameDataService.getMoveById(pokemonBannerEntity.moveIds[1]) else null
            var move3 = if (pokemonBannerEntity.moveIds.size > 2) gameDataService.getMoveById(pokemonBannerEntity.moveIds[2]) else null
            var move4 = if (pokemonBannerEntity.moveIds.size > 3) gameDataService.getMoveById(pokemonBannerEntity.moveIds[3]) else null
            return PokemonBanner(data, move1, move2, move3, move4, pokemonBannerEntity.rate)
        }
    }

    override fun getRate(): Int {
        return rate
    }
}
