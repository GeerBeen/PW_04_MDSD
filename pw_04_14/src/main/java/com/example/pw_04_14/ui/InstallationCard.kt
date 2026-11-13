package com.example.pw_04_14.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pw_04_14.data.model.PowerInstallation
import java.util.Locale

@Composable
fun InstallationCard(
    installation: PowerInstallation,
    isSelected: Boolean,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColors = if (isSelected) {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    } else {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Card(
        colors = cardColors,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect(installation.id) }
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = installation.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Тип: ${installation.type}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Розташування: ${installation.location}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Опис: ${installation.description}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(6.dp))
            val powerText = if (installation.powerKw % 1.0 == 0.0) {
                String.format(Locale.US, "Потужність: %.0f кВт", installation.powerKw)
            } else {
                String.format(Locale.US, "Потужність: %.1f кВт", installation.powerKw)
            }
            Text(
                text = powerText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
