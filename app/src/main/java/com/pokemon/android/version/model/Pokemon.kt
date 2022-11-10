package com.pokemon.android.version.model

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.save.PokemonSave
import com.pokemon.android.version.model.battle.*
import com.pokemon.android.version.model.move.*
import com.pokemon.android.version.model.move.Target
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.utils.BattleUtils
import com.pokemon.android.version.utils.MoveUtils
import com.pokemon.android.version.utils.StatUtils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.random.nextInt

class Pokemon(
    var data: PokemonData,
    var trainer: ITrainer?,
    var level: Int,
    var move1: PokemonMove,
    var move2: PokemonMove?,
    var move3: PokemonMove?,
    var move4: PokemonMove?,
    var status: Status = Status.OK,
    var hp: Int = 0,
    var attack: Int = 0,
    var defense: Int = 0,
    var spAtk: Int = 0,
    var spDef: Int = 0,
    var speed: Int = 0,
    var currentHP: Int = 0,
    var currentExp: Int = 0,
    var shiny: Boolean,
    var battleData: PokemonBattleData?,
    var isFromBanner: Boolean = false,
    var movesLearnedByTM: ArrayList<Move> = arrayListOf(),
) {

    constructor(
        data: PokemonData, trainer: Trainer?, level: Int,
        move1: PokemonMove, move2: PokemonMove?, move3: PokemonMove?, move4: PokemonMove?,
        hp: Int, attack: Int, defense: Int, spAtk: Int, spDef: Int, speed: Int, currentHP: Int,
        shiny: Boolean
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
        hp,
        attack,
        defense,
        spAtk,
        spDef,
        speed,
        currentHP,
        0,
        shiny,
        PokemonBattleData()
    )

    companion object {
        fun of(pokemonSave: PokemonSave, gameDataService: GameDataService, trainer: Trainer): Pokemon {
            val data = gameDataService.getPokemonDataById(pokemonSave.id)
            return PokemonBuilder().data(data)
                .level(pokemonSave.level)
                .trainer(trainer)
                .status(Status.valueOf(pokemonSave.status))
                .hp(StatUtils.computeHP(data, pokemonSave.level))
                .attack(StatUtils.computeStat(data, pokemonSave.level, Stats.ATTACK))
                .defense(StatUtils.computeStat(data, pokemonSave.level, Stats.DEFENSE))
                .spAtk(StatUtils.computeStat(data, pokemonSave.level, Stats.SPATK))
                .spDef(StatUtils.computeStat(data, pokemonSave.level, Stats.SPDEF))
                .speed(StatUtils.computeStat(data, pokemonSave.level, Stats.SPEED))
                .currentHP(pokemonSave.currentHP)
                .currentExp(pokemonSave.currentExp)
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
                .shiny(pokemonSave.shiny)
                .isFromBanner(pokemonSave.isFromBanner)
                .movesLearnedByTM(arrayListOf())
                .build()
        }
    }

    private fun canBeKOdByOpponent(opponent: Pokemon): Boolean{
        val offensiveMove = MoveUtils.getMoveList(opponent).filter { it.pp > 0 && it.move.power > 0}
        for (move in offensiveMove){
            var damage: Int = DamageCalculator.computeDamage(opponent, move.move, this, 1f)
            if (move.move is MultipleHitMove || move.move is VariableHitMove)
                damage *= 2
            if (damage >= currentHP)
                return true
        }
        return false
    }

    fun ia(opponent: Pokemon): PokemonMove {
        val usableMoves = MoveUtils.getMoveList(this).filter { it.pp > 0 }
        if (battleData!!.chargedMove != null) {
            val move = battleData!!.chargedMove!!
            battleData!!.chargedMove = null
            return move
        }
        if (battleData!!.rampageMove != null)
            return battleData!!.rampageMove!!
        var maxDamage = 0
        var maxDamageIdx = 0
        for ((Idx, move) in usableMoves.withIndex()) {
            var damage: Int = DamageCalculator.computeDamage(this, move.move, opponent, 1f)
            if (move.move is MultipleHitMove || move.move is VariableHitMove)
                damage *= 2
            if (canBeKOdByOpponent(opponent) && move.move is ChargedMove)
                continue
            if (damage >= opponent.currentHP) {
                if (move.move !is ChargedMove || hp / currentHP > 2)
                    return move
            }
            if (damage > 0 && hp / currentHP < 4 && move.move.priorityLevel > 0 && speed * battleData!!.speedMultiplicator < opponent.speed * opponent.battleData!!.speedMultiplicator)
                return move
            move.move.status.forEach {
                if (Status.isAffectedByStatus(move.move.id, it.status, opponent) && it.probability == null)
                    return move
            }
            if (move.move is HealMove && hp / currentHP > 4)
                return move
            if (move.move is StatChangeMove) {
                val statChangeMove: StatChangeMove = move.move as StatChangeMove
                if (move.move.power == 0 &&
                    statChangeMove.statsAffected.contains(Stats.SPEED) && speed * battleData!!.speedMultiplicator < opponent.speed * opponent.battleData!!.speedMultiplicator
                )
                    return move
                if (statChangeMove.statsAffected.contains(Stats.ACCURACY) && opponent.battleData!!.accuracyMultiplicator == 1f)
                    return move
            }
            if (damage > maxDamage) {
                maxDamageIdx = Idx
                maxDamage = damage
            }
        }
        return usableMoves[maxDamageIdx]
    }

    private fun canAttack(move: PokemonMove): AttackResponse {
        if (this.status == Status.FROZEN) {
            if (Random.nextInt(100) > 20)
                return AttackResponse(false, "${this.data.name} is frozen solid!\n")
            else
                status = Status.OK
        }
        if (this.status == Status.PARALYSIS) {
            if (Random.nextInt(100) < 25)
                return AttackResponse(false, "${this.data.name} can't move because it is paralysed!\n")
        }
        if (this.status == Status.ASLEEP) {
            if (battleData!!.sleepCounter == 3) {
                battleData!!.sleepCounter = 0
                this.status = Status.OK
            } else if (battleData!!.sleepCounter > 1){
                if (Random.nextInt(100) < 30){
                    battleData!!.sleepCounter = 0
                    this.status = Status.OK
                }
                else
                    return AttackResponse(false, this.data.name + " is fast asleep!\n")
            }
            else
                return AttackResponse(false, this.data.name + " is fast asleep!\n")
        }
        if (this.battleData!!.battleStatus.contains(Status.UNABLE_TO_MOVE))
            return AttackResponse(false, "${this.data.name} needs to rest!\n")
        if (this.battleData!!.battleStatus.contains(Status.FLINCHED)) {
            var reason = "${this.data.name} flinched and couldn't move\n"
            if (this.hasAbility(Ability.STEADFAST)){
                reason += "Steadfast: ${this.data.name}'s speed rose!\n"
                this.battleData!!.speedMultiplicator *= 1.5f
            }
            return AttackResponse(false, reason)
        }
        if (this.battleData!!.battleStatus.contains(Status.CONFUSED)) {
            if (Random.nextInt(100) < 33) {
                val confusionDamage = DamageCalculator.computeConfusionDamage(this)
                if (currentHP > confusionDamage) {
                    currentHP -= confusionDamage
                } else {
                    currentHP = 0
                    status = Status.OK
                }
                return AttackResponse(
                    false,
                    "${this.data.name} uses ${move.move.name}\nBut ${this.data.name} hits hurt itself in its confusion\n"
                )
            }
        }
        return AttackResponse(true, "")
    }

    fun attack(move: PokemonMove, opponent: Pokemon): AttackResponse {
        val attackResponse = canAttack(move)
        if (!attackResponse.success)
            return attackResponse
        if (move.move.category != MoveCategory.OTHER && opponent.hasAbility(Ability.PRESSURE))
            move.pp = if (move.pp > 1) move.pp - 2 else 0
        else
            move.pp = move.pp - 1
        if (move.move.characteristics.contains(MoveCharacteristic.SOUND) && opponent.hasAbility(Ability.SOUNDPROOF))
            return AttackResponse(
                false,
                "${this.data.name} uses ${move.move.name}\nSoundproof: It does not affect ${opponent.data.name}!\n"
            )
        if (move.move.type == Type.ELECTRIC && opponent.hasAbility(Ability.LIGHTNING_ROD)) {
            opponent.battleData!!.spAtkMultiplicator *= 1.5f
            return AttackResponse(
                false,
                "${this.data.name} uses ${move.move.name}\nLightning Rod: ${opponent.data.name}'s Sp. Atk rose!\n"
            )
        }
        if (move.move.type == Type.FIRE && opponent.hasAbility(Ability.FLASH_FIRE)) {
            if(!opponent.battleData!!.battleStatus.contains(Status.FIRED_UP))
                opponent.battleData!!.battleStatus.add(Status.FIRED_UP)
            return AttackResponse(
                false,
                "${this.data.name} uses ${move.move.name}\nFlash Fire: ${opponent.data.name}'s fire power is increased!\n"
            )
        }
        if (move.move.type == Type.GROUND && opponent.hasAbility(Ability.LEVITATE))
            return AttackResponse(
                false,
                "${this.data.name} uses ${move.move.name}\nLevitate: It does not affect ${opponent.data.name}!\n"
            )
        if ((move.move.type == Type.WATER && (opponent.hasAbility(Ability.WATER_ABSORB) || opponent.hasAbility(Ability.DRY_SKIN)))
                    || (move.move.type == Type.ELECTRIC && opponent.hasAbility(Ability.VOLT_ABSORB))){
            val heal = DamageCalculator.computeDamageWithoutAbility(this, move.move, opponent)
            if (heal + this.currentHP > this.hp)
                this.currentHP = hp
            else
                this.currentHP += heal
        }
        if (move.move.accuracy != null && !this.hasAbility(Ability.NO_GUARD)) {
            val random: Int = Random.nextInt(100)
            if (random > move.move.accuracy!! * battleData!!.accuracyMultiplicator)
                return AttackResponse(
                    false,
                    "${this.data.name} uses ${move.move.name}\n${this.data.name}'s attack missed!\n"
                )
        }
        var details = ""
        if (move.move is HealMove)
            HealMove.heal(this)
        if (move.move is UltimateMove)
            this.battleData!!.battleStatus.add(Status.UNABLE_TO_MOVE)
        var damage = 0
        if (move.move.power > 0) {
            if (move.move is MoveBasedOnLevel){
                damage = this.level
            } else if (move.move is VariableHitMove) {
                val timesItHits = Random.nextInt(2..5)
                var i = 0
                while (i < timesItHits && opponent.currentHP > damage) {
                    val crit = DamageCalculator.getCriticalMultiplicator(this, move.move,opponent)
                    if (crit == 1.5f)
                        details += "A critical hit!\n"
                    damage += DamageCalculator.computeDamage(
                        this,
                        move.move,
                        opponent,
                        crit
                    )
                    i++
                }
                details =
                    if (timesItHits > 1) "${opponent.data.name} was hit $timesItHits times!\n" else "${opponent.data.name} was hit 1 time!\n"
            } else if (move.move is MultipleHitMove){
                val crit = DamageCalculator.getCriticalMultiplicator(this, move.move,opponent)
                if (crit == 1.5f)
                    details += "A critical hit!\n"
                damage += DamageCalculator.computeDamage(
                    this,
                    move.move,
                    opponent,
                    crit
                )
                if (damage >= opponent.currentHP){
                    details += "${opponent.data.name} was hit 1 time!\n"
                }
                else{
                    DamageCalculator.getCriticalMultiplicator(this, move.move,opponent)
                    if (crit == 1.5f)
                        details += "A critical hit!\n"
                    damage += DamageCalculator.computeDamage(
                        this,
                        move.move,
                        opponent,
                        crit
                    )
                    details += "${opponent.data.name} was hit 2 times!\n"
                }
            }
            else {
                val crit = DamageCalculator.getCriticalMultiplicator(this, move.move,opponent)
                if (crit == 1.5f)
                    details += "A critical hit!\n"
                damage = DamageCalculator.computeDamage(
                    this,
                    move.move,
                    opponent,
                    crit
                )

            }
        }
        if (move.move.power > 0)
            details += BattleUtils.getEffectiveness(move.move,opponent)
        val damageDone: Int
        if (damage >= opponent.currentHP) {
            if (opponent.currentHP == opponent.hp
                && move.move !is MultipleHitMove
                && move.move !is VariableHitMove
                && opponent.hasAbility(Ability.STURDY)) {
                damageDone = opponent.currentHP - 1
                opponent.currentHP = 1
                details += "Sturdy: ${opponent.data.name} endured the hit!\n"
            }
            else {
                damageDone = opponent.currentHP
                opponent.currentHP = 0
                opponent.status = Status.OK
                if (opponent.hasAbility(Ability.AFTERMATH) && move.move.characteristics.contains(MoveCharacteristic.CONTACT)) {
                    details += "Aftermath: ${this.data.name} loses some hp!\n"
                    currentHP = if (hp/4 > currentHP) 0 else currentHP - hp/4
                }
            }
        } else {
            damageDone = damage
            opponent.currentHP = opponent.currentHP - damage
            if (move.move.type == Type.FIRE && opponent.status == Status.FROZEN)
                opponent.status = Status.OK
            if (!this.hasAbility(Ability.SHEER_FORCE) && (move.move.power == 0 || damage > 0 && move.move.status.isNotEmpty())) {
                details += Status.updateStatus(this, opponent, move.move)
            }
            if (!this.hasAbility(Ability.SHEER_FORCE) && move.move is StatChangeMove && (move.move.power == 0 || damage > 0)) {
                var randomForStats: Int = Random.nextInt(100)
                if (this.hasAbility(Ability.SERENE_GRACE))
                    randomForStats /= 2
                if ((move.move as StatChangeMove).probability == null || randomForStats <= (move.move as StatChangeMove).probability!!) {
                    val statChangeMove = move.move as StatChangeMove
                    details += if (statChangeMove.target == Target.SELF)
                        Stats.updateStat(this, move.move as StatChangeMove)
                    else {
                        if ((move.move as StatChangeMove).multiplicator < 0 && hasAbility(Ability.CLEAR_BODY))
                            details += "Clear Body: ${data.name}'s stats cannot be lowered!\n"
                        else
                            Stats.updateStat(opponent, move.move as StatChangeMove)
                    }
                }
            }
        }
        if (move.move is DrainMove) {
            if (opponent.hasAbility(Ability.LIQUID_OOZE)){
                currentHP = if (damageDone / 2 > currentHP) 0 else currentHP - damageDone / 2
                details += "Liquid Ooze: ${this.data.name} loses some hp.\n"
            }
            else {
                currentHP = if (currentHP + damageDone / 2 > hp) hp else currentHP + damageDone / 2
                details += "The opposing ${opponent.data.name} had its energy drained!\n"
            }
        }
        if (move.move is RecoilMove) {
            if (!this.hasAbility(Ability.ROCK_HEAD)) {
                val recoil = (move.move as RecoilMove).recoil
                if (currentHP > (damageDone * recoil.damage).toInt()) {
                    currentHP -= (damageDone * recoil.damage).toInt()
                } else {
                    currentHP = 0
                    status = Status.OK
                }
                details += "${this.data.name} is damaged by recoil!\n"
            }
            else{
                details += "Rock Head: ${this.data.name} does not receive recoil damage!\n"
            }
        }
        details += BattleUtils.contactMovesCheck(this, move.move, opponent)
        return AttackResponse(true, details)
    }

    fun isMegaEvolved() : Boolean{
        return data.megaEvolutionData != null && data.megaEvolutionData!!.isMegaEvolved
    }
    
    fun canMegaEvolve() : Boolean{
        return trainer != null && data.megaEvolutionData != null && !data.megaEvolutionData!!.isMegaEvolved
                && trainer!!.getTrainerTeam().none { it.isMegaEvolved()}
                && (trainer is OpponentTrainer || (trainer as Trainer).items.containsKey(30))
    }

    fun megaEvolve(){
        if (data.megaEvolutionData != null && !data.megaEvolutionData!!.isMegaEvolved){
            this.attack = StatUtils.computeMegaEvolutionStat(data.megaEvolutionData!!, level, Stats.ATTACK)
            this.defense = StatUtils.computeMegaEvolutionStat(data.megaEvolutionData!!, level, Stats.DEFENSE)
            this.spAtk = StatUtils.computeMegaEvolutionStat(data.megaEvolutionData!!, level, Stats.SPATK)
            this.spDef = StatUtils.computeMegaEvolutionStat(data.megaEvolutionData!!, level, Stats.SPDEF)
            this.speed = StatUtils.computeMegaEvolutionStat(data.megaEvolutionData!!, level, Stats.SPEED)
            data.megaEvolutionData!!.isMegaEvolved = true
        }
    }
    
    fun recomputeStat() {
        if (isMegaEvolved())
            this.data.megaEvolutionData!!.isMegaEvolved = false
        val addHP: Boolean = hp == currentHP
        this.hp = StatUtils.computeHP(data, level)
        this.attack = StatUtils.computeStat(data, level, Stats.ATTACK)
        this.defense = StatUtils.computeStat(data, level, Stats.DEFENSE)
        this.spAtk = StatUtils.computeStat(data, level, Stats.SPATK)
        this.spDef = StatUtils.computeStat(data, level, Stats.SPDEF)
        this.speed = StatUtils.computeStat(data, level, Stats.SPEED)
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

    private fun gainLevel() {
        this.level += 1
        (this.trainer!! as Trainer).coins += 10
        this.recomputeStat()
        val moveFiltered = data.movesByLevel.filter { it.level == this.level }
        if (moveFiltered.size == 1)
            autoLearnMove(data.movesByLevel.first { it.level == this.level }.move)
    }

    fun gainExp(value: Int) {
        val maxLevel = (trainer!! as Trainer).getMaxLevel()
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

    private fun meetsEvolutionCondition(evolution: Evolution): Boolean {
        val condition = evolution.evolutionCondition
        if (condition.level != null && level >= condition.level!!) {
            if (condition.dayTime != null){
                val rightNow = Calendar.getInstance()
                val currentHourIn24Format: Int =rightNow.get(Calendar.HOUR_OF_DAY)
                if (condition.dayTime == "DAY")
                    return currentHourIn24Format in 6..18
                if (condition.dayTime == "NIGHT")
                    return currentHourIn24Format in 19..24 || currentHourIn24Format in 0..6
            }
            return true
        }
        if (condition.itemId != null && (this.trainer!! as Trainer).items.containsKey(condition.itemId))
            return true
        if (condition.moveId != null
            && MoveUtils.getMoveList(this).map{it.move.id}.contains(condition.moveId))
            return true
        return false
    }

    fun canEvolve(): Boolean {
        if (data.evolutions.isEmpty())
            return false
        return if (data.evolutions.size == 1) {
            meetsEvolutionCondition(data.evolutions[0])
        } else {
            data.evolutions.any { meetsEvolutionCondition(it) }
        }
    }

    fun getPossibleEvolutions(): List<Int> {
        return data.evolutions.filter { meetsEvolutionCondition(it) }.map { it.evolutionId }
    }

    fun evolve(gameDataService: GameDataService, evolutionId: Int) {
        if (canEvolve()) {
            val evolution = data.evolutions.first { it.evolutionId == evolutionId }
            if (evolution.evolutionCondition.itemId != null) {
                (this.trainer!! as Trainer).useItem(evolution.evolutionCondition.itemId!!, this)
            }
            this.data = gameDataService.getPokemonDataById(evolutionId)
            (this.trainer!! as Trainer).pokedex[this.data.id] = true
            recomputeStat()
            if (currentHP > hp)
                currentHP = hp
            val moveFiltered = data.movesByLevel.filter { it.level == this.level }
            if (moveFiltered.size == 1)
                autoLearnMove(data.movesByLevel.first { it.level == this.level }.move)
        }
    }

    fun hasAbility(ability: Ability) : Boolean{
        return data.abilities.contains(ability) && !isMegaEvolved()
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
        var hp: Int = 0,
        var attack: Int = 0,
        var defense: Int = 0,
        var spAtk: Int = 0,
        var spDef: Int = 0,
        var speed: Int = 0,
        var currentHP: Int = 0,
        var currentExp: Int = 0,
        var shiny: Boolean = false,
        var isFromBanner: Boolean = false,
        var movesLearnedByTM: ArrayList<Move> = arrayListOf()
    ) {
        fun data(data: PokemonData) = apply { this.data = data }
        fun trainer(trainer: Trainer) = apply { this.trainer = trainer }
        fun level(level: Int) = apply { this.level = level }
        fun status(status: Status) = apply { this.status = status }
        fun move1(move: PokemonMove) = apply { this.move1 = move }
        fun move2(move: PokemonMove?) = apply { this.move2 = move }
        fun move3(move: PokemonMove?) = apply { this.move3 = move }
        fun move4(move: PokemonMove?) = apply { this.move4 = move }
        fun hp(hp: Int) = apply { this.hp = hp }
        fun attack(attack: Int) = apply { this.attack = attack }
        fun defense(defense: Int) = apply { this.defense = defense }
        fun spAtk(spAtk: Int) = apply { this.spAtk = spAtk }
        fun spDef(spDef: Int) = apply { this.spDef = spDef }
        fun speed(speed: Int) = apply { this.speed = speed }
        fun currentHP(currentHP: Int) = apply { this.currentHP = currentHP }
        fun currentExp(currentExp: Int) = apply { this.currentExp = currentExp }
        fun shiny(shiny: Boolean) = apply { this.shiny = shiny }
        fun isFromBanner(isFromBanner: Boolean) = apply { this.isFromBanner = isFromBanner }
        fun movesLearnedByTM(movesLearnedByTM: ArrayList<Move>) = apply { this.movesLearnedByTM = movesLearnedByTM }
        fun build() = Pokemon(
            data!!,
            trainer,
            level,
            move1!!,
            move2,
            move3,
            move4,
            status,
            hp,
            attack,
            defense,
            spAtk,
            spDef,
            speed,
            currentHP,
            currentExp,
            shiny,
            PokemonBattleData(),
            isFromBanner,
            movesLearnedByTM
        )

    }
}
