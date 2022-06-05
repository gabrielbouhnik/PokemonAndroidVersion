package com.pokemon.android.version.model.item

import com.pokemon.android.version.exception.ItemCannotBeUsedException
import com.pokemon.android.version.model.Pokemon

class HealingHPItem(val heal : Int) : Item {
    companion object{
        val POTION = HealingHPItem(20)
        val SUPERPOTION = HealingHPItem(60)
        val HYPERPOTION = HealingHPItem(120)
    }

    override fun isUsable(pokemon: Pokemon): Boolean {
        return pokemon.currentHP > 0  && pokemon.currentHP < pokemon.hp
    }

    override fun apply(pokemon: Pokemon) {
        if (pokemon.currentHP + heal > pokemon.hp)
            pokemon.currentHP = pokemon.hp
        else
            pokemon.currentHP += heal
    }
}