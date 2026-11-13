package com.example.pw_04_15.ui

import com.example.pw_04_15.data.model.InstallationStatus
import com.example.pw_04_15.data.model.InstallationType
import com.example.pw_04_15.data.model.PowerInstallation

/**
 * Критерії сортування списку електроустановок.
 *
 * @property displayName Українська назва критерію для відображення на чипах.
 */
enum class SortCriterion(val displayName: String) {
    NAME("Назва"),
    TYPE("Тип"),
    POWER("Потужність"),
    DATE("Дата")
}

/**
 * Стан користувацького інтерфейсу реєстру електроустановок.
 *
 * @property allInstallations Повний початковий список установок з репозиторію.
 * @property displayedInstallations Відфільтрований та відсортований список для відображення.
 * @property searchQuery Поточний пошуковий запит за назвою.
 * @property selectedType Обраний фільтр за типом (null — відображати всі типи).
 * @property selectedStatus Обраний фільтр за статусом (null — відображати всі статуси).
 * @property sortCriterion Поточний обраний критерій сортування.
 * @property selectedInstallation Обрана електроустановка для детального перегляду (null — нічого не обрано).
 */
data class InstallationsUiState(
    val allInstallations: List<PowerInstallation> = emptyList(),
    val displayedInstallations: List<PowerInstallation> = emptyList(),
    val searchQuery: String = "",
    val selectedType: InstallationType? = null,
    val selectedStatus: InstallationStatus? = null,
    val sortCriterion: SortCriterion = SortCriterion.NAME,
    val selectedInstallation: PowerInstallation? = null
)
