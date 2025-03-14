package com.medina.educatool.ui.screens.generateHistories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medina.educatool.AppLogger
import com.medina.educatool.data.ia.repository.IAGeminiRepository
import com.medina.educatool.ui.screens.generateHistories.model.GenerateHistoriesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GenerateHistoriesViewModel(
    private val ia: IAGeminiRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(GenerateHistoriesState())
    val uiState: StateFlow<GenerateHistoriesState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GenerateHistoriesState()
    )

    fun generateContent(prompt: String) {
        viewModelScope.launch { //Handles the screen loading
            _uiState.update { it.copy(isLoading = true) }
            ia.generateContent(prompt).collect { content ->
                AppLogger.i("received content", "content $content")
                _uiState.update { it.copy(history = content ?: "", isLoading = false) }
            }
        }
    }

    private val historyStream:MutableList<String> = MutableList(0){""}
    fun generateStreamedContent(prompt: String) {
        viewModelScope.launch { //Handles the screen loading
            _uiState.update { it.copy(isLoading = true) }
            historyStream.clear()
            ia.generateStreamedContent(prompt).collect { content ->
                AppLogger.i("received content", "content $content")
                if(content!=null) {
                    historyStream.add(content)
                    _uiState.update { it.copy(historyStream = historyStream, isLoading = false) }
                }
            }
        }
    }
}