package com.medina.educatool.data.ia.repository
import com.medina.educatool.data.ia.gemini.GeminiService
import com.medina.educatool.data.ia.model.IAJSONSchema
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface IARepository {
    fun configureApiKey(apiKey: String)
    fun generateContent(prompt: String): Flow<String?>
    fun generateStreamedContent(prompt: String): Flow<String?>
    fun generateJsonContent(prompt: String, schema: IAJSONSchema): Flow<String?>
}

class IAGeminiRepository(
    private val geminiService: GeminiService
):IARepository{
    override fun configureApiKey(apiKey: String) = geminiService.configureApiKey(apiKey)
    override fun generateContent(prompt: String): Flow<String?> = geminiService.generateContent(prompt)
    override fun generateJsonContent(prompt: String, schema: IAJSONSchema): Flow<String?> = geminiService.generateJsonContent(prompt,schema)
    override fun generateStreamedContent(prompt: String): Flow<String?> = geminiService.generateStreamedContent(prompt)
}

class IADummyRepository:IARepository{
    override fun configureApiKey(apiKey: String){}
    override fun generateContent(prompt: String): Flow<String?> = flowOf(null)
    override fun generateStreamedContent(prompt: String): Flow<String?> = flowOf(null)
    override fun generateJsonContent(prompt: String, schema: IAJSONSchema): Flow<String?> = flowOf(null)
}