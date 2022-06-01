package com.pokemon.android.version.model.item

import com.pokemon.android.version.model.Pokemon

abstract class HealingHPItem(val heal : Int) : Item {
    override fun apply(pokemon: Pokemon) {
        pokemon.hp += heal
    }
}