package com.medina.educatool.data.ia.gemini

import com.medina.educatool.data.ia.gemini.model.Content
import com.medina.educatool.data.ia.gemini.model.GenerateContentRequest
import com.medina.educatool.data.ia.gemini.model.GenerationConfig
import com.medina.educatool.data.ia.gemini.model.Part
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import com.medina.educatool.data.ia.model.IAJSONSchema

class GeminiService(
    private val geminiDataSource: GeminiDataSource
) {
    val modelFlashLite = "gemini-2.0-flash-lite"
    val modelFlash = "gemini-2.0-flash"

    fun generateContent(prompt: String): Flow<String?> =
        geminiDataSource.generateContent(
            model = modelFlashLite
            , request = GenerateContentRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(text = prompt)
                        )
                    )
                )
            )
        ).map {
            it?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        }.catch {
            emit("Error: ${it.message}")
        }

    fun generateJsonContent(prompt: String, schema: IAJSONSchema): Flow<String?> =
        geminiDataSource.generateContent(
            model = modelFlash
            , request = GenerateContentRequest(
                contents = listOf(
                    Content(
                        parts =
                            prompt.split("\n").map {
                                Part(text = it)
                            }
                    )
                ),
                generationConfig = GenerationConfig(
                    responseMimeType = "application/json",
                    responseSchema = schema.root,
                    temperature = 0.5
                )
            )
        ).map {
            it?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        }.catch {
            emit("Error: ${it.message}")
        }

    fun generateStreamedContent(prompt: String): Flow<String?> =
        geminiDataSource.streamGenerateContent(
            model = modelFlashLite
            , request = GenerateContentRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(text = prompt)
                        )
                    )
                )
            )
        ).map {
            it?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        }.catch {
            emit("Error: ${it.message}")
        }
}