package com.example.pw_04_16.data.model

/**
 * Типи промислових електроустановок.
 *
 * @property displayName Українська назва типу для інтерфейсу.
 */
enum class InstallationType(val displayName: String) {
    SUBSTATION("Підстанція"),
    GENERATOR("Генератор"),
    ELECTRIC_DRIVE("Електропривод"),
    TECHNOLOGICAL_UNIT("Технологічна установка")
}
