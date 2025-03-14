package com.medina.educatool.data.ia.repository
import com.medina.educatool.data.ia.gemini.GeminiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface IARepository {
    fun generateContent(prompt: String): Flow<String?>
    fun generateStreamedContent(prompt: String): Flow<String?>
}

class IAGeminiRepository(
    private val geminiService: GeminiService
):IARepository{
    override fun generateContent(prompt: String): Flow<String?> = geminiService.generateContent(prompt)
    override fun generateStreamedContent(prompt: String): Flow<String?> = geminiService.generateStreamedContent(prompt)
}

class IADummyRepository:IARepository{
    override fun generateContent(prompt: String): Flow<String?> = flowOf(null)
    override fun generateStreamedContent(prompt: String): Flow<String?> = flowOf(null)
}