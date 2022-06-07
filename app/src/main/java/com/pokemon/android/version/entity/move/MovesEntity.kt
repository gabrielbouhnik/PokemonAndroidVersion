package com.pokemon.android.version.entity.move

data class MovesEntity (var basicMoves : List<MoveEntity>, var statusMoves : List<StatusMoveEntity>,
                        var critMoves: List<CritMoveEntity>, var variableHitMoves: List<VariableHitMoveEntity>,
                        var statsChangesMoves : List<StatChangeMoveEntity>){
}