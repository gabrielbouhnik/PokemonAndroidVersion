package com.pokemon.android.version.model.battle

import com.pokemon.android.version.model.Ability
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.model.move.pokemon.PokemonMove

class PokemonBattleData(
    var statsMultiplier: StatsMultiplier = StatsMultiplier(),
    var sleepCounter: Int = 0,
    var confusionCounter: Int = 0,
    var tauntCounter: Int = 0,
    var chargedCounter: Int = 0,
    var trapCounter: Int = 0,
    var poisonCounter: Int = 0,
    var haxxCounter: Int = 0,
    var unableToMoveCounter: Int = 0,
    var battleStatus: HashSet<Status> = hashSetOf(),
    var chargedMove: PokemonMove? = null,
    var rampageMove: PokemonMove? = null,
    var rampageCounter: Int = 0,
    var lastHitReceived: LastHitReceived? = null,
    var lastMoveFailed: Boolean = false,
    var lastMoveUsed: PokemonMove? = null,
    var sameMoveCounter: Int = 0,
    var hadATurn: Boolean = false,
    var child: Boolean = false,
    var numberOfHitTaken: Int = 0,
    var abilities: ArrayList<Ability> = arrayListOf(),
    var magnetRiseCounter: Int = 0,
    var battleType1: Type? = null,
    var battleType2: Type? = null,
    var transformData: TransformData? = null,
) {
}