package com.pokemon.android.version.utils

import java.time.DayOfWeek

class DateUtils {
    companion object {
        fun getDayOfWeek(day: Int) : DayOfWeek {
            when(day){
                1 -> return DayOfWeek.valueOf("MONDAY")
                2 -> return DayOfWeek.valueOf("TUESDAY")
                3 -> return DayOfWeek.valueOf("WEDNESDAY")
                4 -> return DayOfWeek.valueOf("THURSDAY")
                5 -> return DayOfWeek.valueOf("FRIDAY")
                6 -> return DayOfWeek.valueOf("SATURDAY")
                else -> return DayOfWeek.valueOf("SUNDAY")
            }
        }
    }
}