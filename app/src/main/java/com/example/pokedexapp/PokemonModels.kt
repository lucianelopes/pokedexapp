package com.example.pokedexapp

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
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<Type>
)

data class Type(
    val type: TypeInfo
)

data class TypeInfo(
    val name: String
)

