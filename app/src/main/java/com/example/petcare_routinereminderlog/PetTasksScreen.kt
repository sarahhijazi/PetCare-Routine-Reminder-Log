
package com.example.petcare_routinereminderlog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petcare_routinereminderlog.data.CareTask

@Composable
fun PetTasksScreen(
    petName: String,
    tasks: List<CareTask>,
    onAddTask: () -> Unit,
    onCompleteTask: (CareTask) -> Unit,
    onViewHistory: () -> Unit,
    onManageReminders: (CareTask) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "🐾 $petName's Care Tasks",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(
            onClick = onAddTask,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Add Care Task")
        }

        OutlinedButton(
            onClick = onViewHistory,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📋 View Activity History")
        }

        if (tasks.isEmpty()) {
            Text("No care tasks yet. Add your first task!")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = tasks,
                    key = { it.id }
                ) { task ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("Category: ${task.category}")
                            Text("Frequency: ${task.frequency}")

                            Text(
                                text = if (task.isCompleted)
                                    "Status: Completed ✅"
                                else
                                    "Status: Pending ⏳"
                            )

                            OutlinedButton(
                                onClick = {
                                    onManageReminders(task)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("⏰ Manage Reminders")
                            }

                            Button(
                                onClick = {
                                    onCompleteTask(task)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    if (task.isCompleted)
                                        "Mark as Pending"
                                    else
                                        "Mark as Completed"
                                )
                            }
                        }
                    }
                }
            }
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Pets")
        }
    }
}
