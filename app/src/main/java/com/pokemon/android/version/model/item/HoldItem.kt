package com.pokemon.android.version.model.item

import java.util.*

enum class HoldItem(var id: Int) {
    AIR_BALLOON(172),
    ASSAULT_VEST(176),
    BIG_ROOT(174),
    BLACK_BELT(160),
    BLACK_GLASSES(165),
    BLACK_SLUDGE(171),
    CHARCOAL(151),
    DRAGON_FANG(164),
    EXPERT_BELT(170),
    EVIOLITE(173),
    FOCUS_SASH(169),
    HARD_STONE(158),
    LEFTOVERS(166),
    LIFE_ORB(168),
    LUM_BERRY(167),
    MAGNET(153),
    MIRACLE_SEED(150),
    MYSTIC_WATER(152),
    NEVER_MELT_ICE(163),
    POISON_BARB(157),
    ROCKY_HELMET(175),
    SILK_SCARF(154),
    SHARP_BEAK(155),
    SILVER_POWDER(156),
    SOFT_SAND(159),
    SPELL_TAG(162),
    TWISTED_SPOON(161);

    fun heldItemToString(): String {
        return this.toString().replace("_", " ").lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}