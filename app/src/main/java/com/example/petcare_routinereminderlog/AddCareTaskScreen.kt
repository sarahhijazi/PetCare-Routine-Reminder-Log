
package com.example.petcare_routinereminderlog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddCareTaskScreen(
    petName: String,
    onSaveTask: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Feeding") }
    var frequency by remember { mutableStateOf("Daily") }

    var categoryExpanded by remember {
        mutableStateOf(false)
    }

    var frequencyExpanded by remember {
        mutableStateOf(false)
    }

    val categories = listOf(
        "Feeding",
        "Grooming",
        "Flea Treatment",
        "Medication",
        "Other"
    )

    val frequencies = listOf(
        "Daily",
        "Weekly",
        "Monthly",
        "One-time"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "🐾 Add Care Task",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Pet: $petName",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Task Name") },
            placeholder = {
                Text("e.g. Morning Feeding")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Category")

        Box {
            OutlinedButton(
                onClick = { categoryExpanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$category ▼")
            }

            DropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = {
                    categoryExpanded = false
                }
            ) {
                categories.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            category = item
                            categoryExpanded = false
                        }
                    )
                }
            }
        }

        Text("Frequency")

        Box {
            OutlinedButton(
                onClick = { frequencyExpanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$frequency ▼")
            }

            DropdownMenu(
                expanded = frequencyExpanded,
                onDismissRequest = {
                    frequencyExpanded = false
                }
            ) {
                frequencies.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            frequency = item
                            frequencyExpanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                if (title.isNotBlank()) {
                    onSaveTask(
                        title.trim(),
                        category,
                        frequency
                    )
                }
            },
            enabled = title.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Task")
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}


