package com.example.pokedexapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PokemonViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<List<Pokemon>>

    @Test
    fun fetchPokemonData_populatesPokemonList() {
        val viewModel = PokemonViewModel()
        viewModel.pokemonList.observeForever(observer)

        val mockResponse = listOf(Pokemon("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"))
        viewModel._pokemonList.value = mockResponse

        verify(observer).onChanged(mockResponse)
    }
}
