package com.example.pw_04_16.data.model

/**
 * Статуси роботи промислових електроустановок.
 *
 * @property displayName Українська назва статусу для інтерфейсу.
 */
enum class InstallationStatus(val displayName: String) {
    ACTIVE("Активна"),
    MAINTENANCE("На ремонті"),
    RESERVE("У резерві")
}
