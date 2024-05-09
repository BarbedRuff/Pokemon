package com.vk.pokemon.model

data class Pokemon(
    val id: Int,
    val name: String,
    val sprite: String,
    val height: Int,
    val weight: Int,
    val baseExperience: Int,
)