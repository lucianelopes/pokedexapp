package com.example.pokedexapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonViewModel : ViewModel() {

    val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>> = _pokemonList

    init {
        fetchPokemonData()
    }

    private fun fetchPokemonData() {
        ApiService.api.getPokemonList().enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    _pokemonList.value = response.body()?.results ?: emptyList()
                } else {
                    // Handle the error case
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                // Handle the failure case
            }
        })
    }
}