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
    private val _state = MutableStateFlow("")
    private val repository = PokemonRepository()
    private var start = 1
    var pokemons = _pokemons.asStateFlow()
    var state = _state.asStateFlow()
    fun fetchPokemons(listSize: Int){
        if(_state.value != "Loading"){
            _state.value = if(start != 1) "Loading" else "Init"
            viewModelScope.launch {
                try{
                    repository.getPokemons(listSize, start)
                        .flowOn(Dispatchers.IO)
                        .collect{
                            _state.value = "Loaded"
                            val pokemonsList = _pokemons.value.toMutableList()
                            pokemonsList.addAll(it)
                            start = pokemonsList.last().id + 1
                            _pokemons.value = pokemonsList.toList()
                        }
                } catch (e: Exception){
                    _state.value = "Error"
                    Log.d("Loading error", e.message.toString())
                }
            }
        }
    }
}