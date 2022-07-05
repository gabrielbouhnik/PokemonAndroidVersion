package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.BossEntity
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.Stats

class BossData(var id: Int, var level: Int, var moves: List<Move>, var boostedStats: List<Stats>) {
    companion object {
        fun of(bossEntity: BossEntity, gameDataService: GameDataService): BossData {
            return BossData(bossEntity.id, bossEntity.level,
                bossEntity.moveIds.map { gameDataService.getMoveById(it) },
                bossEntity.boostedStats.map { Stats.valueOf(it) })
        }
    }
}