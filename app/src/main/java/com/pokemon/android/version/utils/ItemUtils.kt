package com.pokemon.android.version.utils

import com.pokemon.android.version.model.item.*

class ItemUtils {
    companion object {
        val TM_MOVE_ID = listOf(
            72,
            221,
            24,
            184,
            66,
            37,
            45,
            82,
            93,
            137,
            172,
            138,
            55,
            113,
            207,
            30,
            69,
            140,
            76,
            205,
            13,
            204,
            105,
            36,
            110,
            31,
            136,
            43,
            103,
            12,
            200,
            114,
            176,
            177,
            50,
            56,
            62,
            25,
            139,
            107,
            100,
            148,
            135,
            206,
            46,
            104,
            108,
            111,
            92,
            44)
        const val POKEBALL_ID = 11

        fun isBall(itemId: Int): Boolean {
            return itemId in 11..15
        }

        fun isBadge(itemId: Int): Boolean {
            return itemId in 31..38 || itemId in 41..48
        }

        fun getItemById(itemId: Int): Item {
            when (itemId) {
                1 -> return HealingHPItem.POTION
                2 -> return HealingHPItem.SUPERPOTION
                3 -> return HealingHPItem.HYPERPOTION
                4 -> return FullHeal.FULL_HEAL
                5 -> return HealingStatusItem.PARALYSE_HEAL
                6 -> return HealingStatusItem.BURN_HEAL
                7 -> return HealingStatusItem.ANTIDOTE
                8 -> return RareCandy.RARE_CANDY
                9 -> return Revive.REVIVE
                10 -> return PPHealItem.PP_HEAL
                11 -> return Ball.POKEBALL
                12 -> return Ball.SUPERBALL
                13 -> return Ball.HYPERBALL
                14 -> return Ball.NETBALL
                15 -> return Ball.HEALBALL
                in 16..29,in 38..39 -> return EvolutionItem(itemId)
                in 51..100 -> return TMItem(TM_MOVE_ID[itemId - 51])
                in 150..176 -> return ItemToHold(HoldItem.values().first { it.id == itemId })
            }
            return Revive.MAX_REVIVE
        }
    }
}