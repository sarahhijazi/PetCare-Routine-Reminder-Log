
package com.example.petcare_routinereminderlog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare_routinereminderlog.api.CatFactApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CatFactUiState(
    val fact: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class CatFactViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CatFactUiState())
    val uiState: StateFlow<CatFactUiState> =
        _uiState.asStateFlow()

    fun fetchCatFact() {

        viewModelScope.launch {

            _uiState.value = CatFactUiState(
                isLoading = true
            )

            try {
                val fact = CatFactApi.getRandomFact()

                _uiState.value = CatFactUiState(
                    fact = fact,
                    isLoading = false
                )

            } catch (e: Exception) {

                _uiState.value = CatFactUiState(
                    isLoading = false,
                    error = "Unable to load cat facts. Please try again."
                )
            }
        }
    }
}

