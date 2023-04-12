package com.pokemon.android.version.model.battle

import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.move.pokemon.PokemonMove

class PokemonBattleData(
    var attackMultiplicator: Float = 1F,
    var defenseMultiplicator: Float = 1F,
    var spAtkMultiplicator: Float = 1F,
    var spDefMultiplicator: Float = 1F,
    var speedMultiplicator: Float = 1F,
    var accuracyMultiplicator: Float = 1F,
    var criticalRate: Float = 1F,
    var sleepCounter: Int = 0,
    var confusionCounter: Int = 0,
    var trapCounter: Int = 0,
    var poisonCounter: Int = 0,
    var unableToMoveCounter: Int = 0,
    var battleStatus: ArrayList<Status> = arrayListOf(),
    var chargedMove: PokemonMove? = null,
    var rampageMove: PokemonMove? = null,
    var rampageCounter: Int = 0,
    var isMegaEvolved: Boolean = false
)