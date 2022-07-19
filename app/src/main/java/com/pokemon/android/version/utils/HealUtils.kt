package com.pokemon.android.version.utils

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Trainer
import java.util.*

class HealUtils {
    companion object {
        fun canUseDailyHeal(trainer: Trainer): Boolean {
            if (trainer.lastTimeDailyHealUsed == null)
                return true
            return trainer.lastTimeDailyHealUsed == null || compareDates(trainer.lastTimeDailyHealUsed!!)
        }

        private fun compareDates(last: Date): Boolean {
            val lastUsed = last.toString().split(" ")
            val currentDate = Calendar.getInstance().time.toString().split(" ")
            return lastUsed[0] != currentDate[0] || lastUsed[2] != currentDate[2] || lastUsed[5] != currentDate[5]
        }

        fun healPP(pokemon: Pokemon) {
            pokemon.move1.pp = pokemon.move1.move.pp
            if (pokemon.move2 != null)
                pokemon.move2!!.pp = pokemon.move2!!.move.pp
            if (pokemon.move3 != null)
                pokemon.move3!!.pp = pokemon.move3!!.move.pp
            if (pokemon.move4 != null)
                pokemon.move4!!.pp = pokemon.move4!!.move.pp
        }

        fun healTeam(team: List<Pokemon>){
            team.forEach {
                it.currentHP = it.hp
                it.status = Status.OK
                healPP(it)
            }
        }

        fun dailyHeal(trainer: Trainer) {
            trainer.pokemons.forEach {
                it.currentHP = it.hp
                it.status = Status.OK
                healPP(it)
            }
            trainer.lastTimeDailyHealUsed = Calendar.getInstance().time
        }
    }
}