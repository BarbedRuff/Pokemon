package com.vk.pokemon.api

import com.vk.pokemon.model.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApi {
    @GET("pokemon/{pokemonId}")
    suspend fun getPokemons(@Path("pokemonId") pokemonId: Int): Response
}