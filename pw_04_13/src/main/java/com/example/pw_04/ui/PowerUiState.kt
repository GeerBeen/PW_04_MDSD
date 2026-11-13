package com.example.pw_04.ui

/**
 * Статус підключення та збору даних.
 */
enum class ConnectionStatus {
    /** Очікування перших даних */
    WAITING,

    /** Дані успішно надходять */
    RECEIVING,

    /** Стався збій, триває пауза та повторна спроба підключення */
    RETRYING,

    /** Фатальна помилка збору даних */
    FAILED
}

/**
 * Стан користувацького інтерфейсу моніторингу електроустановки.
 *
 * @property actualKw Фактичне навантаження в кВт (null до першого показання).
 * @property forecastKw Прогнозоване навантаження в кВт (null до першого показання).
 * @property deviationKw Відхилення фактичного навантаження від прогнозу в кВт (null до першого показання).
 * @property status Поточний стан підключення.
 * @property retryCount Лічильник відновлень (кількість повторних спроб після збоїв).
 * @property errorMessage Повідомлення про помилку у разі стану FAILED.
 */
data class PowerUiState(
    val actualKw: Double? = null,
    val forecastKw: Double? = null,
    val deviationKw: Double? = null,
    val status: ConnectionStatus = ConnectionStatus.WAITING,
    val retryCount: Int = 0,
    val errorMessage: String? = null
)
