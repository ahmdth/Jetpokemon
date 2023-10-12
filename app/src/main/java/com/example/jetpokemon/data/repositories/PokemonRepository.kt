package com.example.jetpokemon.data.repositories

import com.example.jetpokemon.data.remote.PokemonApi
import com.example.jetpokemon.models.Pokemon
import com.example.jetpokemon.models.PokemonList
import com.example.jetpokemon.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val pokemonApi: PokemonApi
) {
    suspend fun getPokemonList(
        limit: Int, offset: Int
    ): Resource<PokemonList> {
        val response = try {
            pokemonApi.getPokemonList(limit, offset)
        } catch (ex: Exception) {
            return Resource.Error("error fetching pokemon list")
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(name: String): Resource<Pokemon> {
        val response = try {
            pokemonApi.getPokemonInfo(name)
        } catch (ex: Exception) {
            return Resource.Error("error fetching single pokemon")
        }
        return Resource.Success(response)
    }
}