package com.pokemon.android.version.model.item

import com.pokemon.android.version.exception.ItemCannotBeUsedException
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Trainer
import com.pokemon.android.version.model.Type
import kotlin.random.Random
import kotlin.random.nextInt

open class Ball(val successRate : Int) : Item{
    companion object{
        val POKEBALL = Ball(20)
        val HEALBALL = HealBall()
        val NETBALL = NetBall()
        val SUPERBALL = Ball(40)
        val HYPERBALL = Ball(60)
    }

    override fun isUsable(pokemon: Pokemon): Boolean {
        return false
    }

    override fun apply(pokemon: Pokemon) {
        throw ItemCannotBeUsedException()
    }

    class HealBall : Ball(20){
    }

    class NetBall : Ball(20){
    }
}