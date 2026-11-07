# 🐾 PetCare – Routine & Reminder Log

## 1. Project Overview

PetCare is an Android application designed to help pet owners organize and manage their pets' daily care routines.

The application provides one place to manage pet profiles, create care tasks, track completed activities, and schedule reminders for upcoming tasks.

It aims to make pet care more organized and help owners keep track of their pets' routines.

## 2. Main Features

- **Pet Profiles:** Add, view, and delete pet profiles.
- **Care Tasks:** Create and manage care tasks for each pet.
- **Task Completion:** Mark care tasks as completed.
- **Activity History:** View completed activities with timestamps.
- **Reminders:** Schedule and delete reminders for upcoming care tasks.
- **Notifications:** Receive notifications for scheduled reminders.
- **Cat Facts API:** Retrieve random cat facts from an external API.
- **Loading and Error States:** Display loading indicators and error messages when retrieving API data.

## 3. Technologies Used

- **Kotlin:** Main programming language.
- **Android Studio:** Development environment.
- **Jetpack Compose:** User interface development.
- **Material 3:** UI components and styling.
- **Navigation Compose:** Navigation between application screens.
- **Room Database:** Local data persistence.
- **ViewModel:** Managing UI-related data and state.
- **Kotlin Coroutines and Flow:** Asynchronous operations and reactive data updates.
- **AlarmManager:** Scheduling reminder notifications.
- **BroadcastReceiver:** Handling scheduled reminder events.
- **HTTPURLConnection:** Communicating with the external API.
- **Git and GitHub:** Version control and collaboration.

## 4. API Integration

PetCare integrates the Cat Facts API:

https://catfact.ninja/fact

The application retrieves random cat facts and displays them to the user.

The API integration includes:
- Loading state while retrieving data.
- Displaying the retrieved cat fact.
- Error handling when the request fails.
- A button to retrieve another fact.

## 5. Local Database

The application uses Room to store data locally, including:

- Pet profiles.
- Care tasks.
- Activity history.
- Reminders.

This allows users to keep their information stored locally on their device.

## 6. Notifications

PetCare allows users to schedule reminders associated with care tasks.

The application uses Android notification functionality to notify users when a scheduled reminder is due.

## 7. Project Structure

The project follows an organized structure:

- `data/` – Room entities, DAOs, database, migrations, and repository.
- `api/` – External API communication.
- `ui/` – Application screens.
- `ui/theme/` – UI theme and Cat Facts screen.
- `PetViewModel.kt` – Pet and care-task data management.
- `CatFactViewModel.kt` – Cat Facts API state management.
- `ReminderScheduler.kt` – Reminder scheduling.
- `ReminderReceiver.kt` – Handling reminder notifications.
- `MainActivity.kt` – Main activity and application navigation.

## 8. Installation and Setup

1. Clone the GitHub repository.
2. Open the project in Android Studio.
3. Allow Gradle to synchronize the project.
4. Connect an Android device or start an emulator.
5. Run the application using Android Studio.

An internet connection is required for the Cat Facts API.

## 9. Team Project

PetCare was developed as a collaborative Android development project.

The project includes pet profile management, care-task management, local data persistence, activity history, reminders, notifications, and API integration.

GitHub was used to manage and share the source code.

## 10. Repository

GitHub: https://github.com/sarahhijazi/PetCare-Routine-Reminder-Log