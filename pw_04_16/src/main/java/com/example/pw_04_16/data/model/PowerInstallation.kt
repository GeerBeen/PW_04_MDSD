package com.example.pw_04_16.data.model

/**
 * Незмінна модель даних промислової електроустановки.
 *
 * @property id Унікальний ідентифікатор установки.
 * @property name Назва електроустановки.
 * @property type Тип обладнання.
 * @property status Поточний статус роботи.
 * @property powerKw Номінальна потужність у кіловатах (кВт).
 * @property description Текстовий опис установки.
 * @property latitude Географічна широта.
 * @property longitude Географічна довгота.
 */
data class PowerInstallation(
    val id: Int,
    val name: String,
    val type: InstallationType,
    val status: InstallationStatus,
    val powerKw: Double,
    val description: String,
    val latitude: Double,
    val longitude: Double
)
