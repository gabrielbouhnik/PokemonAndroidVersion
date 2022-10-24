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
import com.pokemon.android.version.model.move.Stats
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.repository.*
import com.pokemon.android.version.ui.BattleFrontierMenu
import com.pokemon.android.version.ui.LevelMenu
import com.pokemon.android.version.utils.StatUtils

class GameDataService {
    var items: List<ItemData> = ArrayList()
    var moves: List<Move> = ArrayList()
    var pokemons: List<PokemonData> = ArrayList()
    var banners: List<Banner> = ArrayList()
    var levels: List<LevelData> = ArrayList()
    var achievements: List<Achievement> = ArrayList()
    var battleFrontierPokemons: HashMap<Int, ArrayList<List<Move>>> = HashMap()

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
        this.achievements = achievementsRepository.loadData(activity).map { Achievement.of(it, this) }
        battleFrontierPokemonRepository.loadData(activity).forEach {
            if (this.battleFrontierPokemons.containsKey(it.id))
                this.battleFrontierPokemons[it.id]!!.add(it.moveIds.map { moveId -> getMoveById(moveId) })
            else
                this.battleFrontierPokemons[it.id] = arrayListOf(it.moveIds.map { moveId -> getMoveById(moveId) })
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
        var possibleMoves: List<Move> = pokemonData.movesByLevel.filter { it.level <= level }.map { it.move }
        if (possibleMoves.size > 4)
            possibleMoves = possibleMoves.reversed()
        val moveSet: ArrayList<Move> = arrayListOf()
        moveSet.add(possibleMoves.first())
        if (possibleMoves.size > 1)
            moveSet.add(possibleMoves[1])
        if (possibleMoves.size > 2)
            moveSet.add(possibleMoves[2])
        if (possibleMoves.size > 3)
            moveSet.add(possibleMoves[3])
        return generatePokemonWithMoves(id, level, moveSet)
    }

    fun generatePokemonFromBanner(pokemonBanner: PokemonBanner): Pokemon {
        val pokemon = generatePokemonWithMoves(pokemonBanner.pokemonId, 5, pokemonBanner.moves)
        pokemon.isFromBanner = true
        return pokemon
    }

    fun generatePokemonWithMoves(id: Int, level: Int, moves: List<Move>): Pokemon {
        val pokemonData: PokemonData = pokemons.first { it.id == id }
        val hp: Int = StatUtils.computeHP(pokemonData, level)
        val attack: Int = StatUtils.computeStat(pokemonData, level, Stats.ATTACK)
        val defense: Int = StatUtils.computeStat(pokemonData, level, Stats.DEFENSE)
        val spAtk: Int = StatUtils.computeStat(pokemonData, level, Stats.SPATK)
        val spDef: Int = StatUtils.computeStat(pokemonData, level, Stats.SPDEF)
        val speed: Int = StatUtils.computeStat(pokemonData, level, Stats.SPEED)
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

    fun updateGymLeaderExp() {
        levels.filter { it is LeaderLevelData && it.id < BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID }
            .forEach { it.exp = 10000 }
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