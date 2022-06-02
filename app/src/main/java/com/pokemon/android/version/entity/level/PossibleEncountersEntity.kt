package com.pokemon.android.version.entity.level

data class PossibleEncountersEntity(var minLevel   : Int,
                                    var maxLevel   : Int,
                                    var encounters : ArrayList<Int>) {
}