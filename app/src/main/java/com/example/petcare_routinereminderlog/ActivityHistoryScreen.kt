
package com.example.petcare_routinereminderlog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petcare_routinereminderlog.data.ActivityLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityHistoryScreen(
    petName: String,
    logs: List<ActivityLog>,
    onBack: () -> Unit
) {
    val dateFormat = SimpleDateFormat(
        "dd MMM yyyy, hh:mm a",
        Locale.getDefault()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "🐾 $petName's Activity History",
            style = MaterialTheme.typography.headlineMedium
        )

        if (logs.isEmpty()) {
            Text("No completed activities yet.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                items(logs, key = { it.id }) { log ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "✅ ${log.taskTitle}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("Category: ${log.category}")

                            Text(
                                "Completed: ${
                                    dateFormat.format(
                                        Date(log.completedAt)
                                    )
                                }"
                            )
                        }
                    }
                }
            }
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Tasks")
        }
    }
}


