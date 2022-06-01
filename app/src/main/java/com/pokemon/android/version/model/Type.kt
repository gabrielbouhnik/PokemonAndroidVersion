package com.pokemon.android.version.model

enum class Type(val value: Int) {
    NONE(0),
    GRASS(1),
    FIRE(2),
    WATER(3),
    ELECTRIC(4),
    NORMAL(5),
    FLYING(6),
    BUG(7),
    POISON(8),
    ROCK(9),
    GROUND(10),
    FIGHTING(11);

    companion object {
        fun of(value: Int) = Type.values().first { it.value == value }
    }
}