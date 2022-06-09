package com.pokemon.android.version.model

import android.widget.Button

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
    FIGHTING(11),
    PSYCHIC(12),
    GHOST(13),
    ICE(14),
    DRAGON(15),
    STEEL(16),
    DARK(17),
    FAIRY(18);

    fun isEffectiveAgainst(other: Type): Float {
        if (other == NONE)
            return 1f
        when (this) {
            GRASS -> when (other) {
                WATER, GROUND, ROCK -> return 2f
                GRASS, FIRE, FLYING, BUG, POISON, DRAGON, STEEL -> return 0.5f
                else -> return 1f
            }
            FIRE -> when (other) {
                GRASS, BUG, ICE, STEEL -> return 2f
                FIRE, WATER, ROCK, DRAGON -> return 0.5f
                else -> return 1f
            }
            WATER -> when (other) {
                FIRE, GROUND, ROCK -> return 2f
                GRASS, WATER, DRAGON -> return 0.5f
                else -> return 1f
            }
            ELECTRIC -> when (other) {
                WATER, FLYING -> return 2f
                ELECTRIC, DRAGON -> return 0.5f
                GROUND -> return 0f
                else -> return 1f
            }
            NORMAL -> when (other) {
                ROCK, STEEL -> return 0.5f
                GHOST -> return 0f
                else -> return 1f
            }
            FLYING -> when (other) {
                GRASS, BUG, FIGHTING -> return 2f
                ROCK, STEEL -> return 0.5f
                else -> return 1f
            }
            BUG -> when (other) {
                GRASS, PSYCHIC, DARK -> return 2f
                FIRE, FLYING, BUG, POISON, FIGHTING, STEEL, FAIRY -> return 0.5f
                else -> return 1f
            }
            POISON -> when (other) {
                GRASS, FAIRY -> return 2f
                POISON, ROCK, GROUND, GHOST -> return 0.5f
                STEEL -> return 0f
                else -> return 1f
            }
            ROCK -> when (other) {
                FIRE, BUG, DARK, FLYING, ICE -> return 2f
                FIGHTING, STEEL -> return 0.5f
                else -> return 1f
            }
            GROUND -> when (other) {
                FIRE, POISON, ROCK, STEEL -> return 2f
                GRASS, BUG -> return 0.5f
                FLYING -> return 0f
                else -> return 1f
            }
            FIGHTING -> when (other) {
                NORMAL, ROCK, ICE, STEEL, DARK -> return 2f
                FLYING, BUG, POISON, PSYCHIC, FAIRY -> return 0.5f
                GHOST -> return 0f
                else -> return 1f
            }
            PSYCHIC -> when (other) {
                POISON, FIGHTING -> return 2f
                PSYCHIC, STEEL -> return 0.5f
                DARK -> return 0f
                else -> return 1f
            }
            GHOST -> when (other) {
                PSYCHIC, GHOST -> return 2f
                DARK -> return 0.5f
                NORMAL -> return 0f
                else -> return 1f
            }
            ICE -> when (other) {
                GRASS, FLYING, GROUND, DRAGON -> return 2f
                FIRE, WATER, ICE, STEEL -> return 0.5f
                else -> return 1f
            }
            DRAGON -> when (other) {
                DRAGON -> return 2f
                STEEL -> return 0.5f
                FAIRY -> return 0f
                else -> return 1f
            }
            STEEL -> when (other) {
                ROCK, ICE, FAIRY -> return 2f
                FIRE, WATER, ELECTRIC, FIGHTING, STEEL -> return 0.5f
                else -> return 1f
            }
            DARK -> when (other) {
                PSYCHIC, GHOST -> return 2f
                FIGHTING, DARK, FAIRY -> return 0.5f
                else -> return 1f
            }
            FAIRY -> when (other) {
                FIGHTING, DARK, DRAGON -> return 2f
                FIRE, POISON, STEEL -> return 0.5f
                else -> return 1f
            }
        }
        return 1f
    }

    companion object {
        fun of(value: Int) = Type.values().first { it.value == value }
    }
}