package com.medina.educatool.ui.screens.generateHistories.model

data class GenerateHistoriesState(
    val isLoading: Boolean = true,
    val history: String = "",
    val historyStream: List<String> = emptyList(),
    val error: String? = null
)