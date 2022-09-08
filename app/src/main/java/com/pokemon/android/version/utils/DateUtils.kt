package com.pokemon.android.version.utils

import java.time.DayOfWeek

class DateUtils {
    companion object {
        fun getDayOfWeek(day: Int): DayOfWeek {
            return when (day) {
                0 -> DayOfWeek.valueOf("MONDAY")
                1 -> DayOfWeek.valueOf("TUESDAY")
                2 -> DayOfWeek.valueOf("WEDNESDAY")
                3 -> DayOfWeek.valueOf("THURSDAY")
                4 -> DayOfWeek.valueOf("FRIDAY")
                5 -> DayOfWeek.valueOf("SATURDAY")
                else -> DayOfWeek.valueOf("SUNDAY")
            }
        }
    }
}