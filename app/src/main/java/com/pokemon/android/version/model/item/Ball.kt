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

    open fun catch(pokemon: Pokemon, trainer : Trainer) : Boolean{
        if (pokemon.trainer != null){
            throw ItemCannotBeUsedException()
        }
        if (Random.nextInt(0..100) < this.successRate){
            trainer.receivePokemon(pokemon)
            return true;
        }
        else
            return false;
    }

    class HealBall : Ball(20){
        override fun catch(pokemon: Pokemon, trainer : Trainer) : Boolean{
            if (pokemon.trainer != null){
                throw ItemCannotBeUsedException()
            }
            if (Random.nextInt(0..100) < this.successRate){
                pokemon.currentHP = pokemon.hp
                trainer.receivePokemon(pokemon)
                return true;
            }
            else
                return false;
        }
    }

    class NetBall : Ball(20){
        override fun catch(pokemon: Pokemon, trainer : Trainer) : Boolean{
            if (pokemon.trainer != null){
                throw ItemCannotBeUsedException()
            }
            var multiplicator = if (pokemon.data.type1 == Type.WATER || pokemon.data.type1 == Type.BUG
                || pokemon.data.type2 == Type.WATER || pokemon.data.type2 == Type.BUG) 3 else 1
            if (Random.nextInt(0..100) < this.successRate * multiplicator){
                pokemon.currentHP = pokemon.hp
                trainer.receivePokemon(pokemon)
                return true;
            }
            else
                return false;
        }
    }
}