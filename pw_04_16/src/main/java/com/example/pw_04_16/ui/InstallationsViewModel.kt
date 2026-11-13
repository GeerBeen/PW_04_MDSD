package com.example.pw_04_16.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pw_04_16.data.local.LocationDataSource
import com.example.pw_04_16.data.model.InstallationStatus
import com.example.pw_04_16.data.model.InstallationType
import com.example.pw_04_16.data.repository.InstallationRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel для управління станом списку, мапи та геолокації користувача.
 */
class InstallationsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = InstallationRepository()
    private val locationDataSource = LocationDataSource(application)

    private val _uiState = MutableStateFlow(InstallationsUiState())
    val uiState: StateFlow<InstallationsUiState> = _uiState.asStateFlow()

    private var locationJob: Job? = null

    init {
        loadInstallations()
    }

    private fun loadInstallations() {
        val installations = repository.getInstallations()
        _uiState.update { currentState ->
            val updatedState = currentState.copy(allInstallations = installations)
            applyPipeline(updatedState)
        }
    }

    /**
     * Обробляє результат запиту дозволу на геолокацію.
     */
    fun onLocationPermissionResult(granted: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                hasLocationPermission = granted,
                locationStatusMessage = if (granted) {
                    "Визначення координат..."
                } else {
                    "Необхідно надати дозвіл на доступ до GPS для відображення позиції"
                }
            )
        }

        locationJob?.cancel()
        if (granted) {
            locationJob = viewModelScope.launch {
                locationDataSource.getLocationFlow().collect { userLocation ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            userLocation = userLocation,
                            locationStatusMessage = "Координати користувача виявлено"
                        )
                    }
                }
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(userLocation = null)
            }
        }
    }

    fun onTypeFilterSelected(type: InstallationType?) {
        _uiState.update { currentState ->
            val updatedState = currentState.copy(selectedType = type)
            applyPipeline(updatedState)
        }
    }

    fun onStatusFilterSelected(status: InstallationStatus?) {
        _uiState.update { currentState ->
            val updatedState = currentState.copy(selectedStatus = status)
            applyPipeline(updatedState)
        }
    }

    fun onSortOrderToggled() {
        _uiState.update { currentState ->
            val newOrder = if (currentState.sortOrder == SortOrder.ASCENDING) {
                SortOrder.DESCENDING
            } else {
                SortOrder.ASCENDING
            }
            val updatedState = currentState.copy(sortOrder = newOrder)
            applyPipeline(updatedState)
        }
    }

    fun onInstallationSelected(id: Int) {
        _uiState.update { currentState ->
            currentState.copy(selectedId = id)
        }
    }

    /**
     * Конвеєр обчислення списку displayedInstallations:
     * фільтрація за типом -> фільтрація за статусом -> сортування за powerKw.
     * Автоматично скидає selectedId в null, якщо обраний об'єкт не входить у новий відфільтрований список.
     */
    private fun applyPipeline(state: InstallationsUiState): InstallationsUiState {
        var list = state.allInstallations

        if (state.selectedType != null) {
            list = list.filter { it.type == state.selectedType }
        }

        if (state.selectedStatus != null) {
            list = list.filter { it.status == state.selectedStatus }
        }

        list = when (state.sortOrder) {
            SortOrder.ASCENDING -> list.sortedBy { it.powerKw }
            SortOrder.DESCENDING -> list.sortedByDescending { it.powerKw }
        }

        val updatedSelectedId = if (state.selectedId != null) {
            if (list.any { it.id == state.selectedId }) {
                state.selectedId
            } else {
                null
            }
        } else {
            null
        }

        return state.copy(
            displayedInstallations = list,
            selectedId = updatedSelectedId
        )
    }
}
