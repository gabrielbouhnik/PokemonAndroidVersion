package com.pokemon.android.version.model

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.save.PokemonSave
import com.pokemon.android.version.model.battle.AttackResponse
import com.pokemon.android.version.model.battle.DamageCalculator
import com.pokemon.android.version.model.battle.PokemonBattleData
import com.pokemon.android.version.model.move.*
import com.pokemon.android.version.model.move.Target
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.utils.MoveUtils
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt

class Pokemon(
    var data: PokemonData,
    var trainer: Trainer?,
    var level: Int,
    var move1: PokemonMove,
    var move2: PokemonMove?,
    var move3: PokemonMove?,
    var move4: PokemonMove?,
    var status: Status = Status.OK,
    val gender: Gender?,
    var hp: Int = 0,
    var attack: Int = 0,
    var defense: Int = 0,
    var spAtk: Int = 0,
    var spDef: Int = 0,
    var speed: Int = 0,
    var currentHP: Int = 0,
    var currentExp: Int = 0,
    var battleData: PokemonBattleData?,
    var isFromBanner : Boolean = false
) {

    constructor(
        data: PokemonData, trainer: Trainer?, level: Int,
        move1: PokemonMove, move2: PokemonMove?, move3: PokemonMove?, move4: PokemonMove?, gender: Gender?,
        hp: Int, attack: Int, defense: Int, spAtk: Int, spDef: Int, speed: Int, currentHP: Int
    )
            : this(
        data,
        trainer,
        level,
        move1,
        move2,
        move3,
        move4,
        Status.OK,
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

    companion object {
        fun of(pokemonSave: PokemonSave, gameDataService: GameDataService, trainer: Trainer): Pokemon {
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
                .currentExp(pokemonSave.currentExp)
                .gender(Gender.valueOf(pokemonSave.gender))
                .move1(PokemonMove(gameDataService.getMoveById(pokemonSave.moveids[0].id), pokemonSave.moveids[0].pp))
                .move2(
                    if (pokemonSave.moveids.size > 1) PokemonMove(
                        gameDataService.getMoveById(pokemonSave.moveids[1].id),
                        pokemonSave.moveids[1].pp
                    ) else null
                )
                .move3(
                    if (pokemonSave.moveids.size > 2) PokemonMove(
                        gameDataService.getMoveById(pokemonSave.moveids[2].id),
                        pokemonSave.moveids[2].pp
                    ) else null
                )
                .move4(
                    if (pokemonSave.moveids.size > 3) PokemonMove(
                        gameDataService.getMoveById(pokemonSave.moveids[3].id),
                        pokemonSave.moveids[3].pp
                    ) else null
                )
                .isFromBanner(pokemonSave.isFromBanner)
                .build()
        }
    }

    fun IA(opponent: Pokemon): PokemonMove {
        val usableMoves = MoveUtils.getMoveList(this).filter { it.pp > 0 }
        var maxDamage = 0
        var maxDamageIdx = 0
        var Idx = 0
        for (move in usableMoves) {
            val damage : Int = DamageCalculator.computeDamageIA(this, move.move, opponent)
            if (damage > opponent.currentHP)
                return move
            if (damage > 0 && hp/currentHP < 10 && move.move.priorityLevel > 0 && speed * battleData!!.speedMultiplicator < opponent.speed * opponent.battleData!!.speedMultiplicator)
                return move
            if (move.move is StatusMove && Status.isAffectedByStatus((move.move as StatusMove).status, opponent) && (move.move as StatusMove).probability == 100)
                return move
            if (move.move is StatChangeMove)
            {
                val statChangeMove : StatChangeMove = move.move as StatChangeMove
                if (move.move.power == 0 &&
                    statChangeMove.statsAffected.contains(Stats.SPEED) && speed * battleData!!.speedMultiplicator < opponent.speed * opponent.battleData!!.speedMultiplicator)
                    return move
                if (statChangeMove.statsAffected.contains(Stats.ACCURACY) && opponent.battleData!!.accuracyMultiplicator == 1f)
                    return move
            }
            if (damage > maxDamage) {
                maxDamageIdx = Idx
                maxDamage = damage
            }
            Idx++
        }
        return usableMoves[maxDamageIdx]
    }

    fun attack(move: PokemonMove, opponent: Pokemon): AttackResponse {
        if (this.battleData!!.battleStatus.contains(Status.FLINCHED))
            return AttackResponse(false, "But ${this.data.name} flinched and couldn't move\n")

        if (this.status == Status.PARALYSIS) {
            if (Random.nextInt(100) < 25)
                return AttackResponse(false, "But ${this.data.name} can't move because it is paralysed!\n")
        }
        if (this.status == Status.FROZEN) {
            if (Random.nextInt(100) > 20)
                return AttackResponse(false, "But ${this.data.name} is frozen solid!\n")
            else
                status = Status.OK;
        }
        if (this.status == Status.ASLEEP) {
            if (battleData!!.sleepCounter == 3) {
                battleData!!.sleepCounter = 0
                this.status = Status.OK
            } else
                return AttackResponse(false, this.data.name + " is fast asleep!\n")
        }
        if (this.battleData!!.battleStatus.contains(Status.CONFUSED)) {
            if (Random.nextInt(100) < 33) {
                val confusionDamage = DamageCalculator.computeConfusionDamage(this)
                if (confusionDamage >= opponent.currentHP) {
                    currentHP = 1
                    /*status = Status.OK
                    battleData = null*/
                } else {
                    currentHP -= confusionDamage
                }
                return AttackResponse(false, "But ${this.data.name} hits hurt itself in its confusion\n")
            }
        }
        move.pp = move.pp - 1
        if (move.move.accuracy != null) {
            val random: Int = Random.nextInt(100)
            if (random > move.move.accuracy!! * battleData!!.accuracyMultiplicator)
                return AttackResponse(false, this.data.name + "'s attack missed!\n")
        }
        var damage = 0
        if (move.move.power > 0) {
            damage = DamageCalculator.computeDamage(this, move.move, opponent)
            if (move.move is VariableHitMove) {
                var timesItHits = Random.nextInt(1..4)
                while (timesItHits > 0) {
                    damage += DamageCalculator.computeDamage(this, move.move, opponent)
                    timesItHits--
                }
            }
        }
        var damageDone = 0
        if (damage >= opponent.currentHP) {
            damageDone = opponent.currentHP
            opponent.currentHP = 0
            opponent.status = Status.OK
            opponent.battleData = null
        } else {
            damageDone = damage
            opponent.currentHP = opponent.currentHP - damage
            if (move.move.type == Type.FIRE && opponent.status == Status.FROZEN)
                opponent.status = Status.OK
            if (move.move is StatusMove) {
                Status.updateStatus(opponent, move.move as StatusMove)
            }
            if (move.move is StatChangeMove) {
                val statChangeMove = move.move as StatChangeMove
                if (statChangeMove.target == Target.SELF)
                    Stats.updateStat(this, move.move as StatChangeMove)
                else
                    Stats.updateStat(opponent, move.move as StatChangeMove)
            }
        }
        if (move.move is DrainMove) {
            currentHP = if (currentHP + damageDone/2 > hp) hp else currentHP + damageDone/2
        }
        return AttackResponse(true, "")
    }

    private fun recomputeStat() {
        val addHP: Boolean = hp == currentHP
        this.hp = 10 + (data.hp.toFloat() * (level / 50f)).roundToInt()
        this.attack = 5 + (data.attack.toFloat() * (level / 50f)).roundToInt()
        this.defense = 5 + (data.defense.toFloat() * (level / 50f)).roundToInt()
        this.spAtk = 5 + (data.spAtk.toFloat() * (level / 50f)).roundToInt()
        this.spDef = 5 + (data.spDef.toFloat() * (level / 50f)).roundToInt()
        this.speed = 5 + (data.speed.toFloat() * (level / 50f)).roundToInt()
        if (addHP)
            currentHP = hp
    }

    fun learnMove(moveToLearn: Move, moveToDeleteNumber: Int) {
        when (moveToDeleteNumber) {
            1 -> move1 = PokemonMove(moveToLearn)
            2 -> move2 = PokemonMove(moveToLearn)
            3 -> move3 = PokemonMove(moveToLearn)
            4 -> move4 = PokemonMove(moveToLearn)
        }
    }

    fun autoLearnMove(move: Move): Boolean {
        if (MoveUtils.getMoveList(this).map { it.move }.contains(move))
            return false
        if (move2 == null) {
            this.move2 = PokemonMove(move, move.pp)
            return true
        }
        if (move3 == null) {
            this.move3 = PokemonMove(move, move.pp)
            return true
        }
        if (move4 == null) {
            this.move4 = PokemonMove(move, move.pp)
            return true
        }
        return false
    }

    fun gainLevel() {
        this.level += 1
        this.trainer!!.coins += 10
        this.recomputeStat()
        val moveFiltered = data.movesByLevel.filter { it.level == this.level }
        if (moveFiltered.size == 1)
            autoLearnMove(data.movesByLevel.filter { it.level == this.level }.first().move)
    }

    fun gainExp(value: Int) {
        val maxLevel = trainer!!.getMaxLevel()
        if (this.level >= maxLevel)
            return
        var exp = value

        while (this.currentExp + exp >= ExpGaugeType.getExpToNextLevel(this)) {
            exp -= ExpGaugeType.getExpToNextLevel(this) - this.currentExp
            gainLevel()
            if (level == maxLevel)
                return
        }
        this.currentExp += exp
    }

    fun canEvolve(): Boolean {
        if (data.evolutionId == null)
            return false
        val condition = data.evolutionCondition
        if (condition!!.level != null && level >= condition.level!!) {
            return true
        }
        if (condition.itemId != null && this.trainer!!.items.containsKey(condition.itemId))
            return true
        return false
    }

    fun evolve(gameDataService: GameDataService) {
        if (canEvolve()) {
            if (this.data.evolutionCondition!!.itemId != null){
                this.trainer!!.useItem(this.data.evolutionCondition!!.itemId!!, this)
            }
            this.data = gameDataService.getPokemonDataById(data.evolutionId!!)
            recomputeStat()
            if (currentHP > hp)
                currentHP = hp
            val moveFiltered = data.movesByLevel.filter { it.level == this.level }
            if (moveFiltered.size == 1)
                autoLearnMove(data.movesByLevel.filter { it.level == this.level }.first().move)
        }
    }

    data class PokemonBuilder(
        var data: PokemonData? = null,
        var trainer: Trainer? = null,
        var level: Int = 1,
        var move1: PokemonMove? = null,
        var move2: PokemonMove? = null,
        var move3: PokemonMove? = null,
        var move4: PokemonMove? = null,
        var status: Status = Status.OK,
        var gender: Gender? = null,
        var hp: Int = 0,
        var attack: Int = 0,
        var defense: Int = 0,
        var spAtk: Int = 0,
        var spDef: Int = 0,
        var speed: Int = 0,
        var currentHP: Int = 0,
        var currentExp: Int = 0,
        var isFromBanner : Boolean = false
    ) {
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
        fun currentExp(currentExp: Int) = apply { this.currentExp = currentExp }
        fun isFromBanner(isFromBanner: Boolean) = apply { this.isFromBanner = isFromBanner }
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
            currentExp,
            PokemonBattleData(),
            isFromBanner
        )

    }
}
