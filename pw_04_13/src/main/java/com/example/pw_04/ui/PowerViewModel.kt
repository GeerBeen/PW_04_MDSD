package com.example.pw_04.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pw_04.data.model.DataFailureException
import com.example.pw_04.data.repository.PowerRepository
import com.example.pw_04.data.source.SimulationConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel для моніторингу електроустановки.
 * Керує підпискою на потік даних з репозиторію та оновлює UI-стан.
 */
class PowerViewModel : ViewModel() {

    private val repository = PowerRepository(viewModelScope)

    private val _uiState = MutableStateFlow(PowerUiState())
    val uiState: StateFlow<PowerUiState> = _uiState.asStateFlow()

    init {
        observePowerData()
    }

    private fun observePowerData() {
        viewModelScope.launch {
            repository.observeLoad()
                .retryWhen { cause, _ ->
                    if (cause is DataFailureException) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                status = ConnectionStatus.RETRYING,
                                retryCount = currentState.retryCount + 1
                            )
                        }
                        delay(SimulationConfig.RETRY_PAUSE_MS)
                        true
                    } else {
                        false
                    }
                }
                .catch { cause ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            status = ConnectionStatus.FAILED,
                            errorMessage = cause.message ?: "Невідома помилка збору даних"
                        )
                    }
                }
                .collect { snapshot ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            actualKw = snapshot.actualKw,
                            forecastKw = snapshot.forecastKw,
                            deviationKw = snapshot.deviationKw,
                            status = ConnectionStatus.RECEIVING
                        )
                    }
                }
        }
    }
}
