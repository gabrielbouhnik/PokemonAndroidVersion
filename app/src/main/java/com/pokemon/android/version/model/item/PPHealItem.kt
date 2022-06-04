package com.pokemon.android.version.model.item

import com.pokemon.android.version.model.Pokemon

class PPHealItem() : Item{
    companion object{
        val PP_HEAL = PPHealItem()
    }

    override fun isUsable(pokemon: Pokemon): Boolean {
        if (pokemon.move1.pp < pokemon.move1.move.pp)
            return true
        if (pokemon.move2 != null && pokemon.move2!!.pp < pokemon.move2!!.move.pp)
            return true
        if (pokemon.move3 != null && pokemon.move3!!.pp < pokemon.move3!!.move.pp)
            return true
        if (pokemon.move4 != null && pokemon.move4!!.pp < pokemon.move4!!.move.pp)
            return true
        return false
    }

    override fun apply(pokemon: Pokemon) {
        pokemon.move1.pp = pokemon.move1.move.pp
        if (pokemon.move2 != null)
            pokemon.move2!!.pp = pokemon.move2!!.move.pp
        if (pokemon.move3 != null)
            pokemon.move3!!.pp = pokemon.move3!!.move.pp
        if (pokemon.move4 != null)
            pokemon.move4!!.pp = pokemon.move4!!.move.pp
    }
}