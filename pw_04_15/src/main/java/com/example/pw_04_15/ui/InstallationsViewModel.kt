package com.example.pw_04_15.ui

import androidx.lifecycle.ViewModel
import com.example.pw_04_15.data.model.InstallationStatus
import com.example.pw_04_15.data.model.InstallationType
import com.example.pw_04_15.data.model.PowerInstallation
import com.example.pw_04_15.data.repository.InstallationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel для управління станом реєстру електроустановок,
 * виконанням багаторівневої фільтрації, пошуку, сортування та вибору об'єкта.
 */
class InstallationsViewModel : ViewModel() {

    private val repository = InstallationRepository()

    private val _uiState = MutableStateFlow(InstallationsUiState())
    val uiState: StateFlow<InstallationsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val installations = repository.getInstallations()
        _uiState.update { currentState ->
            val updatedState = currentState.copy(allInstallations = installations)
            applyPipeline(updatedState)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            val updatedState = currentState.copy(searchQuery = query)
            applyPipeline(updatedState)
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

    fun onSortCriterionSelected(criterion: SortCriterion) {
        _uiState.update { currentState ->
            val updatedState = currentState.copy(sortCriterion = criterion)
            applyPipeline(updatedState)
        }
    }

    fun onInstallationSelected(installation: PowerInstallation) {
        _uiState.update { currentState ->
            val newSelection = if (currentState.selectedInstallation?.id == installation.id) {
                null
            } else {
                installation
            }
            currentState.copy(selectedInstallation = newSelection)
        }
    }

    /**
     * Єдина чиста обчислювальна функція для розрахунку відфільтрованого й відсортованого списку.
     * Послідовність: фільтрація за типом -> фільтрація за статусом -> пошук за назвою -> сортування.
     * Також автоматично скидає selectedInstallation в null, якщо він зникає з відфільтрованого списку.
     */
    private fun applyPipeline(state: InstallationsUiState): InstallationsUiState {
        var list = state.allInstallations

        // 1. Фільтрація за типом
        if (state.selectedType != null) {
            list = list.filter { it.type == state.selectedType }
        }

        // 2. Фільтрація за статусом
        if (state.selectedStatus != null) {
            list = list.filter { it.status == state.selectedStatus }
        }

        // 3. Пошук за назвою без урахування регістру (із trim)
        val query = state.searchQuery.trim()
        if (query.isNotEmpty()) {
            list = list.filter { it.name.contains(query, ignoreCase = true) }
        }

        // 4. Сортування за обраним критерієм
        list = when (state.sortCriterion) {
            SortCriterion.NAME -> list.sortedBy { it.name }
            SortCriterion.TYPE -> list.sortedBy { it.type.displayName }
            SortCriterion.POWER -> list.sortedByDescending { it.powerKw }
            SortCriterion.DATE -> list.sortedByDescending { it.lastUpdate }
        }

        // 5. Перевірка збереження обраного елемента
        val updatedSelection = if (state.selectedInstallation != null) {
            if (list.any { it.id == state.selectedInstallation.id }) {
                state.selectedInstallation
            } else {
                null
            }
        } else {
            null
        }

        return state.copy(
            displayedInstallations = list,
            selectedInstallation = updatedSelection
        )
    }
}
