package com.medina.educatool.ui.screens.moodleTool.model

import kotlinx.serialization.Serializable

data class MoodleToolState(
    val questionList: List<Question> = emptyList(),
    val xmlDataText: String? = null,
    val message: Pair<String,Boolean>? = null,
    val hasApiKey: Boolean = true
)

@Serializable
data class Question(
    val summary:String = "",
    val responses:List<Response> = emptyList()
){
    @Serializable
    data class Response(
        val text:String = "",
        val correct:Boolean = false
    )
}