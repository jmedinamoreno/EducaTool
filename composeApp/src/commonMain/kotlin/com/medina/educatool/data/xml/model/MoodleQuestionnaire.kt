package com.medina.educatool.data.xml.model

data class MoodleQuestionnaire(
    val questions:List<Question> = emptyList()
) {

    data class Question(
        val summary:String = "",
        val responses:List<Response> = emptyList()
    ){
        data class Response(
            val text:String = "",
            val correct:Boolean = false
        )
    }
}

