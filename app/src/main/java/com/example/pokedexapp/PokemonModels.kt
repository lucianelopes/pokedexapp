package com.example.pokedexapp

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>
)

data class Pokemon(
    val name: String,
    val url: String
)

data class PokemonDetail(
    var id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<Type>,
    @SerializedName("sprites") val sprites: Sprites
)

data class Type(
    val type: TypeInfo
)

data class TypeInfo(
    val name: String
)

data class Sprites(
    @SerializedName("front_default") val imgFront: String,
    @SerializedName("back_default") val imgBack: String,
    @SerializedName("showdown") val sprites: Showdown
)

data class Showdown(
    @SerializedName("front_default") val imgFront: String,
    @SerializedName("back_default") val imgBack: String,

)