package com.pokemon.android.version.model

import com.pokemon.android.version.model.item.Ball
import com.pokemon.android.version.model.item.Item
import com.pokemon.android.version.ui.LevelMenu
import com.pokemon.android.version.utils.ItemUtils
import java.util.*
import kotlin.math.pow
import kotlin.random.Random

class Trainer(var name: String, gender: Gender) : ITrainer {
    var pokemons: ArrayList<Pokemon> = ArrayList()
    var gender: Gender? = gender
    var team: ArrayList<Pokemon> = ArrayList()
    var items: HashMap<Int, Int> = HashMap()
    var progression: Int = 1
    var coins: Int = 50
    var lastTimeDailyHealUsed: Date? = null
    var eliteProgression: Int = 0
    var battleTowerProgression: BattleFrontierProgression? = null
    var battleFactoryProgression: BattleFrontierProgression? = null
    var successfulAchievements: ArrayList<Int> = arrayListOf()
    var achievements: Achievements? = null
    var pokedex: HashMap<Int, Boolean> = HashMap()

    override fun getFirstPokemonThatCanFight(): Pokemon? {
        for (pokemon in team) {
            if (pokemon.currentHP > 0)
                return pokemon
        }
        return null
    }

    fun catchPokemon(pokemon: Pokemon, ballId: Int): Boolean {
        if (pokemon.trainer != null)
            return false
        items[ballId] = items[ballId]!! - 1
        if (items[ballId] == 0) {
            items.remove(ballId)
        }
        var status = 1f
        if (pokemon.status == Status.BURN || pokemon.status == Status.POISON || pokemon.status == Status.BADLY_POISON || pokemon.status == Status.PARALYSIS || pokemon.status == Status.FROZEN)
            status = 1.5f
        if (pokemon.status == Status.ASLEEP)
            status = 2.5f
        val ball: Ball = ItemUtils.getItemById(ballId) as Ball
        var successRate = ball.successRate
        if (ball is Ball.NetBall && (pokemon.data.type1 == Type.WATER || pokemon.data.type1 == Type.BUG
                    || pokemon.data.type2 == Type.WATER || pokemon.data.type2 == Type.BUG)
        )
            successRate *= 3
        val catch: Int =
            (((1f - ((2f / 3f) * (pokemon.currentHP / pokemon.hp).toFloat())) * status) * pokemon.data.catchRate.toFloat() * successRate).toInt()
        if (catch >= 255) {
            receivePokemon(pokemon)
            if (ball is Ball.HealBall) {
                pokemon.currentHP = pokemon.hp
            }
            return true
        } else {
            for (i in 0..2) {
                val random: Int = Random.nextInt(65535)
                if (random > 65535 * (catch / 255f).toDouble().pow(1 / 4.toDouble()))
                    return false
            }
            receivePokemon(pokemon)
            if (ball is Ball.HealBall) {
                pokemon.currentHP = pokemon.hp
            }
            return true
        }
    }

    fun receivePokemon(pokemon: Pokemon) {
        pokemon.trainer = this
        pokedex[pokemon.data.id] = true
        pokemon.currentExp = ExpGaugeType.getExpGauge(pokemon)
        pokemons.add(pokemon)
        if (this.team.size < 6)
            team.add(pokemon)
    }

    fun addItem(id: Int, quantity: Int) {
        if (items.contains(id)) {
            if (items[id]!! + quantity < 99)
                items[id] = items[id]!! + quantity
            else
                items[id] = 99
        } else
            items[id] = quantity
    }

    fun useItem(id: Int, pokemon: Pokemon): Boolean {
        if (ItemUtils.isBall(id))
            return false
        if (items.contains(id)) {
            val item: Item = ItemUtils.getItemById(id)
            if (item.isUsable(pokemon)) {
                items[id] = items[id]!! - 1
                if (items[id] == 0) {
                    items.remove(id)
                }
                item.apply(pokemon)
                return true
            }
        }
        return false
    }

    fun heal(pokemon: Pokemon) {
        if (items.contains(10) && ItemUtils.getItemById(10).isUsable(pokemon))
            useItem(10, pokemon)
        if (pokemon.currentHP == 0) {
            if (items.contains(9))
                useItem(9, pokemon)
            else
                return
        }
        while (pokemon.currentHP < pokemon.hp && items.contains(1)) {
            useItem(1, pokemon)
        }
        while (pokemon.currentHP < pokemon.hp && items.contains(2)) {
            useItem(2, pokemon)
        }
        while (pokemon.currentHP < pokemon.hp && items.contains(3)) {
            useItem(3, pokemon)
        }
        if (items.contains(4) && pokemon.status != Status.OK)
            useItem(4, pokemon)
    }

    override fun canStillBattle(): Boolean {
        for (pokemon in team) {
            if (pokemon.currentHP > 0)
                return true
        }
        return false
    }

    fun receiveExp(exp: Int) {
        for (pokemon in team) {
            if (pokemon.currentHP > 0)
                pokemon.gainExp(exp)
        }
    }

    fun getMaxLevel(): Int {
        if (progression >= LevelMenu.ELITE_4_LAST_LEVEL_ID)
            return 80
        if (items.contains(38))
            return 70
        if (items.contains(37))
            return 60
        if (items.contains(36))
            return 55
        if (items.contains(35))
            return 50
        if (items.contains(34))
            return 45
        if (items.contains(33))
            return 40
        if (items.contains(32))
            return 35
        if (items.contains(31))
            return 30
        return 20
    }

    fun updatePokedex(pokemon: Pokemon) {
        if (!pokedex.containsKey(pokemon.data.id))
            pokedex[pokemon.data.id] = false
    }
}