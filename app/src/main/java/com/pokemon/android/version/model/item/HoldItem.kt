package com.pokemon.android.version.model.item

import java.util.*

enum class HoldItem(var id: Int, var description: String) {
    AIR_BALLOON(172,"When held by a Pokémon, the Pokémon will float into the air. When the holder is attacked, this item will burst."),
    ASSAULT_VEST(176,"Raises Special Defense but prevents the use of status moves."),
    BIG_ROOT(174,"Recovers more HP from HP-stealing moves."),
    BLACK_BELT(160,"Increases the power of Fighting-type moves."),
    BLACK_GLASSES(165,"Increases the power of Dark-type moves."),
    BLACK_SLUDGE(171,"A held item that gradually restores the HP of Poison-type Pokémon. It inflicts damage on all other types."),
    CHARCOAL(151,"Increases the power of Fire-type moves."),
    DRAGON_FANG(164,"Increases the power of Dragon-type moves."),
    EXPERT_BELT(170,"Increases the power of super-effective moves."),
    EVIOLITE(173,"A mysterious evolutionary lump. When held, it raises the Defense and Sp. Def of a Pokémon that can still evolve."),
    FLAME_ORB(179,"An item to be held by a Pokémon. It is a bizarre orb that inflicts a burn on the holder in battle."),
    FOCUS_SASH(169,"An item to be held by a Pokémon. If it has full HP, the holder will endure one potential KO attack, leaving 1 HP."),
    HARD_STONE(158,"Increases the power of Rock-type moves."),
    LEFTOVERS(166,"An item to be held by a Pokémon. The holder's HP is gradually restored during battle."),
    LOADED_DICE(177,"Makes multi-strike moves more likely to hit more times."),
    LIFE_ORB(168,"Increases the power of moves, but loses HP each turn."),
    LUM_BERRY(167,"If held by a Pokémon, it recovers from any status problem."),
    MAGNET(153,"Increases the power of Electric-type moves."),
    MIRACLE_SEED(150,"Increases the power of Grass-type moves."),
    MYSTIC_WATER(152,"Increases the power of Water-type moves."),
    NEVER_MELT_ICE(163,"Increases the power of Ice-type moves."),
    POISON_BARB(157,"Increases the power of Poison-type moves."),
    POWER_HERB(181, "A single-use item to be held by a Pokémon. It allows the immediate use of a move that charges on the first turn."),
    PROTECTIVE_PADS(183, "These pads protect the holder from effects caused by making direct contact with the target."),
    ROCKY_HELMET(175,"If the holder of this item takes damage, the attacker will also be damaged upon contact."),
    SILK_SCARF(154,"Increases the power of Normal-type moves."),
    SHARP_BEAK(155,"Increases the power of Flying-type moves."),
    SILVER_POWDER(156,"Increases the power of Bug-type moves"),
    SOFT_SAND(159,"Increases the power of Ground-type moves."),
    SPELL_TAG(162,"Increases the power of Ghost-type moves."),
    THROAT_SPRAY(184,"Raises Sp. Atk when a sound-based move is used."),
    TOXIC_ORB(178,"An item to be held by a Pokémon. It is a bizarre orb that badly poisons the holder in battle."),
    TWISTED_SPOON(161,"Increases the power of Psychic-type moves."),
    WEAKNESS_POLICY(180,"Sharply raises Attack and Special Attack if hit by a super-effective move."),
    WIDE_LENS(182,"Increases the accuracy of moves.");

    fun heldItemToString(): String {
        return this.toString().replace("_", " ").lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}