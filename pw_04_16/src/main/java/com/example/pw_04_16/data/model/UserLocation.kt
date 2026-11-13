package com.example.pw_04_16.data.model

/**
 * Незмінна модель даних геолокації користувача.
 *
 * @property latitude Поточна широта пристрою.
 * @property longitude Поточна довгота пристрою.
 */
data class UserLocation(
    val latitude: Double,
    val longitude: Double
)
