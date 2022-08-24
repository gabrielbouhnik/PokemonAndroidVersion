package com.pokemon.android.version.utils

import com.pokemon.android.version.model.item.*

class ItemUtils {
    companion object {
        const val POKEBALL_ID = 11

        fun isBall(itemId: Int): Boolean {
            return itemId in 11..15
        }

        fun isBadge(itemId: Int): Boolean {
            return itemId in 31..38
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
                41 -> return TMItem(72)
                42 -> return TMItem(25)
                43 -> return TMItem(103)
                44 -> return TMItem(62)
                45 -> return TMItem(110)
                46 -> return TMItem(44)
                47 -> return TMItem(104)
                48 -> return TMItem(107)
                49 -> return TMItem(12)
                50 -> return TMItem(31)
                51 -> return TMItem(45)
                52 -> return TMItem(93)
                53 -> return TMItem(46)
                54 -> return TMItem(82)
                55 -> return TMItem(43)
                56 -> return TMItem(37)
                57 -> return TMItem(100)
                58 -> return TMItem(105)
                59 -> return TMItem(13)
                60 -> return TMItem(69)
                61 -> return TMItem(55)
                62 -> return TMItem(135)
                63 -> return TMItem(114)
                64 -> return TMItem(136)
                65 -> return TMItem(113)
                66 -> return TMItem(111)
                67 -> return TMItem(66)
                68 -> return TMItem(76)
                69 -> return TMItem(108)
                70 -> return TMItem(128)
                71 -> return TMItem(140)
                72 -> return TMItem(56)
                73 -> return TMItem(138)
                74 -> return TMItem(137)
                75 -> return TMItem(92)
            }
            return Revive.MAX_REVIVE
        }
    }
}