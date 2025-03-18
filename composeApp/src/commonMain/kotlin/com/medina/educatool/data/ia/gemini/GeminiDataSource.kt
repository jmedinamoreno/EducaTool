package com.medina.educatool.data.ia.gemini

import com.medina.educatool.data.ia.gemini.model.GenerateContentRequest
import com.medina.educatool.data.ia.gemini.model.GenerateContentResponse
import kotlinx.coroutines.flow.Flow

interface GeminiDataSource {
    fun generateContent(model: String, request: GenerateContentRequest): Flow<GenerateContentResponse?>
    fun streamGenerateContent(model: String, request: GenerateContentRequest): Flow<GenerateContentResponse?>
}