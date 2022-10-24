package com.pokemon.android.version.utils

import java.time.DayOfWeek

class DateUtils {
    companion object {
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