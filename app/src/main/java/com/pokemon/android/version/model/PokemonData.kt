package com.pokemon.android.version.model

import com.pokemon.android.version.entity.pokemon.PokemonDataEntity
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.model.move.pokemon.PokemonMoveLearnedByLevel

class PokemonData (val id : Int,
                   val name: String,
                   val type1 : Type,
                   val type2 : Type,
                   val possibleMoves : List<PokemonMove>,
                   val catchRate : Int,
                   var hp : Int,
                   var attack : Int,
                   var defense : Int,
                   var spAtk : Int,
                   var spDef : Int,
                   var speed : Int){
    companion object {
        fun of(pokemonDataEntity : PokemonDataEntity, moves : List<Move>) : PokemonData{
            var possibleMoves : ArrayList<PokemonMove> = ArrayList()
            pokemonDataEntity.possibleMoves.movesLearnByLevel.forEach{possibleMoves.add(PokemonMoveLearnedByLevel.of(it,moves))}
            pokemonDataEntity.possibleMoves.movesLearnWithHM.forEach{possibleMoves.add(PokemonMove.of(it,moves))}
            return PokemonDataBuilder()
                .id(pokemonDataEntity.id)
                .name(pokemonDataEntity.name)
                .type1(Type.of(pokemonDataEntity.type1))
                .type2(Type.of(pokemonDataEntity.type2))
                .possibleMoves(possibleMoves)
                .catchRate(pokemonDataEntity.catchRate)
                .hp(pokemonDataEntity.hp)
                .attack(pokemonDataEntity.attack)
                .defense(pokemonDataEntity.defense)
                .spAtk(pokemonDataEntity.spAtk)
                .spDef(pokemonDataEntity.spDef)
                .speed(pokemonDataEntity.speed)
                .build()
        }
    }

    data class PokemonDataBuilder(
        var id : Int = 0,
        var name : String = "",
        var type1 : Type = Type.NONE,
        var type2 : Type = Type.NONE,
        var possibleMoves : List<PokemonMove> = ArrayList(),
        var catchRate : Int =  0,
        var hp : Int  = 0,
        var attack : Int = 0,
        var defense : Int = 0,
        var spAtk : Int = 0,
        var spDef : Int = 0,
        var speed : Int = 0
    ){
        fun id(id : Int) = apply { this.id = id }
        fun name(name : String) = apply { this.name = name }
        fun type1(type: Type) = apply { this.type1 = type }
        fun type2(type: Type) = apply { this.type2 = type }
        fun possibleMoves(possibleMoves: List<PokemonMove>) = apply { this.possibleMoves = possibleMoves }
        fun catchRate(catchRate: Int) = apply { this.catchRate = catchRate }
        fun hp(hp: Int) = apply { this.hp = hp }
        fun attack(attack: Int) = apply { this.attack = attack }
        fun defense(defense: Int) = apply { this.defense = defense }
        fun spAtk(spAtk: Int) = apply { this.spAtk = spAtk }
        fun spDef(spDef: Int) = apply { this.spDef = spDef }
        fun speed(speed: Int) = apply { this.speed = speed }
        fun build() = PokemonData(id,name,type1,type2, possibleMoves, catchRate, hp, attack, defense, spAtk, spDef, speed)
    }
}