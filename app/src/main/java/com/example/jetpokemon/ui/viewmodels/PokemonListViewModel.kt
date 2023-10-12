package com.example.jetpokemon.ui.viewmodels

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.jetpokemon.data.models.PokemonEntry
import com.example.jetpokemon.data.repositories.PokemonRepository
import com.example.jetpokemon.utils.Constants
import com.example.jetpokemon.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var currentPage = 0
    var pokemonList = mutableStateOf(listOf<PokemonEntry>())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    var checkedPokemonList = listOf<PokemonEntry>()
    var isSearchedStarting = true
    var isSearching = mutableStateOf(false)


    init {
        loadPokemonPaginated()
    }

    fun searchPokemonList(query: String) {
        var listToSearch = if (isSearchedStarting) {
            pokemonList.value
        } else {
            checkedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()){
                pokemonList.value = checkedPokemonList
                isSearching.value = false
                isSearchedStarting = true
                return@launch
            }
            val result = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) || it.number.toString() == query.trim()
            }
            if(isSearchedStarting){
                checkedPokemonList = pokemonList.value
                isSearchedStarting = false
            }
            pokemonList.value = result
            isSearchedStarting = true
        }
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result =
                repository.getPokemonList(Constants.PAGE_SIZE, currentPage * Constants.PAGE_SIZE)
            when (result) {
                is Resource.Success -> {
                    endReached.value = currentPage * Constants.PAGE_SIZE >= result.data!!.count
                    var pokemonEntries = result.data.results.mapIndexed { _, entry ->
                        val number = if (entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokemonEntry(entry.name.capitalize(Locale.ROOT), url, number.toInt())
                    }
                    currentPage++
                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokemonEntries
                }

                is Resource.Loading -> {
                    // handle loading
                }

                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

    fun calcDominantColor(
        drawable: Drawable,
        onFinished: (Color) -> Unit
    ) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinished(Color(colorValue))
            }
        }
    }
}