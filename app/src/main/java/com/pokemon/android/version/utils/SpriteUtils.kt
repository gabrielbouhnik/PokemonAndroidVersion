package com.pokemon.android.version.utils

class SpriteUtils {
    companion object {
        fun getThreeDigitId(id: Int): String {
            var newId = id
            val idForUrl = StringBuilder()
            if (newId > 10000)
                newId -= 10000
            if (newId < 100)
                idForUrl.append('0')
            if (newId < 10)
                idForUrl.append('0')
            idForUrl.append(newId)
            return idForUrl.toString()
        }
    }
}