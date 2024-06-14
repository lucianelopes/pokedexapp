package com.example.pokedexapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface PokeApi {
    @GET("pokemon")
    fun getPokemonList(): Call<PokemonResponse>

    @GET
    fun getPokemonDetail(@Url url: String): Call<PokemonDetail>
}
