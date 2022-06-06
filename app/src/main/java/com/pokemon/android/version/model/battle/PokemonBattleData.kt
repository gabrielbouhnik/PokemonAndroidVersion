package com.pokemon.android.version.model.battle

class PokemonBattleData(var attackMultiplicator : Float = 1F,
                        var defenseMultiplicator : Float = 1F,
                        var spAtkMultiplicator : Float = 1F,
                        var spDefMultiplicator : Float = 1F,
                        var speedMultiplicator : Float = 1F,
                        var accuracyMultiplicator : Float = 1F,
                        var critRate : Float = 1F,
                        var evasion : Float = 1F,
                        var sleepCounter : Int = 0,
                        var confusionCounter : Int = 0,
                        var poisonCounter : Int = 0) {
}