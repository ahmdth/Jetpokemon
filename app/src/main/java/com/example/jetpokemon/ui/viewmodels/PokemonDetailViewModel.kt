package com.example.jetpokemon.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.jetpokemon.data.repositories.PokemonRepository
import com.example.jetpokemon.models.Pokemon
import com.example.jetpokemon.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {
    suspend fun getPokemonInfo(name: String) : Resource<Pokemon> {
        return repository.getPokemonInfo(name)
    }
}