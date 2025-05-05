package com.pokemon.android.version.model.battle

import com.pokemon.android.version.model.Ability
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.model.item.HoldItem
import com.pokemon.android.version.model.move.StatChange

class BattleField(var weather: Weather, var weatherCounter: Int) {
    fun setWeather(weatherSetter: Pokemon, weather: Weather, other: Pokemon) {
        this.weather = weather
        if (weather != Weather.NONE) {
            weatherCounter = 5
            if (weather == Weather.SANDSTORM) {
                if (weatherSetter.hasItem(HoldItem.SMOOTH_ROCK))
                    this.weatherCounter += 3
                if (weatherSetter.hasType(Type.ROCK))
                    weatherSetter.battleData!!.statsMultiplier.updateStat(StatChange.SPDEF_ONE_LEVEL_RAISE)
                if (other.hasType(Type.ROCK))
                    other.battleData!!.statsMultiplier.updateStat(StatChange.SPDEF_ONE_LEVEL_RAISE)
                if (weatherSetter.hasAbility(Ability.SAND_RUSH))
                    weatherSetter.battleData!!.statsMultiplier.updateStat(StatChange.SPEED_TWO_LEVEL_RAISE)
                if (other.hasAbility(Ability.SAND_RUSH))
                    other.battleData!!.statsMultiplier.updateStat(StatChange.SPEED_TWO_LEVEL_RAISE)
            }
            if (weather == Weather.SNOW) {
                if (weatherSetter.hasItem(HoldItem.ICY_ROCK))
                    this.weatherCounter += 3
                if (weatherSetter.hasType(Type.ICE))
                    weatherSetter.battleData!!.statsMultiplier.updateStat(StatChange.DEFENSE_ONE_LEVEL_RAISE)
                if (other.hasType(Type.ICE))
                    other.battleData!!.statsMultiplier.updateStat(StatChange.DEFENSE_ONE_LEVEL_RAISE)
            }
        }
    }
}