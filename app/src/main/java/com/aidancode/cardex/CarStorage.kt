package com.aidancode.cardex

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aidancode.cardex.model.Car
import com.aidancode.cardex.model.Rarity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import androidx.datastore.preferences.core.stringSetPreferencesKey

private val Context.dataStore by preferencesDataStore(
    name = "cardex_data"
)

class CarStorage(
    private val context: Context
) {

    private val carsKey = stringPreferencesKey("cars")

    private val unlockedAchievementsKey =
        stringSetPreferencesKey("unlocked_achievements")

    suspend fun saveUnlockedAchievements(
        achievementIds: Set<String>
    ) {
        context.dataStore.updateData { preferences ->

            preferences.toMutablePreferences().apply {

                this[unlockedAchievementsKey] = achievementIds
            }
        }
    }

    val unlockedAchievements: Flow<Set<String>> =
        context.dataStore.data.map { preferences ->

            preferences[unlockedAchievementsKey] ?: emptySet()
        }

    val cars: Flow<List<Car>> =
        context.dataStore.data.map { preferences ->

            val json = preferences[carsKey] ?: "[]"

            carsFromJson(json)
        }

    suspend fun saveCars(
        cars: List<Car>
    ) {

        context.dataStore.updateData { preferences ->

            preferences.toMutablePreferences().apply {

                this[carsKey] = carsToJson(cars)
            }
        }
    }

    private fun carsToJson(
        cars: List<Car>
    ): String {

        val array = JSONArray()

        cars.forEach { car ->

            val objectJson = JSONObject()

            objectJson.put(
                "id",
                car.id
            )

            objectJson.put(
                "manufacturer",
                car.manufacturer
            )

            objectJson.put(
                "model",
                car.model
            )

            objectJson.put(
                "generation",
                car.generation
            )

            objectJson.put(
                "year",
                car.year
            )

            objectJson.put(
                "trim",
                car.trim
            )

            objectJson.put(
                "rarity",
                car.rarity.name
            )

            objectJson.put(
                "xp",
                car.xp
            )

            objectJson.put(
                "dateSpotted",
                car.dateSpotted
            )

            objectJson.put(
                "photoUri",
                car.photoUri
            )

            array.put(objectJson)
        }

        return array.toString()
    }

    private fun carsFromJson(
        json: String
    ): List<Car> {

        val array = JSONArray(json)

        val cars = mutableListOf<Car>()

        for (i in 0 until array.length()) {

            val objectJson = array.getJSONObject(i)

            cars.add(
                Car(
                    id = objectJson.getInt("id"),

                    manufacturer = objectJson.getString(
                        "manufacturer"
                    ),

                    model = objectJson.getString(
                        "model"
                    ),

                    generation = objectJson.getString(
                        "generation"
                    ),

                    year = objectJson.optInt(
                        "year",
                        0
                    ),

                    trim = objectJson.optString(
                        "trim",
                        "Unknown"
                    ),

                    rarity = Rarity.valueOf(
                        objectJson.getString(
                            "rarity"
                        )
                    ),

                    xp = objectJson.getInt(
                        "xp"
                    ),

                    dateSpotted = objectJson.optLong(
                        "dateSpotted",
                        0L
                    ),

                    photoUri = objectJson.optString(
                        "photoUri",
                        ""
                    )
                )
            )
        }

        return cars
    }
}