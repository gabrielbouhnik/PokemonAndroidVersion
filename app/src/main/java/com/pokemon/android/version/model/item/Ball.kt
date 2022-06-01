package com.pokemon.android.version.model.item

import com.pokemon.android.version.exception.ItemCannotBeUsedException
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Trainer
import kotlin.random.Random
import kotlin.random.nextInt

abstract class Ball(val successRate : Int) : Item{
    override fun apply(pokemon: Pokemon) {
        throw ItemCannotBeUsedException()
    }

    fun catch(pokemon: Pokemon, trainer : Trainer) : Boolean{
        if (pokemon.trainer != null){
            throw ItemCannotBeUsedException()
        }
        if (Random.nextInt(0..100) < this.successRate){
            trainer.catchPokemon(pokemon)
            return true;
        }
        else
            return false;
    }
}