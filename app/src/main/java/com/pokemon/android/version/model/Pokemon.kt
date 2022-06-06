package com.pokemon.android.version.model

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.save.PokemonSave
import com.pokemon.android.version.model.battle.AttackResponse
import com.pokemon.android.version.model.battle.DamageCalculator
import com.pokemon.android.version.model.battle.PokemonBattleData
import com.pokemon.android.version.model.move.CritMove
import com.pokemon.android.version.model.move.StatusMove
import com.pokemon.android.version.model.move.VariableHitMove
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt

class Pokemon (var data : PokemonData,
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
               var currentExp : Int  = 0,
               var battleData : PokemonBattleData?)
{


    constructor(data: PokemonData, trainer: Trainer?, level: Int,
                move1: PokemonMove, move2: PokemonMove?, move3: PokemonMove?, move4: PokemonMove?, gender : Gender?,
                hp : Int, attack : Int, defense : Int, spAtk : Int, spDef : Int, speed : Int, currentHP: Int)
            : this(data, trainer, level, move1, move2, move3, move4, Status.OK, gender, hp, attack,defense,spAtk, spDef, speed, currentHP, 0, PokemonBattleData())

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
            usableMoves.add(move3!!)
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

    fun attack(move : PokemonMove, opponent : Pokemon) : AttackResponse {
        if (this.status == Status.PARALYSIS){
            if (Random.nextInt(100) < 25)
                return AttackResponse(false,this.data.name + " can't move because it is paralysed!\n")
        }
        if (this.status == Status.ASLEEP){
            if (battleData!!.sleepCounter == 2){
                this.status = Status.OK
            }
            else
                return AttackResponse(false,this.data.name + " is fast asleep!\n")
        }
        move.pp = move.pp - 1
        if (move.move.accuracy < 100){
            var random : Int = Random.nextInt(100)
            if (random * battleData!!.accuracyMultiplicator >= move.move.accuracy)
                return AttackResponse(false,this.data.name + "'s attack missed!\n")
        }
        var damage = DamageCalculator.computeDamage(this,move.move, opponent)
        if (move.move is VariableHitMove){
           var timesItHits = Random.nextInt(1..4)
            while(timesItHits > 0){
                damage += DamageCalculator.computeDamage(this,move.move, opponent)
                timesItHits--
            }
        }
        if (damage >= opponent.currentHP) {
            opponent.currentHP = 0
            opponent.status = Status.OK
        }
        else {
            opponent.currentHP = opponent.currentHP - damage
            if (move.move is StatusMove){
                val statusMove : StatusMove = move.move as StatusMove
                val randomForStatus : Int = Random.nextInt(100)
                if (randomForStatus < statusMove.probability)
                    opponent.status = statusMove.status
            }
        }
        return AttackResponse(true,"")
    }

    private fun recomputeStat(){
        this.hp = 10 + (data.hp.toFloat() * (level/50f)).roundToInt()
        this.attack = 5 + (data.attack.toFloat() * (level/50f)).roundToInt()
        this.defense = 5 + (data.defense.toFloat() * (level/50f)).roundToInt()
        this.spAtk= 5 + (data.spAtk.toFloat() * (level/50f)).roundToInt()
        this.spDef = 5 + (data.spDef.toFloat() * (level/50f)).roundToInt()
        this.speed= 5 + (data.speed.toFloat() * (level/50f)).roundToInt()
    }

    fun gainLevel(){
        this.level += 1
        this.recomputeStat()
        //TODO moves
    }

    fun gainExp(value : Int){
        var exp = value
        while (this.currentExp + exp >= level * 15 * data.expGaugeCoeff ){
            exp -= (level * 15 * data.expGaugeCoeff - this.currentExp).toInt()
            this.currentExp = 0
            gainLevel()
        }
        this.currentExp += exp
    }

    fun canEvolve() : Boolean{
        if (data.evolutionId == null)
            return false
        var condition = data.evolutionCondition
        if (condition!!.level != null && level >= condition.level!!){
            return true
        }
        return false
    }

    fun evolve(gameDataService: GameDataService){
        if (canEvolve()){
            this.data = gameDataService.getPokemonDataById(data.evolutionId!!)
            recomputeStat()
        }
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
        var currentHP: Int = 0,
        var currentExp : Int = 0)
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
                currentHP,
                0,
                PokemonBattleData()
            )

        }
}
