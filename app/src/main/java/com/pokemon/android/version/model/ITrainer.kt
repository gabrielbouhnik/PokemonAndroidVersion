package com.pokemon.android.version.model

interface ITrainer {
    fun canStillBattle(): Boolean

    fun getFirstPokemonThatCanFight(): Pokemon?

    fun getTrainerTeam(): List<Pokemon>
}