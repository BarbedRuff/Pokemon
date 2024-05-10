package com.vk.pokemon.api

import android.util.Log
import com.vk.pokemon.core.Retrofit
import com.vk.pokemon.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokemonRepository {
    private val api: PokemonApi by lazy { Retrofit.getClient().create(PokemonApi::class.java) }

    suspend fun getPokemons(listSize: Int, start: Int): Flow<List<Pokemon>>{
        val pokemons = mutableListOf<Pokemon>()
        for(it in 0..<listSize){
            try{
                val pokemon = api.getPokemons(start + it)
                pokemons.add(
                    Pokemon(
                        pokemon.id,
                        pokemon.name,
                        pokemon.sprites.frontDefault,
                        pokemon.height,
                        pokemon.weight,
                        pokemon.baseExperience
                    )
                )
            } catch (e: Exception){
                Log.d("Pokemon Error", e.message.toString())
            }
        }
        return flow{ emit(pokemons) }
    }
}