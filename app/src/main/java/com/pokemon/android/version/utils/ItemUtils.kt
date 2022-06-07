package com.pokemon.android.version.utils

import com.pokemon.android.version.model.item.*

class ItemUtils {
    companion object {
        fun isBall(itemId: Int) : Boolean{
            return itemId > 9 && itemId < 13
        }

        fun getItemById(itemId: Int) : Item {
            when(itemId){
                1 -> return HealingHPItem.POTION
                2 -> return HealingHPItem.SUPERPOTION
                3 -> return HealingHPItem.HYPERPOTION
                4 -> return FullHeal.FULL_HEAL
                5 -> return HealingStatusItem.PARALYSE_HEAL
                6 -> return HealingStatusItem.BURN_HEAL
                7 -> return HealingStatusItem.ANTIDOTE
                8 -> return RareCandy.RARE_CANDY
                9 -> return Revive.REVIVE
                10 -> return Ball.POKEBALL
                11 -> return Ball.SUPERBALL
                12  -> return Ball.HYPERBALL
                else  -> return PPHealItem.PP_HEAL
            }
        }
    }
}