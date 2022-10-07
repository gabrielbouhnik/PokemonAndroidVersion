package com.pokemon.android.version.model

enum class Ability(var description : String) {
    ADAPTABILITY("Powers up moves of the same type as the Pokémon."),
    BLAZE("Powers up Fire-type moves when the Pokémon's HP is low."),
    GUTS("It's so gutsy that having a status condition boosts the Pokémon's Attack stat."),
    IMMUNITY("The immune system of the Pokémon prevents it from getting poisoned."),
    INNER_FOCUS("The Pokémon's intensely focused, and that protects the Pokémon from flinching."),
    INTIMIDATE("The Pokémon intimidates opposing Pokémon upon entering battle, lowering their Attack stat."),
    LEVITATE("By floating in the air, the Pokémon receives full immunity to all Ground-type moves."),
    LIGHTNING_ROD("The Pokémon draws in all Electric-type moves. Instead of being hit by Electric-type moves, it boosts its Sp. Atk."),
    LIMBER("Its limber body protects the Pokémon from paralysis."),
    MOXIE("The Pokémon shows moxie, and that boosts the Attack stat after knocking out any Pokémon."),
    NO_GUARD("The Pokémon employs no-guard tactics to ensure incoming and outgoing attacks always land."),
    OVERGROW("Powers up Grass-type moves when the Pokémon's HP is low."),
    OWN_TEMPO("This Pokémon has its own tempo, and that prevents it from becoming confused."),
    ROCK_HEAD("Protects the Pokémon from recoil damage."),
    SPEED_BOOST("Its Speed stat is boosted every turn."),
    STURDY("It cannot be knocked out with one hit. One-hit KO moves cannot knock it out, either."),
    SUPER_LUCK("The Pokémon is so lucky that the critical-hit ratios of its moves are boosted."),
    SWARM("Powers up Bug-type moves when the Pokémon's HP is low."),
    TECHNICIAN("Powers up the Pokémon's weaker moves."),
    THICK_FAT("The Pokémon is protected by a layer of thick fat, which halves the damage taken from Fire- and Ice-type moves."),
    TORRENT("Powers up Water-type moves when the Pokémon's HP is low."),
    VOLT_ABSORB("Restores HP if hit by an Electric-type move instead of taking damage."),
    WATER_ABSORB("Restores HP if hit by a Water-type move instead of taking damage.")
}