package com.aidancode.cardex.model

data class Car(
    val id: Int,
    val manufacturer: String,
    val model: String,
    val generation: String,
    val year: Int,
    val trim: String,
    val rarity: Rarity,
    val xp: Int,
    val dateSpotted: Long
)

enum class Rarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY
}