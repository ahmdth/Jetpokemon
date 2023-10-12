package com.example.jetpokemon.di

import com.example.jetpokemon.data.remote.PokemonApi
import com.example.jetpokemon.data.repositories.PokemonRepository
import com.example.jetpokemon.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePokemonRepository(
        pokemonApi: PokemonApi
    ) = PokemonRepository(pokemonApi)

    @Provides
    @Singleton
    fun providePokemonApi(): PokemonApi =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(PokemonApi::class.java)
}