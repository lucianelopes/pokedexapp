package com.example.pokedexapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.viewpager2.widget.ViewPager2

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var heightTextView: TextView
    private lateinit var weightTextView: TextView
    private lateinit var typeTextView: TextView
    private lateinit var frontImageView: ImageView
    private lateinit var database: PokemonDatabase
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        nameTextView = findViewById(R.id.pokemonNameTextView)
        heightTextView = findViewById(R.id.pokemonHeightTextView)
        weightTextView = findViewById(R.id.pokemonWeightTextView)
        typeTextView = findViewById(R.id.pokemonTypeTextView)
        viewPager = findViewById(R.id.pokemonViewPager)

        val pokemonName = intent.getStringExtra("POKEMON_NAME")
        val pokemonUrl = intent.getStringExtra("POKEMON_URL")

        nameTextView.text = pokemonName

        database = PokemonDatabase.getDatabase(this)


        if (pokemonUrl != null) {
            val pokemonId = extractPokemonIdFromUrl(pokemonUrl)
            fetchPokemonDetails(pokemonUrl, pokemonId)
        }
    }

    private fun extractPokemonIdFromUrl(url: String): Int {
        return url.trimEnd('/').takeLastWhile { it.isDigit() }.toInt()
    }

    private fun fetchPokemonDetails(url: String, pokemonId: Int) {
        ApiService.api.getPokemonDetail(url).enqueue(object : Callback<PokemonDetail> {
            override fun onResponse(call: Call<PokemonDetail>, response: Response<PokemonDetail>) {
                if (response.isSuccessful) {
                    val pokemonDetail = response.body()
                    if (pokemonDetail != null) {
                        heightTextView.text = "Altura: ${pokemonDetail.height}"
                        weightTextView.text = "Largura: ${pokemonDetail.weight}"
                        typeTextView.text = "Tipos: ${pokemonDetail.types.joinToString(", ") { it.type.name }}"
                        // URL of the image
                        val imageUrls = listOf(
                            pokemonDetail.sprites.imgFront,
                            pokemonDetail.sprites.imgBack,
                        )
                        val adapter = ImageCarouselAdapter(this, imageUrls)
                        viewPager.adapter = adapter

                        // Salvar no banco de dados
                        val pokemonEntity = PokemonEntity(
                            id = pokemonId,
                            name = pokemonDetail.name,
                            height = pokemonDetail.height,
                            weight = pokemonDetail.weight,
                            types = pokemonDetail.types.joinToString(", ") { it.type.name },
                            frontImage = pokemonDetail.sprites.imgFront
                        )

                        lifecycleScope.launch {
                            database.pokemonDao().insert(pokemonEntity)
                        }
                    }
                } else {
                    Log.e("API_ERROR", "Failed to get pokemon details: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                Log.e("API_ERROR", "Error: ${t.message}")
            }
        })
    }
}