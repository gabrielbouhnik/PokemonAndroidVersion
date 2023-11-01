package com.pokemon.android.version.model

import com.pokemon.android.version.model.item.HoldItem
import com.pokemon.android.version.model.move.HealMove
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.MoveCategory
import com.pokemon.android.version.utils.BattleUtils
import kotlin.random.Random

enum class Status(var activeOutsideBattle: Boolean) {
    OK(true),
    BURN(true),
    PARALYSIS(true),
    POISON(true),
    BADLY_POISON(true),
    ASLEEP(true),
    FROZEN(true),
    CONFUSED(false),
    FLINCHED(false),
    TIRED(false),
    UNABLE_TO_MOVE(false),
    TRAPPED_WITH_DAMAGE(false),
    TRAPPED_WITHOUT_DAMAGE(false),
    LEECH_SEEDED(false),
    FIRED_UP(false),
    TAUNTED(false),
    ROOSTED(false);

    fun toDetails(): String {
        return when (this) {
            BURN -> "is burned!"
            PARALYSIS -> "is paralyzed!"
            POISON -> "is poisoned!"
            BADLY_POISON -> "is badly poisoned!"
            ASLEEP -> "fell asleep!"
            FROZEN -> "is frozen!"
            else -> ""
        }
    }

    fun toBattleIcon(): String {
        return when (this) {
            BURN -> "BRN"
            PARALYSIS -> "PAR"
            POISON, BADLY_POISON -> "PSN"
            ASLEEP -> "SLP"
            FROZEN -> "FRZ"
            else -> ""
        }
    }

