package com.example.pw_04_15.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pw_04_15.data.model.InstallationStatus
import com.example.pw_04_15.data.model.InstallationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstallationsScreen(
    viewModel: InstallationsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Промислові електроустановки: облік та пошук",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Поле пошуку за назвою
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Пошук за назвою") },
                singleLine = true,
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Text("✕", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            )

            // Рядок фільтрації за типом
            Text(
                text = "Фільтр за типом:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = state.selectedType == null,
                        onClick = { viewModel.onTypeFilterSelected(null) },
                        label = { Text("Усі") }
                    )
                }
                items(InstallationType.entries) { type ->
                    FilterChip(
                        selected = state.selectedType == type,
                        onClick = { viewModel.onTypeFilterSelected(type) },
                        label = { Text(type.displayName) }
                    )
                }
            }

            // Рядок фільтрації за статусом
            Text(
                text = "Фільтр за статусом:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = state.selectedStatus == null,
                        onClick = { viewModel.onStatusFilterSelected(null) },
                        label = { Text("Усі статуси") }
                    )
                }
                items(InstallationStatus.entries) { status ->
                    FilterChip(
                        selected = state.selectedStatus == status,
                        onClick = { viewModel.onStatusFilterSelected(status) },
                        label = { Text(status.displayName) }
                    )
                }
            }

            // Рядок сортування
            Text(
                text = "Сортування за:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(SortCriterion.entries) { criterion ->
                    FilterChip(
                        selected = state.sortCriterion == criterion,
                        onClick = { viewModel.onSortCriterionSelected(criterion) },
                        label = { Text(criterion.displayName) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Динамічний список з вагою 1f
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (state.displayedInstallations.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "За заданими критеріями електроустановок не знайдено",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.displayedInstallations,
                            key = { it.id }
                        ) { installation ->
                            InstallationCard(
                                installation = installation,
                                isSelected = installation.id == state.selectedInstallation?.id,
                                onSelect = viewModel::onInstallationSelected
                            )
                        }
                    }
                }
            }

            // Картка детальної інформації про обрану електроустановку
            state.selectedInstallation?.let { selected ->
                SelectedInstallationCard(
                    installation = selected,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
