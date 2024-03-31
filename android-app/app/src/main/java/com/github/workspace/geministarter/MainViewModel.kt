package com.github.workspace.geministarter

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.apiKey
    )

    private val _uiState: MutableStateFlow<MainUiState> =
        MutableStateFlow(MainUiState.Initial())
    val uiState: StateFlow<MainUiState> =
        _uiState.asStateFlow()

    private var job: Job? = null

    fun guess(country: Country) {
        _uiState.value = MainUiState.Loading

        val prompt = """
            Italy: Rome
            Germany: Berlin
            France: Paris
            $country:   
        """.trimIndent()
        // you can reuse previous answer for country.

        job?.cancel()
        job = viewModelScope.launch {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                        /*
                        images.forEach { image ->
                            image(image)
                        }
                         */
                    }
                )
                response.text?.let { outputContent ->
                    _uiState.value = MainUiState.Success(country, outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = MainUiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    fun reset() {
        _uiState.value = MainUiState.Initial()
    }
}