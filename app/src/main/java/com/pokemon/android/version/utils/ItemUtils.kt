package com.pokemon.android.version.utils

import com.pokemon.android.version.model.item.*

class ItemUtils {
    companion object {
        val MEGA_RING_ID = 130
        val TM_MOVES_IDS = listOf(
            72,//Headbutt
            221,//ROCK TOMB
            24,//WATER PULSE
            184,//CHARGED BEAM
            66,//GIGA DRAIN
            37,//SLUDGE BOMB
            45,//PSYCHIC
            82,//FIRE BLAST
            93,//EARTHQUAKE
            137,//Bullet Seed
            172,//Flame Charge
            138,//WILL-O-WISP
            55,//Thunder Wave
            113,//Aerial Ace
            207,//Struggle Bug
            30,//Bulldoze
            69,//Brick Break
            140,//Bulk Up
            76,//Drain Punch
            97,//Icy Wind
            205,//Steel Wing
            204,//Snarl
            105,//Energy Ball
            36,//Seed Bomb
            110,//Fire Punch
            31,//Flamethrower
            261,//Heat Wave
            136,//Waterfall
            43,//Surf
            103,//Thunder Punch
            12,//Thunderbolt
            200,//Wild Charge
            114,//Swords Dance
            176,//Hyper Beam
            177,//Giga Impact
            50,//Air Slash
            56,//X-Scissor
            62,//Poison Jab
            25,//Rock Slide
            87,//Earth Power
            267,//Body Press
            139,//Focus Blast
            219,//Psyshock
            148,//Zen Headbutt
            135,//Calm Mind
            206,//Shadow Claw
            46,//Shadow Ball
            107,//Ice Punch
            100,//Ice Beam
            104,//Dragon Pulse
            108,//Dragon Claw
            57,//Iron Head
            111,//Flash Cannon
            173,//Nasty Plot
            92,//Dark Pulse
            73,//Play Rough
            44)//Dazzling Gleam
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
                in 16..30,in 38..39 -> return EvolutionItem(itemId)
                in 51..106 -> return TMItem(TM_MOVES_IDS[itemId - 51])
                in 150..185 -> return ItemToHold(HoldItem.values().first { it.id == itemId })
            }
            return Revive.MAX_REVIVE
        }
    }
}