package com.pokemon.android.version.entity.save

import com.pokemon.android.version.model.Trainer

class TrainerSave(var name: String,
                  var gender: String,
                  var team: List<PokemonSave>,
                  var pokemons: List<PokemonSave>,
                  var items: List<ItemSave>,
                  var coins: Int,
                  var progression: Int) {
    companion object{
        fun of(trainer : Trainer)  : TrainerSave{
            return TrainerSave(trainer.name, trainer.gender.toString(),
                trainer.team.map {PokemonSave.of(it)},
                trainer.pokemons.filter{!trainer.team.contains(it)}.map { PokemonSave.of(it)},
                listOf(ItemSave(1,10)),
                trainer.coins,
                trainer.progression
            )
        }
    }
}