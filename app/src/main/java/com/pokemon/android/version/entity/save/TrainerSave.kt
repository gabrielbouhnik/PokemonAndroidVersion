package com.pokemon.android.version.entity.save

import com.pokemon.android.version.model.Trainer
import com.pokemon.android.version.model.item.ItemQuantity
import java.text.SimpleDateFormat

class TrainerSave(
    var name: String,
    var gender: String,
    var team: List<PokemonSave>,
    var pokemons: List<PokemonSave>,
    var items: List<ItemQuantity>,
    var coins: Int,
    var progression: Int,
    var lastTimeDailyHealUsed: String?,
    var eliteMode: Boolean?,
    var eliteProgression: Int
) {
    companion object {
        fun of(trainer: Trainer, eliteMode: Boolean): TrainerSave {
            return TrainerSave(trainer.name, trainer.gender.toString().uppercase(),
                trainer.team.map { PokemonSave.of(it) },
                trainer.pokemons.filter { !trainer.team.contains(it) }.map { PokemonSave.of(it) },
                ItemQuantity.createItemQuantityFromHashMap(trainer.items),
                trainer.coins,
                trainer.progression,
                if (trainer.lastTimeDailyHealUsed == null) null else SimpleDateFormat("yyyy-MM-dd").format(trainer.lastTimeDailyHealUsed),
                eliteMode,
                trainer.eliteProgression
            )
        }
    }
}