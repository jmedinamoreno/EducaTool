package com.medina.educatool.ui.screens.moodleTool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medina.educatool.AppLogger
import com.medina.educatool.data.ia.gemini.model.JsonSchema
import com.medina.educatool.data.ia.model.IAJSONSchema
import com.medina.educatool.data.ia.repository.IAGeminiRepository
import com.medina.educatool.ui.screens.moodleTool.model.MoodleToolState
import com.medina.educatool.ui.screens.moodleTool.model.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

class MoodleToolViewModel(
    private val ia: IAGeminiRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MoodleToolState())
    val uiState: StateFlow<MoodleToolState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MoodleToolState()
    )

    private val questionList = mutableListOf<Question>()

    private var loadingMessage = "..."
    private var errorMessage = "..."
    private var successMessage = "..."

    fun configureLocaleTexts(loadingMessage: String, errorMessage: String, successMessage: String){
        this.loadingMessage = loadingMessage
        this.errorMessage = errorMessage
        this.successMessage = successMessage
    }

    fun parseQuestions(rawQuestions: String) {
        viewModelScope.launch { //Handles the screen loading
            _uiState.update { it.copy(message = Pair(loadingMessage, true)) }
            val prompt = "Given the following questions for a test exam: $rawQuestions \n"+
                    "Write a JSON file with the following schema: $questionJsonSchema"
            ia.generateJsonContent(prompt, questionJsonSchema)
                .map { content ->
                    AppLogger.i("received content", "content $content")
                    jsonDecoder.decodeFromString<List<Question>>(content ?: "")
                }
                .retry(3)
                .catch { exception ->
                    AppLogger.e("error parsing", "Exception $exception")
                    _uiState.update { it.copy(message = Pair("$errorMessage $exception", false)) }
                }
                .collect { questionList ->
                    questionList.forEach { question -> addQuestion(question) }
                    _uiState.update { it.copy(message = Pair(successMessage, false)) }
                }
        }
    }

    fun addQuestion(question: Question){
        questionList.add(question)
        _uiState.update { it.copy(questionList = questionList.toList()) }
    }

    fun deleteQuestion(question: Question){
        questionList.remove(question)
        _uiState.update { it.copy(questionList = questionList.toList()) }
    }

    fun modifyQuestion(old:Question, new:Question){
        val index = questionList.indexOf(old)
        questionList.remove(old)
        questionList.add(index, new)
        _uiState.update { it.copy(questionList = questionList.toList()) }
    }

    fun generateXml(){
        _uiState.update { it.copy(message = Pair("Esto aún no está implementado", false)) }
    }

    fun messageClosed(){
        _uiState.update { it.copy(message = null) }
    }

    private val questionJsonSchema = IAJSONSchema(
        root = JsonSchema(
            type = JsonSchema.Type.ARRAY,
            items = JsonSchema(
                type = JsonSchema.Type.OBJECT,
                properties = mapOf(
                    "summary" to JsonSchema(type = JsonSchema.Type.STRING, nullable = false),
                    "responses" to JsonSchema(
                        type = JsonSchema.Type.ARRAY,
                        nullable = false,
                        items = JsonSchema(
                            type = JsonSchema.Type.OBJECT,
                            properties = mapOf(
                                "text" to JsonSchema(type = JsonSchema.Type.STRING, nullable = false),
                                "correct" to JsonSchema(type = JsonSchema.Type.BOOLEAN, nullable = false)
                            )
                        )
                    )
                )
            )
        )
    )

    private val jsonDecoder = Json{
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }

}