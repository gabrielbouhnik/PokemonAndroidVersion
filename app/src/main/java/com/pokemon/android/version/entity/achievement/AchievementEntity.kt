package com.pokemon.android.version.entity.achievement

data class AchievementEntity(
    var id: Int, var description: String, var pokemonReward: List<PokemonRewardEntity>, var itemRewards: List<Int>
) {
}