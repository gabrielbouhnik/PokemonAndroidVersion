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
                10  -> return PPHealItem.PP_HEAL
                11 -> return Ball.POKEBALL
                12 -> return Ball.SUPERBALL
                13  -> return Ball.HYPERBALL
                14 -> return Ball.NETBALL
                15 -> return Ball.HEALBALL
                28 -> return ExpItem.EXP_CANDY_S
                29 -> return ExpItem.EXP_CANDY_M
                30 -> return ExpItem.EXP_CANDY_L
            }
            return Ball(0)
        }
    }
}