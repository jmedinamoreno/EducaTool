package com.medina.educatool.ui.screens.moodleTool.model

import kotlinx.serialization.Serializable

data class MoodleToolState(
    val isLoading: Boolean = false,
    val questionList: List<Question> = emptyList(),
    val xmlDataText: String? = null,
    val error: String? = null
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