package com.vk.pokemon.api

import com.vk.pokemon.core.Retrofit
import com.vk.pokemon.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokemonRepository {
    private val api: PokemonApi by lazy { Retrofit.getClient().create(PokemonApi::class.java) }

    suspend fun getPokemons(listSize: Int, start: Int): Flow<List<Pokemon>>{
        return flow{
            emit(
                List(listSize
                ) {
                    val pokemon = api.getPokemons(start + it)
                    Pokemon(
                        pokemon.id,
                        pokemon.name,
                        pokemon.sprites.frontDefault,
                        pokemon.height,
                        pokemon.weight,
                        pokemon.baseExperience
                    )
                }
            )
        }
    }
}