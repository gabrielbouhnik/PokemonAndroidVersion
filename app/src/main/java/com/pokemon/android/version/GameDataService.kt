package com.pokemon.android.version

import com.pokemon.android.version.model.Achievement
import com.pokemon.android.version.model.BattleFrontierPokemonData
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.PokemonData
import com.pokemon.android.version.model.banner.Banner
import com.pokemon.android.version.model.banner.PokemonBanner
import com.pokemon.android.version.model.item.HoldItem
import com.pokemon.android.version.model.item.ItemData
import com.pokemon.android.version.model.item.ItemFactory
import com.pokemon.android.version.model.item.ShopItem
import com.pokemon.android.version.model.level.*
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.MoveFactory
import com.pokemon.android.version.model.move.Stats
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.repository.*
import com.pokemon.android.version.ui.BattleFrontierMenu
import com.pokemon.android.version.ui.LevelMenu
import com.pokemon.android.version.utils.StatUtils
import kotlin.random.Random

class GameDataService {
    var items: List<ItemData> = ArrayList()
    var moves: List<Move> = ArrayList()
    var pokemons: List<PokemonData> = ArrayList()
    var banners: List<Banner> = ArrayList()
    var levels: List<LevelData> = ArrayList()
    var achievements: List<Achievement> = ArrayList()
    var shop: MutableList<ShopItem> = ArrayList()
    var battleFrontierPokemons: HashMap<Int, ArrayList<BattleFrontierPokemonData>> = HashMap()

    companion object {
        const val MOVES_DATA_PATH = "game_data/moves.json"
        const val ITEMS_DATA_PATH = "game_data/items.json"
        const val POKEMON_DATA_PATH = "game_data/pokemons.json"
        const val BANNER_DATA_PATH = "game_data/banners.json"
        const val LEVELS_DATA_PATH = "game_data/levels.json"
        const val POKEMON_BATTLE_FRONTIER_PATH = "game_data/battle_frontier/pokemons.json"
        const val ACHIEVEMENTS_DATA_PATH = "game_data/achievements.json"
        const val SHOP_DATA_PATH = "game_data/shop.json"
    }

    fun loadGameData(activity: MainActivity) {
        val pokemonRepository = PokemonRepository()
        val itemRepository = ItemsRepository()
        val bannerRepository = BannerRepository()
        val achievementsRepository = AchievementRepository()
        val movesRepository = MovesRepository()
        val levelsRepository = LevelsRepository()
        val shopRepository = ShopRepository()
        val battleFrontierPokemonRepository = BattleFrontierPokemonRepository()
        this.moves = MoveFactory.createMove(movesRepository.loadData(activity))
        this.items = ItemFactory.createItems(itemRepository.loadData(activity), this.moves)
        this.pokemons = pokemonRepository.loadData(activity).map { PokemonData.of(it, moves) }
        this.banners = bannerRepository.loadData(activity).map { Banner.of(it, this) }
        this.levels = LevelFactory.createLevels(levelsRepository.loadData(activity), this)
        this.achievements = achievementsRepository.loadData(activity).map { Achievement.of(it, this) }
        battleFrontierPokemonRepository.loadData(activity).forEach {
            if (this.battleFrontierPokemons.containsKey(it.id))
                this.battleFrontierPokemons[it.id]!!.add(BattleFrontierPokemonData(it.moveIds.map { moveId -> getMoveById(moveId)}, if (it.itemHeld != null) HoldItem.valueOf(it.itemHeld!!) else null))
            else
                this.battleFrontierPokemons[it.id] = arrayListOf(BattleFrontierPokemonData(it.moveIds.map { moveId -> getMoveById(moveId)}, if (it.itemHeld != null) HoldItem.valueOf(it.itemHeld!!) else null))
        }
        this.shop = shopRepository.loadData(activity).map { ShopItem.of(it, this)}.toMutableList()
    }

    fun updateShopForHardMode() {
        this.shop.filter { it.itemId < 12 }.map{ it.cost *= 3}
        for (itemId in 16..23) {
            this.shop.add(ShopItem(itemId, this.items.first { it.id == itemId }.name, 1000, 3))
        }
        this.shop.add(ShopItem(12, this.items.first { it.id == 12 }.name, 500, 3))
        this.shop.add(ShopItem(13, this.items.first { it.id == 13 }.name, 1000, 6))
        this.shop.add(ShopItem(28, this.items.first { it.id == 28 }.name, 1000, 3))
        this.shop.sortBy { it.itemId }
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
        return generatePokemonWithMoves(id, level, moveSet,null)
    }

    fun generateWildPokemon(id: Int, level: Int): Pokemon {
        var adjustedLevel = level
        if (id == 92 || id == 228 || id == 602)
            adjustedLevel = 21
        if (id == 633)
            adjustedLevel = 40
        val pokemon = generatePokemon(id, adjustedLevel)
        val random = Random.nextInt(250)
        if (random == 50 && id < 650)
            pokemon.shiny = true
        if ((id == 21 || id == 22 || id == 84) && random % 4 == 0)
            pokemon.heldItem = HoldItem.SHARP_BEAK
        if (id == 50 && random % 4 == 0)
            pokemon.heldItem = HoldItem.SOFT_SAND
        if ((id == 88 || id == 89) && random % 4 == 0)
            pokemon.heldItem = HoldItem.BLACK_SLUDGE
        return pokemon
    }

    fun generatePokemonFromBanner(pokemonBanner: PokemonBanner): Pokemon {
        val pokemon = generatePokemonWithMoves(pokemonBanner.pokemonId, 5, pokemonBanner.moves,null)
        pokemon.isFromBanner = true
        if (pokemon.data.id == 133){
            val random = Random.nextInt(1,5)
            if (random == 1)
                pokemon.shiny = true
        }
        return pokemon
    }

    fun generatePokemonWithMoves(id: Int, level: Int, moves: List<Move>, holdItem: HoldItem?): Pokemon {
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
            .shiny(false)
            .holdItem(holdItem)
            .build()
    }

    fun generateBoss(id: Int, level: Int, moves: List<Move>): PokemonBoss {
        val pokemon = generatePokemonWithMoves(id, level, moves,null)
        val move5: PokemonMove? = if (moves.size < 5) null else PokemonMove(moves[4])
        val move6: PokemonMove? = if (moves.size < 6) null else PokemonMove(moves[5])
        val boss = PokemonBoss.PokemonBossBuilder()
            .data(pokemon.data)
            .level(level)
            .hp(pokemon.hp)
            .attack(pokemon.attack)
            .defense(pokemon.defense)
            .spAtk(pokemon.spAtk)
            .spDef(pokemon.spDef)
            .speed(pokemon.speed)
            .currentHP(pokemon.hp)
            .move1(pokemon.move1)
            .move2(pokemon.move2)
            .move3(pokemon.move3)
            .move4(pokemon.move4)
            .move5(move5)
            .move6(move6)
            .build()
        if (boss.data.id == 143)
            boss.heldItem = HoldItem.LEFTOVERS
        return boss
    }

    fun getPokemonLocation(id: Int, progression: Int): String {
        return this.levels.filter { it is WildBattleLevelData
                && it.id < progression
                && it.possibleEncounters.encounters.map{data->data.id}.contains(id) }
            .map { it.name }.toString()
    }

    fun increaseLevelDifficulty() {
        levels.filter { it is LeaderLevelData && it.id < BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID }
            .forEach {
                it.exp = 10000
                (it as LeaderLevelData).iaLevel = 3
            }
    }
}