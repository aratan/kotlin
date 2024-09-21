package com.example.dosomas.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dosomas.databinding.FragmentGalleryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import androidx.recyclerview.widget.RecyclerView
import com.example.dosomas.R

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding is only valid between onCreateView and onDestroyView")
    private lateinit var adapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        setupRecyclerView()
        loadCharacters() // Llamada a la API para obtener los personajes

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = CharacterAdapter(listOf())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@GalleryFragment.adapter
        }
    }

    private fun loadCharacters() {
        val apiService = RetrofitClient.instance.create(RickAndMortyApi::class.java)
        apiService.getCharacters(1).enqueue(object : Callback<CharacterResponse> {
            override fun onResponse(call: Call<CharacterResponse>, response: Response<CharacterResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.updateCharacters(it.results)  // Actualizar el adapter con los datos obtenidos
                    }
                } else {
                    Log.e("GalleryFragment", "Error en la respuesta de la API")
                    Toast.makeText(context, "Error al cargar personajes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                Log.e("GalleryFragment", "Error en la API", t)
                Toast.makeText(context, "Error en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Retrofit client object
    object RetrofitClient {
        private const val BASE_URL = "https://rickandmortyapi.com/api/"

        val instance: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    // API Interface
    interface RickAndMortyApi {
        @GET("character")
        fun getCharacters(@Query("page") page: Int): Call<CharacterResponse>
    }

    // Models
    data class CharacterResponse(
        val info: Info,
        val results: List<Character>
    )

    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )

    data class Character(
        val id: Int,
        val name: String,
        val status: String,
        val species: String,
        val type: String,
        val gender: String,
        val origin: Location,
        val location: Location,
        val image: String,
        val episode: List<String>,
        val url: String,
        val created: String
    )

    data class Location(
        val name: String,
        val url: String
    )

    // Adapter
    class CharacterAdapter(private var characters: List<Character>) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

        fun updateCharacters(newCharacters: List<Character>) {
            characters = newCharacters
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
            val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
            return CharacterViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
            holder.bind(characters[position])
        }

        override fun getItemCount(): Int = characters.size

        class CharacterViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {
            fun bind(character: Character) {
                val nameTextView = binding.findViewById<TextView>(R.id.textViewName)
                val statusTextView = binding.findViewById<TextView>(R.id.textViewStatus)
                val imageView = binding.findViewById<ImageView>(R.id.imageView)

                nameTextView.text = character.name
                statusTextView.text = character.status

                // Cargar la imagen usando Glide
                Glide.with(imageView.context)
                    .load(character.image)
                    .into(imageView)
            }
        }
    }
}
