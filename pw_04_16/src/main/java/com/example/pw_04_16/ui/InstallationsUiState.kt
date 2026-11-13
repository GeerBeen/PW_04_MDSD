package com.example.pw_04_16.ui

import com.example.pw_04_16.data.model.InstallationStatus
import com.example.pw_04_16.data.model.InstallationType
import com.example.pw_04_16.data.model.PowerInstallation
import com.example.pw_04_16.data.model.UserLocation

/**
 * Режим сортування за номінальною потужністю.
 */
enum class SortOrder {
    /** За зростанням потужності */
    ASCENDING,

    /** За спаданням потужності */
    DESCENDING
}

/**
 * Стан користувацького інтерфейсу реєстру електроустановок та геолокації.
 *
 * @property allInstallations Повний початковий список об'єктів.
 * @property displayedInstallations Відфільтрований та відсортований список для відображення.
 * @property selectedType Обраний фільтр за типом (null — усе).
 * @property selectedStatus Обраний фільтр за статусом (null — усе).
 * @property sortOrder Поточний режим сортування за потужністю.
 * @property selectedId Ідентифікатор обраної установки (null — нічого не обрано).
 * @property userLocation Поточні координати користувача з GPS.
 * @property hasLocationPermission Наявність дозволів на геолокацію.
 * @property locationStatusMessage Текстове повідомлення про стан отримання координат.
 */
data class InstallationsUiState(
    val allInstallations: List<PowerInstallation> = emptyList(),
    val displayedInstallations: List<PowerInstallation> = emptyList(),
    val selectedType: InstallationType? = null,
    val selectedStatus: InstallationStatus? = null,
    val sortOrder: SortOrder = SortOrder.ASCENDING,
    val selectedId: Int? = null,
    val userLocation: UserLocation? = null,
    val hasLocationPermission: Boolean = false,
    val locationStatusMessage: String = "Очікування дозволу на геолокацію"
)
