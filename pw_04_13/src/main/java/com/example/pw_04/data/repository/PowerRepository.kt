package com.example.pw_04.data.repository

import com.example.pw_04.data.model.DataFailureException
import com.example.pw_04.data.model.LoadSnapshot
import com.example.pw_04.data.source.ForecastDataSource
import com.example.pw_04.data.source.SensorDataSource
import com.example.pw_04.data.source.SimulationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlin.random.Random

/**
 * Репозиторій для керування потоками даних моніторингу електроустановки.
 *
 * РІЗНИЦЯ МІЖ ХОЛОДНИМИ ТА ГАРЯЧИМИ ПОТОКАМИ:
 * - Холодний потік (Cold Flow): не виконує жодного коду і не генерує значення, поки на нього не з'явиться
 *   підписник (термінальний оператор, наприклад, collect). Кожен новий підписник запускає виконання корутини
 *   потоку заново з початку. Джерела SensorDataSource та ForecastDataSource віддають саме холодні потоки.
 * - Гарячий потік (Hot Flow / SharedFlow): працює незалежно від наявності активних підписників. Він генерує
 *   і поширює дані у власному CoroutineScope незалежно від того, чи є слухачі. Репозиторій перетворює холодні
 *   потоки джерел у гарячі SharedFlow за допомогою оператора shareIn.
 *
 * НАВІЩО ПОТРІБЕН replay ПРИ ПОВТОРНІЙ ПІДПИСЦІ:
 * Параметр `replay = 1` зберігає в буфері гарячого потоку одне останнє випромінене значення. Коли підписник
 * перепідписується на потік (наприклад, після автоматичного відновлення збору за допомогою retryWhen),
 * він одразу отримує це останнє збережене значення з буфера replay, не чекаючи наступної емісії від джерела.
 * Це запобігає зависанню та порожньому стану інтерфейсу під час відновлення підписки.
 */
class PowerRepository(
    scope: CoroutineScope
) {
    private val sensorDataSource = SensorDataSource()
    private val forecastDataSource = ForecastDataSource()

    // Перетворюємо холодний потік датчика на гарячий SharedFlow
    private val actualLoadHotStream: SharedFlow<Double> = sensorDataSource.getActualLoadStream()
        .shareIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            replay = 1
        )

    // Перетворюємо холодний потік прогнозу на гарячий SharedFlow
    private val forecastLoadHotStream: SharedFlow<Double> = forecastDataSource.getForecastLoadStream()
        .shareIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            replay = 1
        )

    /**
     * Повертає об'єднаний потік показань фактичного та прогнозованого навантаження.
     * Імітація збою виконується саме у цьому об'єднаному потоці за допомогою onEach,
     * а не у первинних джерелах. Якби виняток виник усередині потоку під shareIn,
     * гарячий потік завершився б помилкою назавжди і не зміг би відновлюватися.
     */
    fun observeLoad(): Flow<LoadSnapshot> {
        return combine(actualLoadHotStream, forecastLoadHotStream) { actual, forecast ->
            val deviation = actual - forecast
            LoadSnapshot(
                actualKw = actual,
                forecastKw = forecast,
                deviationKw = deviation
            )
        }.onEach {
            if (Random.nextDouble() < SimulationConfig.FAILURE_PROBABILITY) {
                throw DataFailureException("Збій каналу зв'язку під час отримання показань")
            }
        }
    }
}
