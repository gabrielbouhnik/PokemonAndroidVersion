package com.pokemon.android.version.model

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.entity.achievement.AchievementEntity
import com.pokemon.android.version.ui.LevelMenu

class Achievement(var id: Int, var description: String, var pokemonRewards: List<Pokemon>, var itemRewards: List<Int>) {
    companion object {
        fun of(achievementEntity: AchievementEntity, gameDataService: GameDataService): Achievement {
            return Achievement(
                achievementEntity.id,
                achievementEntity.description,
                achievementEntity.pokemonReward.map { gameDataService.generatePokemon(it.id, it.level) },
                achievementEntity.itemRewards
            )
        }

        fun isClaimable(activity: MainActivity, id: Int): Boolean {
            if (activity.trainer!!.successfulAchievements.contains(id))
                return false
            when (id) {
                1 -> return activity.trainer!!.progression > LevelMenu.ELITE_4_LAST_LEVEL_ID
                2 -> return activity.trainer!!.achievements!!.leagueDefeatedSecondTime
                3 -> return activity.trainer!!.achievements!!.dugtrioAchievement
                4 -> return activity.trainer!!.achievements!!.articunoAchievement
                5 -> return activity.trainer!!.achievements!!.moltresAchievement
                6 -> return activity.trainer!!.achievements!!.zapdosAchievement
                7 -> return activity.trainer!!.pokemons.any { it.data.id == 243 }
                8 -> return activity.trainer!!.pokemons.any { it.data.id == 244 }
                9 -> return activity.trainer!!.pokemons.any { it.data.id == 245 }
                10 -> return activity.trainer!!.achievements!!.leagueWithTeamOfFourAchievement
                11 -> return activity.trainer!!.achievements!!.winstreak8Achievement
                12 -> return activity.trainer!!.achievements!!.winstreak15Achievement
                13 -> return activity.trainer!!.achievements!!.winstreak25Factory
                14 -> return activity.trainer!!.achievements!!.leadersDefeatedAfterTheLeague.size == 7
                15 -> return activity.trainer!!.achievements!!.mewtwoAchievement
            }
            return false
        }
    }
}