package com.pokemon.android.version.entity.move

data class MovesEntity(
    var basicMoves: List<MoveEntity>, var variableHitMoves: List<VariableHitMoveEntity>,
    var statsChangesMoves: List<StatChangeMoveEntity>, var drainMoves: List<DrainMoveEntity>,
    var recoilMoves: List<RecoilMoveEntity>, var healMoves: List<HealMoveEntity>,
    var multipleHitMoves : List<MultipleHitMoveEntity>, var movesBasedOnLevelEntity : List<MoveBasedOnLevelEntity>,
    var ultimateMoves: List<MoveEntity>, var movesBasedOnHPEntity: List<MoveEntity>
)