package com.pokemon.android.version.entity.move

data class MovesEntity(
    var basicMoves: List<MoveEntity>, var variableHitMoves: List<VariableHitMoveEntity>,
    var statsChangesMoves: List<StatChangeMoveEntity>, var drainMoves: List<DrainMoveEntity>,
    var recoilMoves: List<RecoilMoveEntity>
)