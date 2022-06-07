package com.pokemon.android.version.model

import com.pokemon.android.version.model.item.Ball
import com.pokemon.android.version.model.item.Item
import com.pokemon.android.version.utils.ItemUtils
import kotlin.random.Random

class Trainer  : ITrainer{
    var name: String = ""
    var pokemons: ArrayList<Pokemon> = ArrayList()
    var gender: Gender? = null
    var team: ArrayList<Pokemon> = ArrayList()
    var items: HashMap<Int, Int> = HashMap()
    var progression: Int = 1
    var coins: Int = 50

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
        var random : Int = Random.nextInt(3)
        var status = 1f
        if (pokemon.status == Status.BURN || pokemon.status == Status.POISON || pokemon.status == Status.PARALYSIS)
            status = 1.5f
        if (pokemon.status ==  Status.ASLEEP)
            status = 2.5f
        var ball : Ball = ItemUtils.getItemById(ballId) as Ball
        var catch : Int = ((1 - ((2/3)*(pokemon.currentHP/pokemon.hp))).toFloat() * status).toInt() * (pokemon.data.catchRate*100f).toInt()*ball.successRate
        if (catch * random > 300){
            receivePokemon(pokemon)
            return true
        }
        return false
    }

    fun receivePokemon(pokemon: Pokemon) {
        pokemon.trainer = this
        pokemons.add(pokemon)
        if (this.team.size < 6)
            team.add(pokemon)
    }

    fun addItem(id: Int, quantity: Int) {
        if (items.contains(id)) {
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
            var item: Item = ItemUtils.getItemById(id)
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
}