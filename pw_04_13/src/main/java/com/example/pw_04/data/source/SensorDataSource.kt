package com.example.pw_04.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.random.Random

/**
 * Джерело даних, що симулює роботу фізичного датчика фактичного навантаження.
 * Надає холодний Flow значень у кВт.
 */
class SensorDataSource {
    /**
     * Повертає холодний потік фактичних показань навантаження.
     * Обчислення та емісія відбуваються у фоновому потоці Dispatchers.Default.
     */
    fun getActualLoadStream(): Flow<Double> {
        return flow {
            while (true) {
                delay(SimulationConfig.SENSOR_INTERVAL_MS)
                val value = Random.nextDouble(SimulationConfig.MIN_KW, SimulationConfig.MAX_KW)
                emit(value)
            }
        }.flowOn(Dispatchers.Default)
    }
}
