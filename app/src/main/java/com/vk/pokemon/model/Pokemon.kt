package com.vk.pokemon.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Pokemon(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("sprite")
    val sprite: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("baseExperience")
    val baseExperience: Int,
    @SerializedName("stats")
    val stats: List<Float>
): Serializable