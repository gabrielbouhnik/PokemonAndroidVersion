package com.pokemon.android.version.model.level

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.PokemonData
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Trainer
import com.pokemon.android.version.model.battle.PokemonBattleData
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import java.util.ArrayList

class PokemonBoss(
    data: PokemonData,
    level: Int,
    move1: PokemonMove,
    move2: PokemonMove?,
    move3: PokemonMove?,
    move4: PokemonMove?,
    var move5: PokemonMove?,
    var move6: PokemonMove?,
    status: Status = Status.OK,
    hp: Int = 0,
    attack: Int = 0,
    defense: Int = 0,
    spAtk: Int = 0,
    spDef: Int = 0,
    speed: Int = 0,
    currentHP: Int = 0,
    currentExp: Int = 0,
    battleData: PokemonBattleData?
) : Pokemon(
    data,
    null,
    level,
    move1,
    move2,
    move3,
    move4,
    status,
    hp,
    attack,
    defense,
    spAtk,
    spDef,
    speed,
    currentHP,
    currentExp,
    false,
    battleData,
    false,
    arrayListOf()
) {
     data class PokemonBossBuilder(
          var data: PokemonData? = null,
          var trainer: Trainer? = null,
          var level: Int = 1,
          var move1: PokemonMove? = null,
          var move2: PokemonMove? = null,
          var move3: PokemonMove? = null,
          var move4: PokemonMove? = null,
          var move5: PokemonMove? = null,
          var move6: PokemonMove? = null,
          var status: Status = Status.OK,
          var hp: Int = 0,
          var attack: Int = 0,
          var defense: Int = 0,
          var spAtk: Int = 0,
          var spDef: Int = 0,
          var speed: Int = 0,
          var currentHP: Int = 0,
          var currentExp: Int = 0,
     ) {
          fun data(data: PokemonData) = apply { this.data = data }
          fun trainer(trainer: Trainer) = apply { this.trainer = trainer }
          fun level(level: Int) = apply { this.level = level }
          fun status(status: Status) = apply { this.status = status }
          fun move1(move: PokemonMove) = apply { this.move1 = move }
          fun move2(move: PokemonMove?) = apply { this.move2 = move }
          fun move3(move: PokemonMove?) = apply { this.move3 = move }
          fun move4(move: PokemonMove?) = apply { this.move4 = move }
          fun move5(move: PokemonMove?) = apply { this.move5 = move }
          fun move6(move: PokemonMove?) = apply { this.move6 = move }
          fun hp(hp: Int) = apply { this.hp = hp }
          fun attack(attack: Int) = apply { this.attack = attack }
          fun defense(defense: Int) = apply { this.defense = defense }
          fun spAtk(spAtk: Int) = apply { this.spAtk = spAtk }
          fun spDef(spDef: Int) = apply { this.spDef = spDef }
          fun speed(speed: Int) = apply { this.speed = speed }
          fun currentHP(currentHP: Int) = apply { this.currentHP = currentHP }
          fun build() = PokemonBoss(
               data!!,
               level,
               move1!!,
               move2,
               move3,
               move4,
               move5,
               move6,
               status,
               hp,
               attack,
               defense,
               spAtk,
               spDef,
               speed,
               currentHP,
               currentExp,
               PokemonBattleData()
          )

     }
}