package com.pokemon.android.version

import com.pokemon.android.version.model.Gender
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.PokemonData
import com.pokemon.android.version.model.item.ItemData
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.MoveFactory
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.model.move.pokemon.PokemonMoveLearnedByLevel
import com.pokemon.android.version.repository.ItemRepository
import com.pokemon.android.version.repository.MovesRepository
import com.pokemon.android.version.repository.PokemonRepository
import kotlin.math.roundToInt

import kotlin.random.Random
import kotlin.random.nextInt

class GameDataService {
    var items : List<ItemData> = ArrayList()
    var moves : List<Move> = ArrayList()
    var pokemons : List<PokemonData> = ArrayList()

    companion object {
        const val MOVES_DATA_PATH = "game_data/moves.json"
        const val ITEMS_DATA_PATH = "game_data/items.json"
        const val POKEMON_DATA_PATH = "game_data/pokemons.json"
    }

    fun loadGameData(activity: MainActivity){
        var pokemonRepository = PokemonRepository()
        var itemRepository = ItemRepository()
        var movesRepository = MovesRepository()
        this.items = itemRepository.loadData(activity).map{ItemData.of(it)}
        this.moves = MoveFactory.createMove(movesRepository.loadData(activity))
        this.pokemons = pokemonRepository.loadData(activity).map{PokemonData.of(it, moves)}
    }

    fun getPokemonDataById(id : Int): PokemonData{
        return pokemons.filter {it.id == id}.first()
    }

    fun getMoveById(id : Int) : Move{
        return moves.filter {it.id == id}.first()
    }

    fun generatePokemon(id : Int, level : Int) :  Pokemon {
        val pokemonData : PokemonData = pokemons.filter {it.id == id}.first()
        val hp : Int = 10 + (pokemonData.hp.toFloat() * (level/50f)).roundToInt()
        val attack : Int = 5 + (pokemonData.attack.toFloat() * (level/50f)).roundToInt()
        val defense : Int = 5 + (pokemonData.defense.toFloat() * (level/50f)).roundToInt()
        val spAtk : Int = 5 + (pokemonData.spAtk.toFloat() * (level/50f)).roundToInt()
        val spDef : Int = 5 + (pokemonData.spDef.toFloat() * (level/50f)).roundToInt()
        val speed : Int = 5 + (pokemonData.speed.toFloat() * (level/50f)).roundToInt()
        val moves : List<PokemonMove> = pokemonData.possibleMoves.filter { it is PokemonMoveLearnedByLevel && (it as PokemonMoveLearnedByLevel).level <= level }
        val move1 : PokemonMove = moves.last()
        val move2 : PokemonMove? = if (moves.size < 2) null else moves[moves.size - 2]
        val move3 : PokemonMove? = if (moves.size < 3) null else moves[moves.size - 3]
        val move4 : PokemonMove? = if (moves.size < 4) null else moves[moves.size - 4]
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
            .currentHP(hp)
            .gender(gender) //TODO gender lock
            .move1(move1)
            .move2(move2)
            .move3(move3)
            .move4(move4)
            .build()
    }
}