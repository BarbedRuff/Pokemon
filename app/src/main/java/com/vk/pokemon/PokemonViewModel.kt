package com.vk.pokemon

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.pokemon.api.PokemonRepository
import com.vk.pokemon.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PokemonViewModel: ViewModel() {
    private val _pokemons = MutableStateFlow(listOf<Pokemon>())
    private val repository = PokemonRepository()
    private var start = 1
    var pokemons = _pokemons.asStateFlow()
    fun fetchPokemons(listSize: Int){
        viewModelScope.launch {
            repository.getPokemons(listSize, start)
                .flowOn(Dispatchers.IO)
                .catch{
                    Log.d("Pokemon Error", it.message.toString())
                }
                .collect{
                    val pokemonsList = _pokemons.value.toMutableList()
                    pokemonsList.addAll(it)
                    _pokemons.value = pokemonsList.toList()
                    start += listSize
                }
        }
    }
}