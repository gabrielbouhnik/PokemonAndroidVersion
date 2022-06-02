package com.pokemon.android.version.entity.banner

data class BannerEntity(var description : String, var image : String, var cost : Int,
                var pokemons : List<PokemonBannerEntity>, var items :List<ItemBannerEntity>) {

}