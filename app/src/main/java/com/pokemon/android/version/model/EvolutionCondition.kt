package com.pokemon.android.version.model

import com.pokemon.android.version.entity.pokemon.PokemonDataEntity

class EvolutionCondition(var level : Int?, var itemId : Int?) {
    companion object{
        fun of(pokemonDataEntity: PokemonDataEntity) : EvolutionCondition?{
            if (pokemonDataEntity.evolutionCondition == null)
                return null
            return EvolutionCondition(pokemonDataEntity.evolutionCondition.level, pokemonDataEntity.evolutionCondition.itemId)
        }
    }
}
