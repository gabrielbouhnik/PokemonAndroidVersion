package com.pokemon.android.version.entity.pokemon

data class PokemonDataEntity(
    val id: Int, val name: String, val type1: Int, val type2: Int,
    val possibleMoves: PossibleMovesEntity, val hp: Int, val attack: Int,
    val defense: Int, val spAtk: Int, val spDef: Int, val speed: Int,
    val catchRate: Float, val evolutionId : Int?, val evolutionCondition : EvolutionConditionEntity?,
    val expGaugeType : String) {
}