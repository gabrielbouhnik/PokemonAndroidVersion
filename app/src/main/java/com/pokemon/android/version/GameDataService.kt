package com.pokemon.android.version

import com.pokemon.android.version.model.Gender
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.PokemonData
import com.pokemon.android.version.model.item.Item
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.model.move.pokemon.PokemonMoveLearnedByLevel


import kotlin.random.Random
import kotlin.random.nextInt

class GameDataService {
    var items : List<Item> = ArrayList()
    var moves : List<Move> = ArrayList()
    var pokemons : List<PokemonData> = ArrayList()

    fun loadGameData(){

    }

    fun generatePokemon(id : Int, level : Int) :  Pokemon {
        var pokemonData : PokemonData = pokemons.filter {it.id == id}.first()
        var hp : Int = 10 + pokemonData.hp * (if (level/50 == 0) 1 else (level/50))
        var attack : Int = 5 + pokemonData.attack * (if (level/50 == 0) 1 else (level/50))
        var defense : Int = 5 + pokemonData.defense * (if (level/50 == 0) 1 else (level/50))
        var spAtk : Int = 5 + pokemonData.spAtk * (if (level/50 == 0) 1 else (level/50))
        var spDef : Int = 5 + pokemonData.spDef * (if (level/50 == 0) 1 else (level/50))
        var speed : Int = 5 + pokemonData.speed * (if (level/50 == 0) 1 else (level/50))
        var moves : List<PokemonMove> = pokemonData.possible_moves.filter { it is PokemonMoveLearnedByLevel && (it as PokemonMoveLearnedByLevel).level <= level }
        var move1 : PokemonMove = moves.last()
        var move2 : PokemonMove? = if (moves.size < 2) null else moves.get(moves.size - 2)
        var move3 : PokemonMove? = if (moves.size < 3) null else moves.get(moves.size - 3)
        var move4 : PokemonMove? = if (moves.size < 4) null else moves.get(moves.size - 4)
        var gender : Gender = Gender.MALE
        if (Random.nextInt(1..10) > 5)
            gender = Gender.FEMALE
        return Pokemon.PokemonBuilder()
            .data(pokemonData)
            .level(level)
            .hp(hp)
            .attack(attack)
            .defense(defense)
            .spAtk(spAtk)
            .spDef(spDef)
            .speed(speed)
            .gender(gender) //TODO gender lock
            .move1(move1)
            .move2(move2)
            .move3(move3)
            .move4(move4)
            .build()
    }
}