package com.pokemon.android.version.utils

import java.time.DayOfWeek

class DateUtils {
    companion object {
        fun getDayOfWeek(day: Int): DayOfWeek {
            return when (day) {
                1 -> DayOfWeek.valueOf("MONDAY")
                2 -> DayOfWeek.valueOf("TUESDAY")
                3 -> DayOfWeek.valueOf("WEDNESDAY")
                4 -> DayOfWeek.valueOf("THURSDAY")
                5 -> DayOfWeek.valueOf("FRIDAY")
                6 -> DayOfWeek.valueOf("SATURDAY")
                else -> DayOfWeek.valueOf("SUNDAY")
            }
        }
    }
}