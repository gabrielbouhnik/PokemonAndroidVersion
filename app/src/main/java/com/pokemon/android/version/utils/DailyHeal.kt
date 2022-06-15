package com.pokemon.android.version.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Trainer
import java.time.LocalDateTime
import java.util.*

class DailyHeal {
    companion object{
        fun canUseDailyHeal(trainer : Trainer) :Boolean{
            return trainer.lastTimeDailyHealUsed == null || trainer.lastTimeDailyHealUsed!!.compareTo(Calendar.getInstance().time) < 0
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