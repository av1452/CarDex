package com.aidancode.cardex

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val xpReward: Int,
    val unlocked: Boolean = false
)

val achievements = listOf(

    Achievement(
        id = "first_spot",
        title = "First Spot",
        description = "Spot your first car.",
        icon = "🚗",
        xpReward = 25
    ),

    Achievement(
        id = "ten_cars",
        title = "Getting Started",
        description = "Discover 10 cars.",
        icon = "🔟",
        xpReward = 50
    ),

    Achievement(
        id = "twenty_five_cars",
        title = "Car Collector",
        description = "Discover 25 cars.",
        icon = "🏆",
        xpReward = 100
    ),

    Achievement(
        id = "fifty_cars",
        title = "Dedicated Collector",
        description = "Discover 50 cars.",
        icon = "🔥",
        xpReward = 200
    ),

    Achievement(
        id = "rare_find",
        title = "Rare Find",
        description = "Spot your first Rare car.",
        icon = "⭐",
        xpReward = 50
    ),

    Achievement(
        id = "epic_find",
        title = "Epic Find",
        description = "Spot your first Epic car.",
        icon = "💎",
        xpReward = 100
    ),

    Achievement(
        id = "legendary_find",
        title = "Legendary Hunter",
        description = "Spot your first Legendary car.",
        icon = "👑",
        xpReward = 250
    )
)

fun getUnlockedAchievements(
    cars: List<com.aidancode.cardex.model.Car>
): List<String> {

    val unlocked = mutableListOf<String>()

    if (cars.isNotEmpty()) {
        unlocked.add("first_spot")
    }

    if (cars.size >= 10) {
        unlocked.add("ten_cars")
    }

    if (cars.size >= 25) {
        unlocked.add("twenty_five_cars")
    }

    if (cars.size >= 50) {
        unlocked.add("fifty_cars")
    }

    if (cars.any { it.rarity == com.aidancode.cardex.model.Rarity.RARE }) {
        unlocked.add("rare_find")
    }

    if (cars.any { it.rarity == com.aidancode.cardex.model.Rarity.EPIC }) {
        unlocked.add("epic_find")
    }

    if (cars.any { it.rarity == com.aidancode.cardex.model.Rarity.LEGENDARY }) {
        unlocked.add("legendary_find")
    }

    return unlocked
}