package com.pokemon.android.version.utils

import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Trainer
import java.time.LocalDate
import java.util.*

class DailyHeal {
    companion object{
        fun canUseDailyHeal(trainer : Trainer) :Boolean{
            if (trainer.lastTimeDailyHealUsed == null)
                return true
            return trainer.lastTimeDailyHealUsed == null || compareDates(trainer.lastTimeDailyHealUsed!!)
        }

        fun compareDates(last: Date): Boolean {
            val lastUsed = last.toString().split(" ")
            val currentDate = Calendar.getInstance().time.toString().split(" ")
            return !lastUsed[0].equals(currentDate[0]) || !lastUsed[2].equals(currentDate[2]) ||  !lastUsed[5].equals(currentDate[5])
        }

        fun heal(trainer : Trainer){
            trainer.pokemons.forEach{
                it.currentHP = it.hp
                it.status = Status.OK
                it.move1.pp = it.move1.move.pp
                if(it.move2 != null)
                    it.move2!!.pp = it.move2!!.move.pp
                if(it.move3 != null)
                    it.move3!!.pp = it.move3!!.move.pp
                if(it.move4 != null)
                    it.move4!!.pp = it.move4!!.move.pp
            }
            trainer.lastTimeDailyHealUsed = Calendar.getInstance().time
        }
    }
}