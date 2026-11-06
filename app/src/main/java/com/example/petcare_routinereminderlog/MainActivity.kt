
package com.example.petcare_routinereminderlog

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petcare_routinereminderlog.data.Pet
import com.example.petcare_routinereminderlog.data.CareTask
import com.example.petcare_routinereminderlog.ui.ReminderScreen
import kotlinx.coroutines.flow.flowOf

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val petViewModel: PetViewModel = viewModel()

            // Request notification permission on Android 13 and above
            val notificationPermissionLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    // Permission result
                }

            LaunchedEffect(Unit) {
                if (
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    notificationPermissionLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }
            }

            // Observe all saved pets
            val pets by petViewModel.allPets
                .collectAsStateWithLifecycle()

            // Current screen
            var currentScreen by remember {
                mutableStateOf("home")
            }

            // Currently selected pet
            var selectedPet by remember {
                mutableStateOf<Pet?>(null)
            }

            // Currently selected care task
            var selectedCareTask by remember {
                mutableStateOf<CareTask?>(null)
            }

            val selectedPetId = selectedPet?.id

            // Observe care tasks for selected pet
            val taskFlow = remember(selectedPetId) {
                selectedPetId?.let {
                    petViewModel.getTasksForPet(it)
                } ?: flowOf(emptyList())
            }

            val tasks by taskFlow.collectAsStateWithLifecycle(
                initialValue = emptyList()
            )

            // Observe activity history for selected pet
            val historyFlow = remember(selectedPetId) {
                selectedPetId?.let {
                    petViewModel.getActivityHistory(it)
                } ?: flowOf(emptyList())
            }

            val historyLogs by historyFlow.collectAsStateWithLifecycle(
                initialValue = emptyList()
            )

            // Observe reminders for selected care task
            val selectedCareTaskId = selectedCareTask?.id

            val reminderFlow = remember(selectedCareTaskId) {
                selectedCareTaskId?.let {
                    petViewModel.getRemindersForTask(it)
                } ?: flowOf(emptyList())
            }

            val reminders by reminderFlow.collectAsStateWithLifecycle(
                initialValue = emptyList()
            )

            // Screen navigation
            when (currentScreen) {

                // HOME SCREEN
                "home" -> {
                    PetCareHomeScreen(
                        pets = pets,
                        onAddPet = {
                            currentScreen = "addPet"
                        },
                        onDeletePet = { pet ->
                            petViewModel.deletePet(pet)
                        },
                        onSelectPet = { pet ->
                            selectedPet = pet
                            currentScreen = "tasks"
                        }
                    )
                }

                // ADD PET SCREEN
                "addPet" -> {
                    AddPetScreen(
                        onSavePet = { name, type, age ->

                            val validAge = age.toIntOrNull()

                            if (
                                name.isNotBlank() &&
                                validAge != null &&
                                validAge >= 0
                            ) {
                                petViewModel.addPet(
                                    name = name.trim(),
                                    type = type,
                                    age = validAge
                                )

                                currentScreen = "home"
                            }
                        },
                        onBack = {
                            currentScreen = "home"
                        }
                    )
                }

                // CARE TASKS SCREEN
                "tasks" -> {
                    selectedPet?.let { pet ->

                        PetTasksScreen(
                            petName = pet.name,
                            tasks = tasks,

                            onAddTask = {
                                currentScreen = "addTask"
                            },

                            onCompleteTask = { task ->
                                petViewModel.completeTask(task)
                            },

                            onViewHistory = {
                                currentScreen = "history"
                            },

                            onManageReminders = { task ->
                                selectedCareTask = task
                                currentScreen = "reminders"
                            },

                            onBack = {
                                currentScreen = "home"
                            }
                        )
                    }
                }

                // ADD CARE TASK SCREEN
                "addTask" -> {
                    selectedPet?.let { pet ->

                        AddCareTaskScreen(
                            petName = pet.name,

                            onSaveTask = { title, category, frequency ->

                                petViewModel.addTask(
                                    petId = pet.id,
                                    title = title,
                                    category = category,
                                    frequency = frequency
                                )

                                currentScreen = "tasks"
                            },

                            onBack = {
                                currentScreen = "tasks"
                            }
                        )
                    }
                }

                // ACTIVITY HISTORY SCREEN
                "history" -> {
                    selectedPet?.let { pet ->

                        ActivityHistoryScreen(
                            petName = pet.name,
                            logs = historyLogs,
                            onBack = {
                                currentScreen = "tasks"
                            }
                        )
                    }
                }

                // REMINDER SCREEN
                "reminders" -> {
                    selectedCareTask?.let { task ->

                        ReminderScreen(
                            taskTitle = task.title,
                            reminders = reminders,

                            onAddReminder = { dateTime ->

                                // Save reminder in database
                                petViewModel.addReminder(
                                    careTaskId = task.id,
                                    reminderDateTime = dateTime
                                )

                                // Schedule notification
                                ReminderScheduler.scheduleReminder(
                                    context = this@MainActivity,
                                    careTaskId = task.id,
                                    taskTitle = task.title,
                                    reminderDateTime = dateTime
                                )
                            },

                            onDeleteReminder = { reminder ->

                                // Cancel scheduled notification
                                ReminderScheduler.cancelReminder(
                                    context = this@MainActivity,
                                    careTaskId = reminder.careTaskId,
                                    reminderDateTime = reminder.reminderDateTime
                                )

                                // Delete reminder from database
                                petViewModel.deleteReminder(reminder)
                            },

                            onBack = {
                                currentScreen = "tasks"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PetCareHomeScreen(
    pets: List<Pet>,
    onAddPet: () -> Unit,
    onDeletePet: (Pet) -> Unit,
    onSelectPet: (Pet) -> Unit
) {

    var petToDelete by remember {
        mutableStateOf<Pet?>(null)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {

            Text(
                text = "🐾 PetCare",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Your pet's daily care companion")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onAddPet,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add a Pet")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "My Pets",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (pets.isEmpty()) {

                Text("No pets added yet.")

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = pets,
                        key = { it.id }
                    ) { pet ->

                        Card(
                            onClick = {
                                onSelectPet(pet)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),

                                verticalAlignment =
                                    Alignment.CenterVertically,

                                horizontalArrangement =
                                    Arrangement.SpaceBetween
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = pet.name,
                                        style =
                                            MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "${pet.type} • ${pet.age} years"
                                    )

                                    Spacer(
                                        modifier = Modifier.height(4.dp)
                                    )

                                    Text(
                                        text = "Tap to view care tasks",
                                        style =
                                            MaterialTheme.typography.bodySmall,
                                        color =
                                            MaterialTheme.colorScheme.primary
                                    )
                                }

                                TextButton(
                                    onClick = {
                                        petToDelete = pet
                                    }
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    petToDelete?.let { pet ->

        AlertDialog(
            onDismissRequest = {
                petToDelete = null
            },

            title = {
                Text("Delete ${pet.name}?")
            },

            text = {
                Text(
                    "This will permanently delete this pet, " +
                            "their care tasks, and activity history."
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        onDeletePet(pet)
                        petToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        petToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
