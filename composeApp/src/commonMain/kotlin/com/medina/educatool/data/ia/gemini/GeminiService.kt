package com.medina.educatool.data.ia.gemini

import com.medina.educatool.data.ia.gemini.model.Content
import com.medina.educatool.data.ia.gemini.model.GenerateContentRequest
import com.medina.educatool.data.ia.gemini.model.Part
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GeminiService(
    private val geminiDataSource: GeminiDataSource
) {
    val modelName = "gemini-2.0-flash-lite"

    fun generateContent(prompt: String): Flow<String?> =
        geminiDataSource.generateContent(request = GenerateContentRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt)
                    )
                )
            )
        )).map {
            it?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        }.catch {
            emit("Error: ${it.message}")
        }

    fun generateStreamedContent(prompt: String): Flow<String?> =
        geminiDataSource.streamGenerateContent(request = GenerateContentRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt)
                    )
                )
            )
        )).map {
            it?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        }.catch {
            emit("Error: ${it.message}")
        }
}