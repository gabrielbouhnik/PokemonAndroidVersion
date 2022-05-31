package com.pokemon.android.version.model.item

import com.pokemon.android.version.model.Pokemon

interface Item {
    fun apply(pokemon : Pokemon);
}