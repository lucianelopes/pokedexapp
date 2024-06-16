package com.example.pokedexapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.viewpager2.widget.ViewPager2
import android.media.MediaPlayer

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var heightTextView: TextView
    private lateinit var weightTextView: TextView
    private lateinit var typeTextView: TextView
    private lateinit var frontImageView: ImageView
    private lateinit var database: PokemonDatabase
    private lateinit var viewPager: ViewPager2
    private lateinit var buttonCries: Button
    private var mediaPlayer: MediaPlayer? = null
    private var soundUrl: String = ""
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

        buttonCries = findViewById(R.id.startAnimationButton)

        buttonCries.setOnClickListener {
            startScaleAnimation(viewPager)
            playSoundFromUrl()
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
                        soundUrl = pokemonDetail.cries.url
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

    private fun startScaleAnimation(imageView: ViewPager2) {
        // Create scale up animation
        val scaleUpX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.5f)
        val scaleUpY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.5f)

        // Create scale down animation
        val scaleDownX = ObjectAnimator.ofFloat(imageView, "scaleX", 1.5f, 1f)
        val scaleDownY = ObjectAnimator.ofFloat(imageView, "scaleY", 1.5f, 1f)

        // Combine animations into a sequence
        val scaleUp = AnimatorSet().apply {
            playTogether(scaleUpX, scaleUpY)
            duration = 1500 // 1.5 seconds
        }

        val scaleDown = AnimatorSet().apply {
            playTogether(scaleDownX, scaleDownY)
            duration = 1500 // 1.5 seconds
        }

        // Play the scale up and scale down animations sequentially
        val scaleAnimation = AnimatorSet().apply {
            playSequentially(scaleUp, scaleDown)
        }

        // Start the animation
        scaleAnimation.start()
    }

    private fun playSoundFromUrl() {
        // Release any existing MediaPlayer instance
        mediaPlayer?.release()

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer().apply {
            setDataSource(soundUrl)
            prepareAsync() // Prepare asynchronously to not block the main thread
            setOnPreparedListener { start() }
            setOnCompletionListener { release() } // Release when playback is complete
            setOnErrorListener { mp, what, extra ->
                mp.release()
                true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release MediaPlayer resources if still playing
        mediaPlayer?.release()
    }
}