package com.pokemon.android.version.model

import com.pokemon.android.version.model.move.pokemon.PokemonMove

class Pokemon (val data : PokemonData,
               var trainer : Trainer?,
               var level : Int,
               var move1 : PokemonMove,
               var move2 : PokemonMove?,
               var move3 : PokemonMove?,
               var move4 : PokemonMove?,
               var status : Status = Status.OK,
               val gender : Gender?,
               var hp : Int  = 0,
               var attack : Int = 0,
               var defense : Int = 0,
               var spAtk : Int = 0,
               var spDef : Int = 0,
               var speed : Int = 0,
               var currentHP : Int  = 0,
               var attackMultiplicator : Float = 1F,
               var defenseMultiplicator : Float = 1F,
               var spAtkMultiplicator : Float = 1F,
               var spDefMultiplicator : Float = 1F,
               var speedMultiplicator : Float = 1F,
               var critRate : Float = 1F,
               var evasion : Float = 1F)
{


    constructor(data: PokemonData, trainer: Trainer?, level: Int,
                move1: PokemonMove, move2: PokemonMove?, move3: PokemonMove?, move4: PokemonMove?, gender : Gender?,
                hp : Int, attack : Int, defense : Int, spAtk : Int, spDef : Int, speed : Int)
            : this(data, trainer, level, move1, move2, move3, move4, Status.OK, gender, hp, attack,defense,spAtk, spDef, speed, hp)

    data class PokemonBuilder(
        var data : PokemonData? = null,
        var trainer : Trainer? = null,
        var level : Int = 1,
        var move1 : PokemonMove? = null,
        var move2 : PokemonMove? = null,
        var move3 : PokemonMove? = null,
        var move4 : PokemonMove? = null,
        var status : Status = Status.OK,
        var gender : Gender? = null,
        var hp : Int  = 0,
        var attack : Int = 0,
        var defense : Int = 0,
        var spAtk : Int = 0,
        var spDef : Int = 0,
        var speed : Int = 0)
        {
            fun data(data: PokemonData) = apply { this.data = data }
            fun trainer(trainer: Trainer) = apply { this.trainer = trainer }
            fun level(level: Int) = apply { this.level = level }
            fun move1(move: PokemonMove) = apply { this.move1 = move }
            fun move2(move: PokemonMove?) = apply { this.move2 = move }
            fun move3(move: PokemonMove?) = apply { this.move3 = move }
            fun move4(move: PokemonMove?) = apply { this.move4 = move }
            fun gender(gender: Gender) = apply { this.gender = gender }
            fun hp(hp: Int) = apply { this.hp = hp }
            fun attack(attack: Int) = apply { this.attack = attack }
            fun defense(defense: Int) = apply { this.defense = defense }
            fun spAtk(spAtk: Int) = apply { this.spAtk = spAtk }
            fun spDef(spDef: Int) = apply { this.spDef = spDef }
            fun speed(speed: Int) = apply { this.speed = speed }
            fun build() = Pokemon(
                data!!,
                trainer,
                level,
                move1!!,
                move2,
                move3,
                move4,
                gender,
                hp,
                attack,
                defense,
                spAtk,
                spDef,
                speed
            )

        }
}
