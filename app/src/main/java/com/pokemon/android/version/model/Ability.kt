package com.pokemon.android.version.model

enum class Ability(var description: String) {
    ADAPTABILITY("Powers up moves of the same type as the Pokémon."),
    AFTERMATH("Damages the attacker if it contacts the Pokémon with a finishing hit."),
    ANGER_POINT("Increases Attack to maximum level upon taking a critical hit."),
    ANTICIPATION("The Pokémon can sense an opposing Pokémon's dangerous moves."),
    ARENA_TRAP("Prevents opposing Pokémon from fleeing."),
    ARMOR_TAIL("The mysterious tail covering the Pokémon's head makes opponents unable to use priority moves against the Pokémon."),
    BATTLE_ARMOR("Hard armor protects the Pokémon from critical hits."),
    BIG_PECKS("Protects the Pokémon from Defense-lowering effects."),
    BLAZE("Powers up Fire-type moves when the Pokémon's HP is low."),
    BULLETPROOF("Protects the Pokémon from some ball and bomb moves."),
    FLASH_FIRE("Powers up the Pokémon's Fire-type moves if it's hit by one."),
    CLEAR_BODY("Prevents other Pokémon's moves or Abilities from lowering the Pokémon's stats."),
    COMPETITIVE("Boosts the Sp. Atk stat sharply when a stat is lowered."),
    CURSED_BODY("May disable a move used on the Pokémon."),
    DAMP("Prevents the use of explosive moves such as Self-Destruct by dampening its surroundings."),
    DEFIANT("Boosts the Pokémon's Attack stat sharply when its stats are lowered."),
    DELTA_STREAM("Creates the Strong Winds weather. This lasts until the Pokémon exits battle, and weakens moves used on Flying-type Pokémon that would be Super Effective"),
    DOWNLOAD("Compares an opposing Pokémon's Defense and Sp. Def stats before raising its own Attack or Sp. Atk stat—whichever will be more effective."),
    DRY_SKIN("Restores HP when hit by Water-type moves and increases the damage received from Fire-type moves."),
    EARLY_BIRD("The Pokémon awakens twice as fast as other Pokémon from sleep."),
    EFFECT_SPORE("Contact with the Pokémon may inflict poison, sleep, or paralysis on its attacker."),
    FILTER("Reduces the power of supereffective attacks taken."),
    FLAME_BODY("Contact with the Pokémon may burn the attacker."),
    FRISK("When it enters a battle, the Pokémon can check an opposing Pokémon's held item."),
    GALE_WINGS("All Flying-type moves have their Speed Priority increased by 1 if the Pokémon's Hit Points are at maximum"),
    GUTS("It's so gutsy that having a status condition boosts the Pokémon's Attack stat."),
    HYPER_CUTTER("The Pokémon's proud of its powerful pincers. They prevent other Pokémon from lowering its Attack stat."),
    IMMUNITY("The immune system of the Pokémon prevents it from getting poisoned."),
    INNER_FOCUS("The Pokémon's intensely focused, and that protects the Pokémon from flinching."),
    INSOMNIA("The Pokémon is suffering from insomnia and cannot fall asleep."),
    INTIMIDATE("The Pokémon intimidates opposing Pokémon upon entering battle, lowering their Attack stat."),
    IRON_BARBS("Inflicts damage to the attacker on contact with iron barbs."),
    IRON_FIST("Powers up punching moves."),
    JUSTIFIED("Being hit by a Dark-type move boosts the Attack stat of the Pokémon, for justice."),
    KEEN_EYE("Keen eyes prevent other Pokémon from lowering this Pokémon's accuracy."),
    LEVITATE("By floating in the air, the Pokémon receives full immunity to all Ground-type moves."),
    LIGHTNING_ROD("The Pokémon draws in all Electric-type moves. Instead of being hit by Electric-type moves, it boosts its Sp. Atk."),
    LIMBER("Its limber body protects the Pokémon from paralysis."),
    LIQUID_OOZE("Oozed liquid has strong stench, which damages attackers using any draining move."),
    MAGIC_BOUNCE("Reflects non-attacking moves used on the Pokémon back to the attacking Pokémon."),
    MAGIC_GUARD("The Pokémon only takes damage from attacks."),
    MAGMA_ARMOR("The Pokémon is covered with hot magma, which prevents the Pokémon from becoming frozen."),
    MAGNET_PULL("Prevents Steel-type Pokémon from escaping using its magnetic force."),
    MARVEL_SCALE("The Pokémon's marvelous scales boost the Defense stat if it has a status condition."),
    MEGA_LAUNCHER("Powers up aura and pulse moves."),
    MOTOR_DRIVE("The Pokémon takes no damage from Electric-type attacks and Speed raises by one level."),
    MOXIE("The Pokémon shows moxie, and that boosts the Attack stat after knocking out any Pokémon."),
    MULTISCALE("Reduces damage afflicted to the Pokémon by 50% if at maximum HP"),
    NATURAL_CURE("All status conditions heal when the Pokémon switches out."),
    NO_GUARD("The Pokémon employs no-guard tactics to ensure incoming and outgoing attacks always land."),
    OVERCOAT("Protects the Pokémon from powder."),
    OVERGROW("Powers up Grass-type moves when the Pokémon's HP is low."),
    OWN_TEMPO("This Pokémon has its own tempo, and that prevents it from becoming confused."),
    PICKUP("The Pokémon will pick up an item after a battle."),
    POISON_HEAL("Restores HP if the Pokémon is poisoned, instead of losing HP."),
    POISON_POINT("Contact with the Pokémon may poison the attacker."),
    POISON_TOUCH("May poison a target when the Pokémon makes contact."),
    PRESSURE("By putting pressure on the opposing Pokémon, it raises their PP usage."),
    PRANKSTER("Gives priority to a status move."),
    QUICK_FEET("Boosts the Speed stat if the Pokémon has a status condition."),
    RECKLESS("Powers up moves that have recoil damage."),
    REGENERATOR("Restores a little HP when withdrawn from battle."),
    ROCK_HEAD("Protects the Pokémon from recoil damage."),
    ROUGH_SKIN("This Pokémon inflicts damage with its rough skin to the attacker on contact."),
    SAND_STREAM("Sandstorm blows when the Pokémon is on the battle. It raises the Special Defense of Rock-type Pokémon by 50%."),
    SCRAPPY("The Pokémon can hit Ghost-type Pokémon with Normal- and Fighting-type moves."),
    SERENE_GRACE("Boosts the likelihood of additional effects occurring when attacking."),
    SHADOW_TAG("This Pokémon steps on the opposing Pokémon's shadow to prevent it from escaping."),
    SHARPNESS("Powers up slicing moves."),
    SHED_SKIN("The Pokémon may heal its own status conditions by shedding its skin."),
    SHEER_FORCE("Removes additional effects to increase the power of moves when attacking."),
    SHELL_ARMOR("A hard shell protects the Pokémon from critical hits."),
    SKILL_LINK("Maximizes the number of times multi-strike moves hit."),
    SNIPER("Powers up moves if they become critical hits when attacking."),
    SOUNDPROOF("Soundproofing of the Pokémon itself gives full immunity to all sound-based moves."),
    SOLID_ROCK("Reduces the power of supereffective attacks taken."),
    SPEED_BOOST("Its Speed stat is boosted every turn."),
    STATIC("The Pokémon is charged with static electricity, so contact with it may cause paralysis."),
    STEADFAST("The Pokémon's determination boosts the Speed stat each time the Pokémon flinches."),
    STENCH("By releasing stench when attacking, this Pokémon may cause the target to flinch."),
    STICKY_HOLD("Items held by the Pokémon are stuck fast and cannot be removed by other Pokémon."),
    STURDY("It cannot be knocked out with one hit. One-hit KO moves cannot knock it out, either."),
    SUPER_LUCK("The Pokémon is so lucky that the critical-hit ratios of its moves are boosted."),
    SWARM("Powers up Bug-type moves when the Pokémon's HP is low."),
    SYNCHRONIZE("The attacker will receive the same status condition if it inflicts a burn, poison, or paralysis to the Pokémon."),
    TECHNICIAN("Powers up the Pokémon's weaker moves."),
    THICK_FAT("The Pokémon is protected by a layer of thick fat, which halves the damage taken from Fire- and Ice-type moves."),
    TINTED_LENS("The Pokémon can use “not very effective” moves to deal regular damage."),
    TORRENT("Powers up Water-type moves when the Pokémon's HP is low."),
    TOUGH_CLAW("Powers up moves that make direct contact."),
    UNBURDEN("Boosts the Speed stat if the Pokémon's held item is used or lost."),
    UNAWARE("When attacking, the Pokémon ignores the target Pokémon's stat changes."),
    VITAL_SPIRIT("The Pokémon is full of vitality, and that prevents it from falling asleep."),
    VOLT_ABSORB("Restores HP if hit by an Electric-type move instead of taking damage."),
    WATER_ABSORB("Restores HP if hit by a Water-type move instead of taking damage."),
    WATER_VEIL("The Pokémon is covered with a water veil, which prevents the Pokémon from getting a burn."),
    WONDER_SKIN("Makes status moves more likely to miss.");
}