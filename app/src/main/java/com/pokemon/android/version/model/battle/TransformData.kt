package com.pokemon.android.version.model.battle

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.PokemonData
import com.pokemon.android.version.model.move.pokemon.PokemonMove

class TransformData(var move1: PokemonMove,
                    var move2: PokemonMove?,
                    var move3: PokemonMove?,
                    var move4: PokemonMove?,
                    var pokemonData: PokemonData) {
    companion object {
        fun transform(pokemon: Pokemon, opponent: Pokemon): TransformData {
            val move1  = PokemonMove(opponent.move1.move)
            val move2: PokemonMove? = if (opponent.move2 == null) null else PokemonMove(opponent.move2!!.move)
            val move3: PokemonMove? = if (opponent.move3 == null) null else PokemonMove(opponent.move3!!.move)
            val move4: PokemonMove? = if (opponent.move4 == null) null else PokemonMove(opponent.move4!!.move)
            pokemon.battleData!!.statsMultiplier = opponent.battleData!!.statsMultiplier
            pokemon.attack = opponent.attack
            pokemon.defense = opponent.defense
            pokemon.spAtk = opponent.spAtk
            pokemon.spDef = opponent.spDef
            pokemon.speed = opponent.speed
            pokemon.battleData!!.abilities.addAll(opponent.getPokemonData().abilities)
            return TransformData(move1, move2, move3, move4, opponent.data)
        }
    }
}