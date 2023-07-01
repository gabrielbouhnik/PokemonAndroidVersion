package com.pokemon.android.version.utils

import com.pokemon.android.version.model.item.*

class ItemUtils {
    companion object {
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
                51 -> return TMItem(72)
                52 -> return TMItem(221)
                53 -> return TMItem(24)
                54 -> return TMItem(184)
                55 -> return TMItem(66)
                56 -> return TMItem(37)
                57 -> return TMItem(45)
                58 -> return TMItem(82)
                59 -> return TMItem(93)
                60 -> return TMItem(137)
                61 -> return TMItem(172)
                62 -> return TMItem(138)
                63 -> return TMItem(55)
                64 -> return TMItem(113)
                65 -> return TMItem(207)
                66 -> return TMItem(30)
                67 -> return TMItem(69)
                68 -> return TMItem(140)
                69 -> return TMItem(76)
                70 -> return TMItem(205)
                71 -> return TMItem(13)
                72 -> return TMItem(204)
                73 -> return TMItem(105)
                74 -> return TMItem(194)
                75 -> return TMItem(110)
                76 -> return TMItem(31)
                77 -> return TMItem(136)
                78 -> return TMItem(43)
                79 -> return TMItem(103)
                80 -> return TMItem(12)
                81 -> return TMItem(200)
                82 -> return TMItem(114)
                83 -> return TMItem(176)
                84 -> return TMItem(177)
                85 -> return TMItem(50)
                86 -> return TMItem(56)
                87 -> return TMItem(62)
                88 -> return TMItem(25)
                89 -> return TMItem(139)
                90 -> return TMItem(107)
                91 -> return TMItem(100)
                92 -> return TMItem(148)
                93 -> return TMItem(135)
                94 -> return TMItem(206)
                95 -> return TMItem(46)
                96 -> return TMItem(104)
                97 -> return TMItem(108)
                98 -> return TMItem(111)
                99 -> return TMItem(92)
                100 -> return TMItem(44)
            }
            return Revive.MAX_REVIVE
        }
    }
}