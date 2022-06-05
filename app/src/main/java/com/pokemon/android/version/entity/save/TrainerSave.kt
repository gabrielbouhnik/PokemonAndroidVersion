package com.pokemon.android.version.entity.save

import com.pokemon.android.version.model.Trainer
import com.pokemon.android.version.model.item.ItemQuantity

class TrainerSave(var name: String,
                  var gender: String,
                  var team: List<PokemonSave>,
                  var pokemons: List<PokemonSave>,
                  var items: List<ItemQuantity>,
                  var coins: Int,
                  var progression: Int) {
    companion object{
        fun of(trainer : Trainer)  : TrainerSave{
            return TrainerSave(trainer.name, trainer.gender.toString().uppercase(),
                trainer.team.map {PokemonSave.of(it)},
                trainer.pokemons.filter{!trainer.team.contains(it)}.map { PokemonSave.of(it)},
                ItemQuantity.createItemQuantityFromHashMap(trainer.items),
                trainer.coins,
                trainer.progression
            )
        }
    }
}