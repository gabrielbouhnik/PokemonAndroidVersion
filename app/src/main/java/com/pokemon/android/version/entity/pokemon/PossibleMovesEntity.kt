package com.pokemon.android.version.entity.pokemon

data class PossibleMovesEntity(
    var movesLearnByLevel: List<MoveLearnByLevelEntity>,
    var movesLearnWithTM: List<Int>,
    var bannerMoves: List<Int>
)