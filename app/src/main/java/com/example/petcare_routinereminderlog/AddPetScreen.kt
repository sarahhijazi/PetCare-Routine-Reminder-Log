
package com.example.petcare_routinereminderlog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddPetScreen(
    onSavePet: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    var petName by remember { mutableStateOf("") }
    var petType by remember { mutableStateOf("Dog") }
    var petAge by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val petTypes = listOf(
        "Dog", "Cat", "Bird", "Rabbit", "Other"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "🐾 Add a New Pet",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = petName,
            onValueChange = { petName = it },
            label = { Text("Pet Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Pet Type",
            style = MaterialTheme.typography.titleMedium
        )

        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$petType ▼")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                petTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            petType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = petAge,
            onValueChange = { input ->
                if (input.all { it.isDigit() }) {
                    petAge = input
                }
            },
            label = { Text("Pet Age (years)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (petName.isNotBlank() && petAge.isNotBlank()) {
                    onSavePet(
                        petName.trim(),
                        petType,
                        petAge
                    )
                }
            },
            enabled = petName.isNotBlank() &&
                    petAge.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Pet")
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
