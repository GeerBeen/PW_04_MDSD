package com.example.pw_04_14.ui

import com.example.pw_04_14.data.model.PowerInstallation

/**
 * Перелічення режимів сортування за потужністю.
 */
enum class SortOrder {
    /** За зростанням потужності */
    ASCENDING,

    /** За спаданням потужності */
    DESCENDING
}

/**
 * Стан користувацького інтерфейсу екрана електроустановок.
 *
 * @property installations Список електроустановок.
 * @property selectedId Ідентифікатор обраної електроустановки (null, якщо нічого не обрано).
 * @property sortOrder Поточний порядок сортування за потужністю.
 */
data class InstallationsUiState(
    val installations: List<PowerInstallation> = emptyList(),
    val selectedId: Int? = null,
    val sortOrder: SortOrder = SortOrder.ASCENDING
)
