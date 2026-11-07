
package com.example.petcare_routinereminderlog.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petcare_routinereminderlog.CatFactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatFactScreen(
    onBack: () -> Unit,
    catFactViewModel: CatFactViewModel = viewModel()
) {

    val uiState by catFactViewModel.uiState
        .collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        catFactViewModel.fetchCatFact()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("🐱 Cat Facts")
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Discover something new about cats!",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            when {

                uiState.isLoading -> {

                    CircularProgressIndicator()

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Loading a cat fact...")
                }

                uiState.error != null -> {

                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                uiState.fact.isNotEmpty() -> {

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = uiState.fact,
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    catFactViewModel.fetchCatFact()
                },
                enabled = !uiState.isLoading
            ) {
                Text("Get Another Fact")
            }
        }
    }
}
