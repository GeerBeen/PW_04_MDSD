package com.example.pw_04_16.ui

/**
 * Конфігураційні константи для мапи osmdroid та системного GPS-моніторингу.
 */
object MapConfig {
    /** Початкова широта центру мапи */
    const val INITIAL_LATITUDE: Double = 48.4647

    /** Початкова довгота центру мапи */
    const val INITIAL_LONGITUDE: Double = 35.0462

    /** Початковий рівень масштабування мапи */
    const val INITIAL_ZOOM: Double = 12.0

    /** Рівень масштабування під час фокусування на обраному об'єкті */
    const val FOCUS_ZOOM: Double = 15.0

    /** Інтервал оновлення координат від LocationManager (у мс) */
    const val LOCATION_UPDATE_INTERVAL_MS: Long = 5000L

    /** Мінімальна дистанція для сповіщення про зміну локації (в метрах) */
    const val LOCATION_MIN_DISTANCE_METERS: Float = 10.0f

    /** Назва джерела тайлів */
    const val TILE_SOURCE_NAME: String = "OpenTopoMap"

    /** Базова адреса джерела растрових тайлів OpenTopoMap */
    const val TILE_SOURCE_BASE_URL: String = "https://a.tile.opentopomap.org/"

    /** Розширення файлів тайлів */
    const val TILE_SOURCE_EXTENSION: String = ".png"
}
