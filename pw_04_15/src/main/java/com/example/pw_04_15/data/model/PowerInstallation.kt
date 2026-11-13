package com.example.pw_04_15.data.model

import java.time.LocalDate

/**
 * Незмінна модель даних промислової електроустановки.
 *
 * @property id Унікальний ідентифікатор установки.
 * @property name Назва електроустановки.
 * @property type Тип обладнання.
 * @property powerKw Номінальна потужність у кіловатах (кВт).
 * @property lastUpdate Дата останнього технічного обслуговування/оновлення.
 * @property status Поточний статус роботи.
 * @property location Розташування на території підприємства (цех, ділянка).
 */
data class PowerInstallation(
    val id: Int,
    val name: String,
    val type: InstallationType,
    val powerKw: Double,
    val lastUpdate: LocalDate,
    val status: InstallationStatus,
    val location: String
)
