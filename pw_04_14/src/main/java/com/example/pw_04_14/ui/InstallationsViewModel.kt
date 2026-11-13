package com.example.pw_04_14.ui

import androidx.lifecycle.ViewModel
import com.example.pw_04_14.data.model.PowerInstallation
import com.example.pw_04_14.data.repository.InstallationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel для управління станом списку та мапи електроустановок.
 */
class InstallationsViewModel : ViewModel() {

    private val repository = InstallationRepository()

    private val _uiState = MutableStateFlow(InstallationsUiState())
    val uiState: StateFlow<InstallationsUiState> = _uiState.asStateFlow()

    init {
        loadInstallations()
    }

    private fun loadInstallations() {
        val rawList = repository.getInstallations()
        val sortedList = sortInstallations(rawList, _uiState.value.sortOrder)
        _uiState.update { currentState ->
            currentState.copy(installations = sortedList)
        }
    }

    /**
     * Оновлює ідентифікатор обраної електроустановки.
     * Повторне натискання залишає вибір без змін.
     */
    fun onInstallationSelected(id: Int) {
        _uiState.update { currentState ->
            currentState.copy(selectedId = id)
        }
    }

    /**
     * Перемикає режим сортування між зростанням і спаданням потужності
     * та перевпорядковує список. Обраний елемент зберігається.
     */
    fun onSortOrderToggled() {
        _uiState.update { currentState ->
            val newOrder = if (currentState.sortOrder == SortOrder.ASCENDING) {
                SortOrder.DESCENDING
            } else {
                SortOrder.ASCENDING
            }
            val sortedList = sortInstallations(currentState.installations, newOrder)
            currentState.copy(
                installations = sortedList,
                sortOrder = newOrder
            )
        }
    }

    private fun sortInstallations(
        list: List<PowerInstallation>,
        order: SortOrder
    ): List<PowerInstallation> {
        return when (order) {
            SortOrder.ASCENDING -> list.sortedBy { it.powerKw }
            SortOrder.DESCENDING -> list.sortedByDescending { it.powerKw }
        }
    }
}
