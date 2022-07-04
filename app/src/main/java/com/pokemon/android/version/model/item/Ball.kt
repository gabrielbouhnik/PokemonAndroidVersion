package com.pokemon.android.version.model.item

import com.pokemon.android.version.exception.ItemCannotBeUsedException
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Trainer
import com.pokemon.android.version.model.Type
import kotlin.random.Random
import kotlin.random.nextInt

open class Ball(val successRate : Float) : Item{
    companion object{
        val POKEBALL = Ball(1f)
        val HEALBALL = HealBall()
        val NETBALL = NetBall()
        val SUPERBALL = Ball(1.5f)
        val HYPERBALL = Ball(2f)
    }

    override fun isUsable(pokemon: Pokemon): Boolean {
        return false
    }

    override fun apply(pokemon: Pokemon) {
        throw ItemCannotBeUsedException()
    }

    class HealBall : Ball(1f){
    }

    class NetBall : Ball(1f){
    }
}