package com.pokemon.android.version.model

import com.pokemon.android.version.entity.pokemon.PokemonDataEntity
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.pokemon.MoveLearned
import com.pokemon.android.version.model.move.pokemon.MoveLearnedByLevel

class PokemonData(
    val id: Int,
    val name: String,
    val type1: Type,
    val type2: Type,
    val movesByLevel: List<MoveLearnedByLevel>,
    val movesByTM: List<MoveLearned>,
    val bannerMoves: List<MoveLearned>,
    val catchRate: Int,
    var hp: Int,
    var attack: Int,
    var defense: Int,
    var spAtk: Int,
    var spDef: Int,
    var speed: Int,
    var evolutions: List<Evolution>,
    val expGaugeType: ExpGaugeType
) {
    companion object {
        fun of(pokemonDataEntity: PokemonDataEntity, moves: List<Move>): PokemonData {
            val movesByLevel: ArrayList<MoveLearnedByLevel> = ArrayList()
            val movesByTM: ArrayList<MoveLearned> = ArrayList()
            val bannerMoves: ArrayList<MoveLearned> = ArrayList()
            pokemonDataEntity.possibleMoves.movesLearnByLevel.forEach {
                movesByLevel.add(
                    MoveLearnedByLevel(
                        moves[it.moveId - 1],
                        it.level
                    )
                )
            }
            pokemonDataEntity.possibleMoves.movesLearnWithTM.forEach { movesByTM.add(MoveLearned(moves[it - 1])) }
            pokemonDataEntity.possibleMoves.bannerMoves.forEach { bannerMoves.add(MoveLearned(moves[it - 1])) }
            return PokemonDataBuilder()
                .id(pokemonDataEntity.id)
                .name(pokemonDataEntity.name)
                .type1(Type.of(pokemonDataEntity.type1))
                .type2(Type.of(pokemonDataEntity.type2))
                .movesByLevel(movesByLevel)
                .movesByTM(movesByTM)
                .bannerMoves(bannerMoves)
                .catchRate(pokemonDataEntity.catchRate)
                .hp(pokemonDataEntity.hp)
                .attack(pokemonDataEntity.attack)
                .defense(pokemonDataEntity.defense)
                .spAtk(pokemonDataEntity.spAtk)
                .spDef(pokemonDataEntity.spDef)
                .speed(pokemonDataEntity.speed)
                .evolutions(pokemonDataEntity.evolutions.map {
                    Evolution(
                        it.evolutionId,
                        EvolutionCondition.of(it.evolutionCondition)
                    )
                })
                .expGaugeType(ExpGaugeType.valueOf(pokemonDataEntity.expGaugeType))
                .build()
        }
    }

    data class PokemonDataBuilder(
        var id: Int = 0,
        var name: String = "",
        var type1: Type = Type.NONE,
        var type2: Type = Type.NONE,
        var movesByLevel: List<MoveLearnedByLevel> = ArrayList(),
        var movesByTM: List<MoveLearned> = ArrayList(),
        var bannerMoves: List<MoveLearned> = ArrayList(),
        var catchRate: Int = 255,
        var hp: Int = 0,
        var attack: Int = 0,
        var defense: Int = 0,
        var spAtk: Int = 0,
        var spDef: Int = 0,
        var speed: Int = 0,
        var evolutions: List<Evolution> = arrayListOf(),
        var expGaugeType: ExpGaugeType = ExpGaugeType.FAST
    ) {
        fun id(id: Int) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun type1(type: Type) = apply { this.type1 = type }
        fun type2(type: Type) = apply { this.type2 = type }
        fun movesByLevel(movesByLevel: List<MoveLearnedByLevel>) = apply { this.movesByLevel = movesByLevel }
        fun movesByTM(movesByTM: List<MoveLearned>) = apply { this.movesByTM = movesByTM }
        fun bannerMoves(bannerMoves: List<MoveLearned>) = apply { this.bannerMoves = bannerMoves }
        fun catchRate(catchRate: Int) = apply { this.catchRate = catchRate }
        fun hp(hp: Int) = apply { this.hp = hp }
        fun attack(attack: Int) = apply { this.attack = attack }
        fun defense(defense: Int) = apply { this.defense = defense }
        fun spAtk(spAtk: Int) = apply { this.spAtk = spAtk }
        fun spDef(spDef: Int) = apply { this.spDef = spDef }
        fun speed(speed: Int) = apply { this.speed = speed }
        fun evolutions(evolutions: List<Evolution>) = apply { this.evolutions = evolutions }
        fun expGaugeType(expGaugeType: ExpGaugeType) = apply { this.expGaugeType = expGaugeType }
        fun build() = PokemonData(
            id,
            name,
            type1,
            type2,
            movesByLevel,
            movesByTM,
            bannerMoves,
            catchRate,
            hp,
            attack,
            defense,
            spAtk,
            spDef,
            speed,
            evolutions,
            expGaugeType
        )
    }
}