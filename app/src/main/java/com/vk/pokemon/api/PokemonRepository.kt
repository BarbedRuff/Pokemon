package com.vk.pokemon.api

import com.vk.pokemon.core.Retrofit
import com.vk.pokemon.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokemonRepository {
    private val api: PokemonApi by lazy { Retrofit.getClient().create(PokemonApi::class.java) }
    private val maxStats = listOf(255, 181, 230, 173, 230, 200)

    suspend fun getPokemons(listSize: Int, start: Int): Flow<List<Pokemon>>{
        val pokemons = mutableListOf<Pokemon>()
        for(it in 0..<listSize){
            try{
                val pokemon = api.getPokemons(start + it)
                pokemons.add(
                    Pokemon(
                        pokemon.id,
                        pokemon.name.replaceFirst(pokemon.name[0], pokemon.name[0].uppercaseChar()),
                        pokemon.sprites.frontDefault,
                        pokemon.height,
                        pokemon.weight,
                        pokemon.baseExperience,
                        (0..5).map{ pokemon.stats[it].baseStat / maxStats[it].toFloat() }
                    )
                )
            } catch (e: Exception){
                throw e
            }
        }
        return flow{ emit(pokemons) }
    }
}