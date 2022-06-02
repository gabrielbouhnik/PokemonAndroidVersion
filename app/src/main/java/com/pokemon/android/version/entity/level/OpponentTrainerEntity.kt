package com.pokemon.android.version.entity.level

data class OpponentTrainerEntity(var sprite : String, var name : String,
                                 var pokemons : List<PokemonOpponentTrainerEntity>) {
}