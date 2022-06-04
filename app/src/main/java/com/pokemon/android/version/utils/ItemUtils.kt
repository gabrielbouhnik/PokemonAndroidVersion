package com.pokemon.android.version.utils

import com.pokemon.android.version.model.item.*

class ItemUtils {
    companion object {
        fun applyById(itemId: Int) {
            when(itemId){
                1 -> HealingHPItem.POTION
                2 -> HealingHPItem.SUPERPOTION
                3 -> HealingHPItem.HYPERPOTION
                4 -> FullHeal.FULL_HEAL
                5 -> HealingStatusItem.PARALYSE_HEAL
                6 -> HealingStatusItem.BURN_HEAL
                7 -> HealingStatusItem.ANTIDOTE
                8 -> RareCandy.RARE_CANDY
                9 -> Revive.REVIVE
                10 -> Ball.POKEBALL
                11 -> Ball.SUPERBALL
                else  -> Ball.HYPERBALL
            }
        }
    }
}