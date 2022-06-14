package com.pokemon.android.version.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.pokemon.android.version.model.item.Ball
import com.pokemon.android.version.model.item.Item
import com.pokemon.android.version.utils.ItemUtils
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.random.Random

class Trainer  : ITrainer{
    var name: String = ""
    var pokemons: ArrayList<Pokemon> = ArrayList()
    var gender: Gender? = null
    var team: ArrayList<Pokemon> = ArrayList()
    var items: HashMap<Int, Int> = HashMap()
    var progression: Int = 1
    var coins: Int = 50
    var lastTimeDailyHealUsed : Date? = null

    constructor(name: String, gender: Gender) {
        this.name = name
        this.gender = gender
    }

    override fun getFirstPokemonThatCanFight(): Pokemon? {
        for (pokemon in team) {
            if (pokemon.currentHP > 0)
                return pokemon
        }
        return null
    }

    fun catchPokemon(pokemon : Pokemon, ballId : Int) : Boolean{
        if (pokemon.trainer != null)
            return false
        items[ballId] = items[ballId]!! - 1
        if (items[ballId] == 0) {
            items.remove(ballId)
        }
        val random : Int = Random.nextInt(3)
        var status = 1f
        if (pokemon.status == Status.BURN || pokemon.status == Status.POISON || pokemon.status == Status.PARALYSIS)
            status = 1.5f
        if (pokemon.status ==  Status.ASLEEP)
            status = 2.5f
        val ball : Ball = ItemUtils.getItemById(ballId) as Ball
        val catch : Int = ((1 - ((2/3)*(pokemon.currentHP/pokemon.hp))).toFloat() * status).toInt() * (pokemon.data.catchRate*100f).toInt()*ball.successRate
        if (catch * random > 300){
            receivePokemon(pokemon)
            return true
        }
        return false
    }

    fun receivePokemon(pokemon: Pokemon) {
        pokemon.trainer = this
        pokemon.currentExp = ExpGaugeType.getExpGauge(pokemon)
        pokemons.add(pokemon)
        if (this.team.size < 6)
            team.add(pokemon)
    }

    fun addItem(id: Int, quantity: Int) {
        if (items.contains(id)) {
            if (id > 13)
                return
            if ( items[id]!! + quantity < 99)
                items[id] = items[id]!! + quantity
            else
                items[id] = 99
        }
        else
            items[id] = quantity
    }

    fun useItem(id: Int, pokemon: Pokemon) : Boolean{
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

    override fun canStillBattle(): Boolean {
        for (pokemon in team) {
            if (pokemon.currentHP > 0)
                return true
        }
        return false
    }

    fun receiveExp(exp : Int){
        for (pokemon in team) {
            if (pokemon.currentHP > 0)
                pokemon.gainExp(exp)
        }
    }

    fun getMaxLevel(): Int{
        if (items.contains(38))
            return 100
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
}