
package com.example.petcare_routinereminderlog.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.petcare_routinereminderlog.data.Reminder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ReminderScreen(
    taskTitle: String,
    reminders: List<Reminder>,
    onAddReminder: (Long) -> Unit,
    onDeleteReminder: (Reminder) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var selectedDateTime by remember {
        mutableLongStateOf(System.currentTimeMillis())
    }

    val calendar = Calendar.getInstance().apply {
        timeInMillis = selectedDateTime
    }

    val dateFormat = SimpleDateFormat(
        "MMM dd, yyyy",
        Locale.getDefault()
    )

    val timeFormat = SimpleDateFormat(
        "hh:mm a",
        Locale.getDefault()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "⏰ Reminders",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Care task: $taskTitle",
            style = MaterialTheme.typography.titleMedium
        )

        Text("Choose a reminder date:")

        Button(
            onClick = {
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        val updatedCalendar = Calendar.getInstance().apply {
                            timeInMillis = selectedDateTime
                            set(Calendar.YEAR, year)
                            set(Calendar.MONTH, month)
                            set(Calendar.DAY_OF_MONTH, day)
                        }
                        selectedDateTime = updatedCalendar.timeInMillis
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(dateFormat.format(Date(selectedDateTime)))
        }

        Text("Choose a reminder time:")

        Button(
            onClick = {
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val updatedCalendar = Calendar.getInstance().apply {
                            timeInMillis = selectedDateTime
                            set(Calendar.HOUR_OF_DAY, hour)
                            set(Calendar.MINUTE, minute)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        selectedDateTime = updatedCalendar.timeInMillis
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(timeFormat.format(Date(selectedDateTime)))
        }

        Button(
            onClick = {
                onAddReminder(selectedDateTime)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Reminder")
        }

        HorizontalDivider()

        Text(
            text = "Your Reminders",
            style = MaterialTheme.typography.titleLarge
        )

        if (reminders.isEmpty()) {
            Text("No reminders created yet.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = reminders,
                    key = { it.id }
                ) { reminder ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                dateFormat.format(
                                    Date(reminder.reminderDateTime)
                                )
                            )

                            Text(
                                timeFormat.format(
                                    Date(reminder.reminderDateTime)
                                )
                            )

                            Text(
                                if (reminder.isActive) "Active" else "Inactive"
                            )

                            OutlinedButton(
                                onClick = {
                                    onDeleteReminder(reminder)
                                }
                            ) {
                                Text("Delete Reminder")
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
            Text("Back")
        }
    }
}
