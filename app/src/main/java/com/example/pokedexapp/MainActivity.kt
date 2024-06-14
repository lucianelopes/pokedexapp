package com.example.pokedexapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), MyAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var pokemonList: List<Pokemon>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        fetchPokemonData()
    }

    private fun fetchPokemonData() {
        ApiService.api.getPokemonList().enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    pokemonList = response.body()?.results ?: emptyList()

                    // Logar a resposta completa da API
                    Log.d("API_RESPONSE", response.body().toString())

                    // Logar cada Pokémon individualmente
                    pokemonList.forEach { pokemon ->
                        Log.d("POKEMON", "Name: ${pokemon.name}, URL: ${pokemon.url}")
                    }

                    // Passar o listener para o adapter
                    adapter = MyAdapter(pokemonList.map { it.name }, this@MainActivity)
                    recyclerView.adapter = adapter
                } else {
                    Log.e("API_ERROR", "Failed to get data: ${response.errorBody()?.string()}")
                    Toast.makeText(this@MainActivity, "Failed to get data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                Log.e("API_ERROR", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(position: Int) {
        val selectedPokemon = pokemonList[position]
        val intent = Intent(this, PokemonDetailActivity::class.java).apply {
            putExtra("POKEMON_NAME", selectedPokemon.name)
            putExtra("POKEMON_URL", selectedPokemon.url)
        }
        startActivity(intent)
    }
}