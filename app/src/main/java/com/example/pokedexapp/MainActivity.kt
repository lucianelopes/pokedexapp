package com.example.pokedexapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), MyAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private val viewModel: PokemonViewModel by viewModels()
    private lateinit var pokemonList: List<Pokemon>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        viewModel.pokemonList.observe(this, Observer { pokemons ->
            if (pokemons != null) {
                pokemonList = pokemons
                adapter = MyAdapter(pokemonList.map { it.name }, this)
                recyclerView.adapter = adapter
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