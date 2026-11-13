package com.example.pw_04_15.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pw_04_15.data.model.PowerInstallation
import java.util.Locale

@Composable
fun SelectedInstallationCard(
    installation: PowerInstallation,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Вибрана установка: ${installation.name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Тип обладнання: ${installation.type.displayName}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = String.format(Locale.US, "Номінальна потужність: %.1f кВт", installation.powerKw),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Статус роботи: ${installation.status.displayName}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Розташування: ${installation.location}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Останнє оновлення: ${installation.lastUpdate}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
