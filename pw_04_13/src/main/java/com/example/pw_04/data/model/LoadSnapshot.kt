package com.example.pw_04.data.model

/**
 * Незмінна модель даних, що описує знімок навантаження електроустановки.
 *
 * @property actualKw Фактичне навантаження з датчика (в кВт).
 * @property forecastKw Прогнозоване навантаження від моделі (в кВт).
 * @property deviationKw Відхилення фактичного навантаження від прогнозованого (в кВт).
 */
data class LoadSnapshot(
    val actualKw: Double,
    val forecastKw: Double,
    val deviationKw: Double
)
