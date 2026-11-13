package com.example.pw_04.data.source

/**
 * Конфігурація параметрів симуляції роботи промислової електроустановки.
 */
object SimulationConfig {
    /** Інтервал опитування датчика фактичного навантаження (у мілісекундах) */
    const val SENSOR_INTERVAL_MS: Long = 1000L

    /** Інтервал отримання значень з прогнозової моделі (у мілісекундах) */
    const val FORECAST_INTERVAL_MS: Long = 1500L

    /** Мінімальне значення навантаження (у кВт) */
    const val MIN_KW: Double = 20.0

    /** Максимальне значення навантаження (у кВт) */
    const val MAX_KW: Double = 100.0

    /** Імовірність виникнення збою під час збору даних (0.2 = 20%) */
    const val FAILURE_PROBABILITY: Double = 0.2

    /** Пауза перед повторною спробою після збою (у мілісекундах) */
    const val RETRY_PAUSE_MS: Long = 2000L
}
