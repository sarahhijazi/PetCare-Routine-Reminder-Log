
package com.example.petcare_routinereminderlog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petcare_routinereminderlog.data.Pet
import com.example.petcare_routinereminderlog.data.CareTask
import com.example.petcare_routinereminderlog.ui.ReminderScreen
import com.example.petcare_routinereminderlog.ui.theme.CatFactScreen
import kotlinx.coroutines.flow.flowOf

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            val petViewModel: PetViewModel = viewModel()

            // Request notification permission on Android 13+
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

            val pets by petViewModel.allPets
                .collectAsStateWithLifecycle()

            var selectedPet by remember {
                mutableStateOf<Pet?>(null)
            }

            var selectedCareTask by remember {
                mutableStateOf<CareTask?>(null)
            }

            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier
            ) {

                // HOME
                composable("home") {

                    PetCareHomeScreen(
                        pets = pets,

                        onAddPet = {
                            navController.navigate("addPet")
                        },

                        onDeletePet = { pet ->
                            petViewModel.deletePet(pet)
                        },

                        onSelectPet = { pet ->
                            selectedPet = pet
                            navController.navigate("tasks")
                        },

                        onOpenCatFacts = {
                            navController.navigate("catFacts")
                        }
                    )
                }

                // ADD PET
                composable("addPet") {

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

                                navController.popBackStack()
                            }
                        },

                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                // CARE TASKS
                composable("tasks") {

                    selectedPet?.let { pet ->

                        val taskFlow = remember(pet.id) {
                            petViewModel.getTasksForPet(pet.id)
                        }

                        val tasks by taskFlow.collectAsStateWithLifecycle(
                            initialValue = emptyList()
                        )

                        PetTasksScreen(
                            petName = pet.name,
                            tasks = tasks,

                            onAddTask = {
                                navController.navigate("addTask")
                            },

                            onCompleteTask = { task ->
                                petViewModel.completeTask(task)
                            },

                            onViewHistory = {
                                navController.navigate("history")
                            },

                            onManageReminders = { task ->
                                selectedCareTask = task
                                navController.navigate("reminders")
                            },

                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }

                // ADD CARE TASK
                composable("addTask") {

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

                                navController.popBackStack()
                            },

                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }

                // ACTIVITY HISTORY
                composable("history") {

                    selectedPet?.let { pet ->

                        val historyFlow = remember(pet.id) {
                            petViewModel.getActivityHistory(pet.id)
                        }

                        val historyLogs by historyFlow
                            .collectAsStateWithLifecycle(
                                initialValue = emptyList()
                            )

                        ActivityHistoryScreen(
                            petName = pet.name,
                            logs = historyLogs,

                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }

                // REMINDERS
                composable("reminders") {

                    selectedCareTask?.let { task ->

                        val reminderFlow = remember(task.id) {
                            petViewModel.getRemindersForTask(task.id)
                        }

                        val reminders by reminderFlow
                            .collectAsStateWithLifecycle(
                                initialValue = emptyList()
                            )

                        ReminderScreen(
                            taskTitle = task.title,
                            reminders = reminders,

                            onAddReminder = { dateTime ->

                                petViewModel.addReminder(
                                    careTaskId = task.id,
                                    reminderDateTime = dateTime
                                )

                                ReminderScheduler.scheduleReminder(
                                    context = this@MainActivity,
                                    careTaskId = task.id,
                                    taskTitle = task.title,
                                    reminderDateTime = dateTime
                                )
                            },

                            onDeleteReminder = { reminder ->

                                ReminderScheduler.cancelReminder(
                                    context = this@MainActivity,
                                    careTaskId = reminder.careTaskId,
                                    reminderDateTime = reminder.reminderDateTime
                                )

                                petViewModel.deleteReminder(reminder)
                            },

                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }

                // CAT FACTS
                composable("catFacts") {

                    CatFactScreen(
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetCareHomeScreen(
    pets: List<Pet>,
    onAddPet: () -> Unit,
    onDeletePet: (Pet) -> Unit,
    onSelectPet: (Pet) -> Unit,
    onOpenCatFacts: () -> Unit
) {
    androidx.compose.material3.Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("🐾 PetCare") }
            )
        }
    ) { padding ->

        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            item {
                Text(
                    text = "Your Pets, Your Care!",
                    style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onAddPet,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add a Pet")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onOpenCatFacts,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Discover Cat Facts 🐱")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "My Pets",
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                )
            }

            items(pets) { pet ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    onClick = { onSelectPet(pet) }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = pet.name,
                            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                        )

                        Text("Type: ${pet.type}")
                        Text("Age: ${pet.age}")

                        TextButton(
                            onClick = { onDeletePet(pet) }
                        ) {
                            Text("Delete Pet")
                        }
                    }
                }
            }
        }
    }
}