    companion object {
        fun updateStatus(attacker: Pokemon, opponent: Pokemon, move: Move): String {
            if ((move.id == 34 || move.id == 35) && opponent.hasAbility(Ability.OVERCOAT))
                return "${opponent.data.name}'s Overcoat: It does not affect ${opponent.data.name}!\n"
            var details = ""
            move.status.forEach {
                if (isAffectedByStatus(move.id, it.status, opponent)) {
                    var randomForStatus: Int = Random.nextInt(100)
                    if (attacker.hasAbility(Ability.SERENE_GRACE))
                        randomForStatus /= 2
                    if (it.probability == null || randomForStatus <= it.probability!!) {
                        if (it.status.activeOutsideBattle) {
                            opponent.status = it.status
                            details += "${opponent.data.name} " + it.status.toDetails() + "\n"
                            BattleUtils.checkForStatsRaiseAbility(opponent)
                            if (opponent.hasAbility(Ability.SYNCHRONIZE)
                                && isAffectedByStatus(0, it.status, attacker)
                            ) {
                                attacker.status = it.status
                                details += "${opponent.data.name}'s Synchronize: ${attacker.data.name} " + it.status.toDetails() + "\n"
                            }
                            if (opponent.hasItem(HoldItem.LUM_BERRY)){
                                details += "${opponent.data.name}'s Lum Berry cured its status\n"
                                opponent.status = OK
                                opponent.consumeItem()
                            }
                        } else {
                            if (it.status == CONFUSED) {
                                if (opponent.hasAbility(Ability.OWN_TEMPO))
                                    details += "${opponent.data.name}'s Own Tempo: ${opponent.data.name} cannot be confused!\n"
                                else {
                                    opponent.battleData!!.battleStatus.add(it.status)
                                    details +="${opponent.data.name} became confused!\n"
                                    if (opponent.hasItem(HoldItem.LUM_BERRY)){
                                        details += "${opponent.data.name}'s Lum Berry cured its status\n"
                                        opponent.status = OK
                                        opponent.consumeItem()
                                    }
                                }

                            } else if (it.status == FLINCHED) {
                                if (!opponent.hasAbility(Ability.INNER_FOCUS))
                                    opponent.battleData!!.battleStatus.add(it.status)
                            } else {
                                opponent.battleData!!.battleStatus.add(it.status)
                                if (it.status == TRAPPED_WITH_DAMAGE || it.status == TRAPPED_WITHOUT_DAMAGE)
                                    details = "${opponent.data.name} is trapped!\n"
                                if (it.status == TIRED && opponent.status == OK)
                                    details = "${opponent.data.name} gets drowsy!\n"
                                if (it.status == LEECH_SEEDED)
                                    details = "${opponent.data.name} was seeded!\n"
                                if (it.status == TAUNTED)
                                    details = "${opponent.data.name} fell for the taunt!\n"
                            }
                        }
                    }
                } else if (it.probability == null) {
                    if (opponent.status == OK) {
                        if ((move.id ==34 || move.id == 83) && opponent.hasAbility(Ability.IMMUNITY))
                            details = "${opponent.data.name}'s Immunity: It does not affect ${opponent.data.name}!\n"
                        if (move.id == 55 && opponent.hasAbility(Ability.LIMBER))
                            details = "${opponent.data.name}'s Limber: It does not affect ${opponent.data.name}!\n"
                        if (move.id == 138 && opponent.hasAbility(Ability.WATER_VEIL))
                            details = "${opponent.data.name}'s Water Veil: It does not affect ${opponent.data.name}!\n"
                        if (it.status == ASLEEP) {
                            if (opponent.hasAbility(Ability.INSOMNIA))
                                details =
                                    "${opponent.data.name}'s Insomnia: It does not affect ${opponent.data.name}!\n"
                            if (opponent.hasAbility(Ability.VITAL_SPIRIT))
                                details =
                                    "${opponent.data.name}'s Vital Spirit: It does not affect ${opponent.data.name}!\n"
                        }
                    }
                    if (move.category == MoveCategory.OTHER) {
                        details = if (it.status == CONFUSED && opponent.battleData!!.battleStatus.contains(CONFUSED))
                            "But ${opponent.data.name} is already confused\n"
                        else
                            "But it failed!\n"
                    }
                }
            }
            return details
        }

        fun isAffectedByStatus(id: Int, status: Status, opponent: Pokemon): Boolean {
            if ((id == 35 || id == 253 || id == 255) && (opponent.hasType(Type.GRASS)
                                || opponent.hasAbility(Ability.MAGIC_BOUNCE)
                                || opponent.hasAbility(Ability.OVERCOAT))
            )
                return false
            if (id == 34 && (opponent.hasAbility(Ability.MAGIC_BOUNCE) || opponent.hasAbility(Ability.OVERCOAT)))
                return false
            if (id == 55 && opponent.hasType(Type.GROUND))
                return false
            if (status == TIRED && opponent.status != OK)
                return false
            if (status == TRAPPED_WITH_DAMAGE && id == 130 && opponent.hasAbility(Ability.FLASH_FIRE))
                return false
            if (status == TRAPPED_WITH_DAMAGE && id == 131 && (opponent.hasAbility(Ability.WATER_ABSORB) || opponent.hasAbility(
                    Ability.DRY_SKIN
                ))
            )
                return false
            if (opponent.hasAbility(Ability.OWN_TEMPO) && status == CONFUSED)
                return false
            if (!status.activeOutsideBattle && !opponent.battleData!!.battleStatus.contains(status))
                return true
            if (status == LEECH_SEEDED && !opponent.hasType(Type.GRASS))
                return true
            if (opponent.status != OK)
                return false
            if (status == ASLEEP && !opponent.hasAbility(Ability.INSOMNIA) && !opponent.hasAbility(Ability.VITAL_SPIRIT))
                return true
            if (status == FROZEN && !opponent.hasType(Type.ICE) && !opponent.hasAbility(Ability.MAGMA_ARMOR))
                return true
            if (status == BURN && !opponent.hasType(Type.FIRE) && !opponent.hasAbility(Ability.WATER_VEIL))
                return true
            if (status == PARALYSIS && !opponent.hasType(Type.ELECTRIC) && !opponent.hasAbility(Ability.LIMBER))
                return true
            if ((status == POISON || status == BADLY_POISON) && !opponent.hasType(Type.POISON) && !opponent.hasType(Type.STEEL)
                && !opponent.hasAbility(Ability.IMMUNITY)
            )
                return true
            return false
        }
    }
}