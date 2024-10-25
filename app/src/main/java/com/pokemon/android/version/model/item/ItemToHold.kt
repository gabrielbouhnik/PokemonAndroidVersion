package com.pokemon.android.version.model.item

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Trainer

class ItemToHold(private var holdItem: HoldItem) : Item {
    override fun isUsable(pokemon: Pokemon): Boolean {
        return pokemon.data.id != 104 && pokemon.data.id != 105 && pokemon.data.id != 10105
    }

    override fun apply(pokemon: Pokemon) {
        if (pokemon.heldItem != null){
            (pokemon.trainer!! as Trainer).addItem(pokemon.heldItem!!.id,1)
        }
        pokemon.heldItem = holdItem;
    }
}