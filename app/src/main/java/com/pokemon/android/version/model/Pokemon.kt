package com.pokemon.android.version.model

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.save.PokemonSave
import com.pokemon.android.version.model.battle.DamageCalculator
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import kotlin.random.Random

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
                hp : Int, attack : Int, defense : Int, spAtk : Int, spDef : Int, speed : Int, currentHP: Int)
            : this(data, trainer, level, move1, move2, move3, move4, Status.OK, gender, hp, attack,defense,spAtk, spDef, speed, currentHP)

    companion object {
        fun of(pokemonSave : PokemonSave, gameDataService: GameDataService, trainer : Trainer) : Pokemon{
            return PokemonBuilder().data(gameDataService.getPokemonDataById(pokemonSave.id))
                .level(pokemonSave.level)
                .trainer(trainer)
                .status(Status.valueOf(pokemonSave.status))
                .hp(pokemonSave.hp)
                .attack(pokemonSave.attack)
                .defense(pokemonSave.defense)
                .spAtk(pokemonSave.spAtk)
                .spDef(pokemonSave.spDef)
                .speed(pokemonSave.speed)
                .currentHP(pokemonSave.currentHP)
                .gender(Gender.valueOf(pokemonSave.gender))
                .move1(PokemonMove(gameDataService.getMoveById(pokemonSave.moveids[0].id), pokemonSave.moveids[0].pp))
                .move2(if (pokemonSave.moveids.size > 1) PokemonMove(gameDataService.getMoveById(pokemonSave.moveids[1].id), pokemonSave.moveids[1].pp) else null)
                .move3(if (pokemonSave.moveids.size > 2) PokemonMove(gameDataService.getMoveById(pokemonSave.moveids[2].id), pokemonSave.moveids[2].pp) else null)
                .move4(if (pokemonSave.moveids.size > 3) PokemonMove(gameDataService.getMoveById(pokemonSave.moveids[3].id), pokemonSave.moveids[3].pp) else null)
                .build()
        }
    }

    fun IA(opponent : Pokemon) : PokemonMove{
        var usableMoves : ArrayList<PokemonMove> = arrayListOf()
        if (move1.pp > 0){
            usableMoves.add(move1)
        }
        if (move2 != null && move2!!.pp > 0){
            usableMoves.add(move2!!)
        }
        if (move3 != null && move3!!.pp > 0){
            usableMoves.add(move4!!)
        }
        if (move4 != null && move4!!.pp > 0){
            usableMoves.add(move4!!)
        }
        var maxDamage = 0
        var maxDamageIdx = 0
        var Idx = 0
        for (move in usableMoves){
            if (DamageCalculator.computeDamage(this,move.move, opponent) > maxDamage)
                maxDamageIdx = Idx
            Idx++
        }
        return usableMoves[maxDamageIdx]
    }

    fun attack(move : PokemonMove, opponent : Pokemon) : Boolean{
        if (this.status == Status.PARALYSIS){
            if (Random.nextInt(100) < 25)
                return false
        }
        move.pp = move.pp - 1
        if (move.move.accuracy < 100){
            var random : Int = Random.nextInt(100)
            if (random >= move.move.accuracy)
                return false
        }
        var damage = DamageCalculator.computeDamage(this,move.move, opponent)
        if (damage >= opponent.currentHP)
            opponent.currentHP = 0
        else
            opponent.currentHP = opponent.currentHP - damage
        return true
    }

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
        var speed : Int = 0,
        var currentHP: Int = 0)
        {
            fun data(data: PokemonData) = apply { this.data = data }
            fun trainer(trainer: Trainer) = apply { this.trainer = trainer }
            fun level(level: Int) = apply { this.level = level }
            fun status(status: Status) = apply { this.status = status }
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
            fun currentHP(currentHP: Int) = apply { this.currentHP = currentHP }
            fun build() = Pokemon(
                data!!,
                trainer,
                level,
                move1!!,
                move2,
                move3,
                move4,
                status,
                gender,
                hp,
                attack,
                defense,
                spAtk,
                spDef,
                speed,
                currentHP
            )

        }
}
