package com.pokemon.android.version.utils

import com.pokemon.android.version.model.Trainer
import java.time.DayOfWeek
import java.util.*

class DateUtils {
    companion object {
        fun canUseDailyHeal(trainer: Trainer): Boolean {
            return trainer.lastTimeDailyHealUsed == null || compareDates(trainer.lastTimeDailyHealUsed!!)
        }

        fun canUseFreeSummon(trainer: Trainer): Boolean {
            return trainer.lastFreeSummon == null || compareDates(trainer.lastFreeSummon!!)
        }

        fun compareDates(last: Date): Boolean {
            val lastCal = Calendar.getInstance().apply { time = last }
            val currentCal = Calendar.getInstance()
            return lastCal.get(Calendar.YEAR) != currentCal.get(Calendar.YEAR) ||
                    lastCal.get(Calendar.DAY_OF_YEAR) != currentCal.get(Calendar.DAY_OF_YEAR)
        }

        fun getDayOfWeek(day: Int): DayOfWeek {
            return when (day) {
                1 -> DayOfWeek.valueOf("SUNDAY")
                2 -> DayOfWeek.valueOf("MONDAY")
                3 -> DayOfWeek.valueOf("TUESDAY")
                4 -> DayOfWeek.valueOf("WEDNESDAY")
                5 -> DayOfWeek.valueOf("THURSDAY")
                6 -> DayOfWeek.valueOf("FRIDAY")
                else -> DayOfWeek.valueOf("SATURDAY")
            }
        }
    }
}