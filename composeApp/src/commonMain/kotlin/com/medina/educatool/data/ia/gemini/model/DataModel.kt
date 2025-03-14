package com.medina.educatool.data.ia.gemini.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerateContentRequest(
    @SerialName("system_instruction")
    val systemInstruction: SystemInstruction?=null,
    val contents: List<Content>
)

@Serializable
data class SystemInstruction(
    val parts: List<Part>
)

@Serializable
data class Content(
    val role: String? = null,
    val parts: List<Part>
)

@Serializable
data class Part(
    val text: String
)

@Serializable
data class GenerateContentResponse(
    @SerialName("candidates") val candidates: List<Candidate>? = null,
    @SerialName("promptFeedback") val promptFeedback: PromptFeedback? = null
)

@Serializable
data class Candidate(
    @SerialName("content") val content: Content? = null,
    @SerialName("finishReason") val finishReason: String? = null,
    @SerialName("index") val index: Int? = null,
    @SerialName("safetyRatings") val safetyRatings: List<SafetyRating>? = null
)

@Serializable
data class PromptFeedback(
    @SerialName("safetyRatings") val safetyRatings: List<SafetyRating>? = null
)

@Serializable
data class SafetyRating(
    @SerialName("category") val category: String? = null,
    @SerialName("probability") val probability: String? = null
)