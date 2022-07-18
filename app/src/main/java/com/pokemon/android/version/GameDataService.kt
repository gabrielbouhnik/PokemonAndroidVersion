package com.pokemon.android.version

import com.pokemon.android.version.model.Achievement
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.PokemonData
import com.pokemon.android.version.model.banner.Banner
import com.pokemon.android.version.model.banner.PokemonBanner
import com.pokemon.android.version.model.item.ItemData
import com.pokemon.android.version.model.item.ItemFactory
import com.pokemon.android.version.model.level.LeaderLevelData
import com.pokemon.android.version.model.level.LevelData
import com.pokemon.android.version.model.level.LevelFactory
import com.pokemon.android.version.model.level.TrainerBattleLevelData
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.MoveFactory
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.repository.*
import com.pokemon.android.version.ui.BattleFrontierMenu
import com.pokemon.android.version.ui.LevelMenu
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class GameDataService {
    var items: List<ItemData> = ArrayList()
    var moves: List<Move> = ArrayList()
    var pokemons: List<PokemonData> = ArrayList()
    var banners: List<Banner> = ArrayList()
    var levels: List<LevelData> = ArrayList()
    var achievements: List<Achievement> = ArrayList()
    var battleFrontierPokemons: HashMap<Int, ArrayList<List<Move>>> = HashMap<Int, ArrayList<List<Move>>>()
    companion object {
        const val MOVES_DATA_PATH = "game_data/moves.json"
        const val ITEMS_DATA_PATH = "game_data/items.json"
        const val POKEMON_DATA_PATH = "game_data/pokemons.json"
        const val BANNER_DATA_PATH = "game_data/banners.json"
        const val LEVELS_DATA_PATH = "game_data/levels.json"
        const val POKEMON_BATTLE_FRONTIER_PATH = "game_data/battle_frontier/pokemons.json"
        const val ACHIEVEMENTS_DATA_PATH = "game_data/achievements.json"
    }

    fun loadGameData(activity: MainActivity) {
        val pokemonRepository = PokemonRepository()
        val itemRepository = ItemsRepository()
        val bannerRepository = BannerRepository()
        val achievementsRepository = AchievementRepository()
        val movesRepository = MovesRepository()
        val levelsRepository = LevelsRepository()
        val battleFrontierPokemonRepository = BattleFrontierPokemonRepository()
        this.moves = MoveFactory.createMove(movesRepository.loadData(activity))
        this.items = ItemFactory.createItems(itemRepository.loadData(activity), this.moves)
        this.pokemons = pokemonRepository.loadData(activity).map { PokemonData.of(it, moves) }
        this.banners = bannerRepository.loadData(activity).map { Banner.of(it, this) }
        this.levels = LevelFactory.createLevels(levelsRepository.loadData(activity), this)
        this.achievements = achievementsRepository.loadData(activity).map{ Achievement.of(it, this)}
        battleFrontierPokemonRepository.loadData(activity).forEach {
            if (this.battleFrontierPokemons.containsKey(it.id))
                this.battleFrontierPokemons[it.id]!!.add(it.moveIds.map{ moveId -> getMoveById(moveId)})
            else
                this.battleFrontierPokemons[it.id] = arrayListOf(it.moveIds.map{ moveId -> getMoveById(moveId)})
        }
    }

    fun getPokemonDataById(id: Int): PokemonData {
        return pokemons.first { it.id == id }
    }

    fun getMoveById(id: Int): Move {
        return moves.first { it.id == id }
    }

    fun generatePokemon(id: Int, level: Int): Pokemon {
        val pokemonData: PokemonData = pokemons.first { it.id == id }
        val hp: Int = 10 + (pokemonData.hp.toFloat() * (level / 50f)).roundToInt()
        val attack: Int = 5 + (pokemonData.attack.toFloat() * (level / 50f)).roundToInt()
        val defense: Int = 5 + (pokemonData.defense.toFloat() * (level / 50f)).roundToInt()
        val spAtk: Int = 5 + (pokemonData.spAtk.toFloat() * (level / 50f)).roundToInt()
        val spDef: Int = 5 + (pokemonData.spDef.toFloat() * (level / 50f)).roundToInt()
        val speed: Int = 5 + (pokemonData.speed.toFloat() * (level / 50f)).roundToInt()
        val moves: List<PokemonMove> =
            pokemonData.movesByLevel.filter { it.level <= level }.map { PokemonMove(it.move, it.move.pp) }.reversed()
        val move1: PokemonMove = moves.first()
        val move2: PokemonMove? = if (moves.size < 2) null else moves[1]
        val move3: PokemonMove? = if (moves.size < 3) null else moves[2]
        val move4: PokemonMove? = if (moves.size < 4) null else moves[3]
        return Pokemon.PokemonBuilder()
            .data(pokemonData)
            .level(level)
            .hp(hp)
            .attack(attack)
            .defense(defense)
            .spAtk(spAtk)
            .spDef(spDef)
            .speed(speed)
            .currentHP(hp)
            .move1(move1)
            .move2(move2)
            .move3(move3)
            .move4(move4)
            .build()
    }

    fun generatePokemonFromBanner(pokemonBanner: PokemonBanner): Pokemon {
        val hp: Int = 10 + (pokemonBanner.pokemonData.hp.toFloat() * 0.1).roundToInt()
        val attack: Int = 5 + (pokemonBanner.pokemonData.attack.toFloat() * 0.1).roundToInt()
        val defense: Int = 5 + (pokemonBanner.pokemonData.defense.toFloat() * 0.1).roundToInt()
        val spAtk: Int = 5 + (pokemonBanner.pokemonData.spAtk.toFloat() * 0.1).roundToInt()
        val spDef: Int = 5 + (pokemonBanner.pokemonData.spDef.toFloat() * 0.1).roundToInt()
        val speed: Int = 5 + (pokemonBanner.pokemonData.speed.toFloat() * 0.1).roundToInt()
        val move1 = PokemonMove(pokemonBanner.move1)
        val move2 = if (pokemonBanner.move2 == null) null else PokemonMove(pokemonBanner.move2!!)
        val move3 = if (pokemonBanner.move3 == null) null else PokemonMove(pokemonBanner.move3!!)
        val move4 = if (pokemonBanner.move4 == null) null else PokemonMove(pokemonBanner.move4!!)
        return Pokemon.PokemonBuilder()
            .data(pokemonBanner.pokemonData)
            .level(5)
            .hp(hp)
            .attack(attack)
            .defense(defense)
            .spAtk(spAtk)
            .spDef(spDef)
            .speed(speed)
            .currentHP(hp)
            .move1(move1)
            .move2(move2)
            .move3(move3)
            .move4(move4)
            .isFromBanner(true)
            .build()
    }

    fun generatePokemonWithMoves(id: Int, level: Int, moves: List<Move>): Pokemon {
        val pokemonData: PokemonData = pokemons.first { it.id == id }
        val hp: Int = 10 + (pokemonData.hp.toFloat() * (level / 50f)).roundToInt()
        val attack: Int = 5 + (pokemonData.attack.toFloat() * (level / 50f)).roundToInt()
        val defense: Int = 5 + (pokemonData.defense.toFloat() * (level / 50f)).roundToInt()
        val spAtk: Int = 5 + (pokemonData.spAtk.toFloat() * (level / 50f)).roundToInt()
        val spDef: Int = 5 + (pokemonData.spDef.toFloat() * (level / 50f)).roundToInt()
        val speed: Int = 5 + (pokemonData.speed.toFloat() * (level / 50f)).roundToInt()
        val move1 = PokemonMove(moves[0])
        val move2: PokemonMove? = if (moves.size < 2) null else PokemonMove(moves[1])
        val move3: PokemonMove? = if (moves.size < 3) null else PokemonMove(moves[2])
        val move4: PokemonMove? = if (moves.size < 4) null else PokemonMove(moves[3])
        return Pokemon.PokemonBuilder()
            .data(pokemonData)
            .level(level)
            .hp(hp)
            .attack(attack)
            .defense(defense)
            .spAtk(spAtk)
            .spDef(spDef)
            .speed(speed)
            .currentHP(hp)
            .move1(move1)
            .move2(move2)
            .move3(move3)
            .move4(move4)
            .build()
    }

    fun updateGymLeaderExp(){
        levels.filter { it is LeaderLevelData && it.id < BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID}
            .forEach { it.exp = 10000}
    }

    fun updateEliteMode() {
        levels.filter { it.id in LevelMenu.ELITE_4_FIRST_LEVEL_ID..LevelMenu.ELITE_4_LAST_LEVEL_ID }
            .map { it as TrainerBattleLevelData }.forEach { levelData ->
                levelData.opponentTrainerData.forEach { trainer ->
                    trainer.pokemons.forEach { pokemon ->
                        pokemon.level += 10
                    }
                }
            }
    }
}