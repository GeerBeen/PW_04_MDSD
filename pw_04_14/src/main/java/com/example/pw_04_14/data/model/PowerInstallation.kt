package com.example.pw_04_14.data.model

/**
 * Незмінна модель даних промислової електроустановки.
 *
 * @property id Унікальний ідентифікатор установки.
 * @property name Назва електроустановки.
 * @property type Тип обладнання.
 * @property location Розташування на території підприємства.
 * @property description Опис призначення та параметрів.
 * @property latitude Географічна широта розташування.
 * @property longitude Географічна довгота розташування.
 * @property powerKw Номінальна потужність у кіловатах (кВт).
 */
data class PowerInstallation(
    val id: Int,
    val name: String,
    val type: String,
    val location: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val powerKw: Double
)
