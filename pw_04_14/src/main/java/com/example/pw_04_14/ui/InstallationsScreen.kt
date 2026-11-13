package com.example.pw_04_14.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstallationsScreen(
    viewModel: InstallationsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(state.selectedId) {
        val selId = state.selectedId
        if (selId != null) {
            val index = state.installations.indexOfFirst { it.id == selId }
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
                        text = "Промислові електроустановки",
                        fontSize = 20.sp,
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
            // Мапа з вагою 2f
            InstallationsMap(
                installations = state.installations,
                selectedId = state.selectedId,
                onInstallationSelected = viewModel::onInstallationSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            )

            // Нижня частина з вагою 3f
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val buttonText = when (state.sortOrder) {
                        SortOrder.ASCENDING -> "Потужність: за зростанням"
                        SortOrder.DESCENDING -> "Потужність: за спаданням"
                    }
                    Button(
                        onClick = viewModel::onSortOrderToggled,
                        enabled = state.installations.isNotEmpty()
                    ) {
                        Text(text = buttonText)
                    }
                }

                if (state.installations.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Дані про електроустановки відсутні",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.installations,
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
