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

    fun isEffectiveAgainst(other : Type) : Float{
        if (other == NONE)
            return 1f
        when(this){
            GRASS -> when(other){
                WATER, GROUND, ROCK -> 2f
                GRASS, FIRE, FLYING, BUG, POISON, DRAGON, STEEL -> 0.5f
                else -> 1f
            }
            FIRE -> when(other){
                GRASS, BUG, ICE, STEEL -> 2f
                FIRE,WATER,ROCK,DRAGON -> 0.5f
                else -> 1f
            }
            WATER -> when(other){
                FIRE, GROUND, ROCK -> 2f
                GRASS, WATER, DRAGON -> 0.5f
                else -> 1f
            }
            ELECTRIC -> when(other){
                WATER, FLYING -> 2f
                ELECTRIC, DRAGON-> 0.5f
                GROUND -> 0f
                else -> 1f
            }
            NORMAL -> when(other){
                ROCK, STEEL-> 0.5f
                GHOST -> 0f
                else -> 1f
            }
            FLYING -> when(other){
                GRASS, BUG, FIGHTING-> 2f
                ROCK, STEEL -> 0.5f
                else -> 1f
            }
            BUG -> when(other){
                GRASS,PSYCHIC,DARK -> 2f
                FIRE,FLYING,BUG,POISON, FIGHTING,STEEL,FAIRY -> 0.5f
                else -> 1f
            }
            POISON -> when(other) {
                GRASS, FAIRY -> 2f
                POISON, ROCK, GROUND, GHOST -> 0.5f
                STEEL -> 0f
                else -> 1f
            }
            ROCK -> when(other){
                GRASS,PSYCHIC,DARK -> 2f
                FIRE,FLYING,BUG,POISON, FIGHTING,STEEL,FAIRY -> 0.5f
                else -> 1f
            }
            GROUND -> when(other) {
                GRASS, FAIRY -> 2f
                POISON, ROCK, GROUND, GHOST -> 0.5f
                STEEL -> 0f
                else -> 1f
            }
            FIGHTING -> when(other) {
                NORMAL, ROCK, ICE, STEEL, DARK -> 2f
                FLYING, BUG, POISON, PSYCHIC, FAIRY -> 0.5f
                GHOST -> 0f
                else -> 1f
            }
            PSYCHIC -> when(other){
                POISON, FIGHTING-> 2f
                PSYCHIC, STEEL -> 0.5f
                DARK -> 0f
                else -> 1f
            }
            GHOST -> when(other){
                PSYCHIC,GHOST -> 2f
                DARK -> 0.5f
                NORMAL -> 0f
                else -> 1f
            }
            ICE -> when(other){
                GRASS,FLYING,GROUND, DRAGON -> 2f
                FIRE,WATER,ICE,STEEL -> 0.5f
                else -> 1f
            }
            DRAGON -> when(other){
                DRAGON -> 2f
                STEEL -> 0.5f
                FAIRY -> 0f
                else -> 1f
            }
            STEEL -> when(other){
                ROCK,ICE,FAIRY -> 2f
                FIRE,WATER,ELECTRIC,FIGHTING,STEEL -> 0.5f
                else -> 1f
            }
            DARK -> when(other){
                PSYCHIC,GHOST -> 2f
                FIGHTING,DARK,FAIRY -> 0.5f
                else -> 1f
            }
            FAIRY -> when(other){
                FIGHTING,DARK,DRAGON -> 2f
                FIRE,POISON,STEEL -> 0.5f
                else -> 1f
            }
        }
        return 1f
    }

    companion object {
        fun of(value: Int) = Type.values().first { it.value == value }
    }
}