package com.medina.educatool.data.ia.gemini

import com.medina.educatool.data.ia.gemini.model.GenerateContentRequest
import com.medina.educatool.data.ia.gemini.model.GenerateContentResponse
import kotlinx.coroutines.flow.Flow

interface GeminiDataSource {
    fun generateContent(request: GenerateContentRequest): Flow<GenerateContentResponse?>
    fun streamGenerateContent(request: GenerateContentRequest): Flow<GenerateContentResponse?>
}