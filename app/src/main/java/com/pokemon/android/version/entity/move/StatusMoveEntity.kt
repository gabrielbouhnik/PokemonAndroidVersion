package com.pokemon.android.version.entity.move

data class StatusMoveEntity (var id : Int, var name : String, var power : Int, var pp : Int,
                             var type : Int, var category : String, var accuracy : Int, var description : String,
                             var priorityLevel : Int, var status : String, var probability : Int){
}