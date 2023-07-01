package com.pokemon.android.version.utils

class SpriteUtils {
    companion object {
        fun getThreeDigitId(id: Int): String {
            val idForUrl = StringBuilder()
            if (id < 100)
                idForUrl.append('0')
            if (id < 10)
                idForUrl.append('0')
            idForUrl.append(id)
            return idForUrl.toString()
        }
    }
}