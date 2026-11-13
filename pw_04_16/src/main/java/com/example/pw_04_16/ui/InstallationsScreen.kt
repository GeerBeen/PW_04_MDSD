package com.example.pw_04_16.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pw_04_16.data.model.InstallationStatus
import com.example.pw_04_16.data.model.InstallationType
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstallationsScreen(
    viewModel: InstallationsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    // Лаунчер для запиту дозволів на геолокацію
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        viewModel.onLocationPermissionResult(granted)
    }

    // Перевірка дозволів під час початкового старту екрана
    LaunchedEffect(Unit) {
        val hasFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasFine || hasCoarse) {
            viewModel.onLocationPermissionResult(true)
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Автопрокрутка списку до обраної картки
    LaunchedEffect(state.selectedId) {
        val selId = state.selectedId
        if (selId != null) {
            val index = state.displayedInstallations.indexOfFirst { it.id == selId }
            if (index >= 0) {
                val visibleIndices = lazyListState.layoutInfo.visibleItemsInfo.map { it.index }
                if (index !in visibleIndices) {
                    lazyListState.animateScrollToItem(index)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Промислові електроустановки та GPS-моніторинг",
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
        ) {
            // Інформаційна панель статусу геолокації
            val locationText = if (state.userLocation != null) {
                String.format(
                    Locale.US,
                    "Мої координати: %.4f, %.4f",
                    state.userLocation!!.latitude,
                    state.userLocation!!.longitude
                )
            } else {
                state.locationStatusMessage
            }

            Text(
                text = locationText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = if (state.userLocation != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )

            // Фільтрація за типом
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = state.selectedType == null,
                        onClick = { viewModel.onTypeFilterSelected(null) },
                        label = { Text("Усі типи") }
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

            // Фільтрація за статусом + Кнопка сортування
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        FilterChip(
                            selected = state.selectedStatus == null,
                            onClick = { viewModel.onStatusFilterSelected(null) },
                            label = { Text("Усі") }
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

                val sortBtnText = when (state.sortOrder) {
                    SortOrder.ASCENDING -> "Потужність ↑"
                    SortOrder.DESCENDING -> "Потужність ↓"
                }
                Button(
                    onClick = viewModel::onSortOrderToggled,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(text = sortBtnText)
                }
            }

            // Мапа з вагою 3f, строго обмежена рамками контейнера за допомогою clipToBounds
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
                    .padding(vertical = 4.dp)
                    .clipToBounds()
            ) {
                InstallationsMap(
                    installations = state.displayedInstallations,
                    selectedId = state.selectedId,
                    userLocation = state.userLocation,
                    onInstallationSelected = viewModel::onInstallationSelected,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Список карток з вагою 2f, ізольований у власному контейнері
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(horizontal = 16.dp)
                    .clipToBounds()
            ) {
                if (state.displayedInstallations.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "За обраними фільтрами електроустановок не знайдено",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.displayedInstallations,
                            key = { it.id }
                        ) { installation ->
                            InstallationCard(
                                installation = installation,
                                isSelected = installation.id == state.selectedId,
                                onSelect = viewModel::onInstallationSelected
                            )
                        }
                    }
                }
            }
        }
    }
}
