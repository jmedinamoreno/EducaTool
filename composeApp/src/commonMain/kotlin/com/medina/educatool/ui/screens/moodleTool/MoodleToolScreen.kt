package com.medina.educatool.ui.screens.moodleTool

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medina.educatool.ui.components.MultiLineInput
import com.medina.educatool.ui.components.QuestionButton
import com.medina.educatool.ui.components.QuestionList
import com.medina.educatool.ui.screens.moodleTool.model.Question
import com.medina.educatool.ui.screens.moodleTool.viewmodel.MoodleToolViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MoodleToolScreen() {
    val viewModel = koinViewModel<MoodleToolViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )
    viewModel.configureLocaleTexts(
        loadingMessage = "Cargando...",
        errorMessage = "Error",
        successMessage = "Listo",
    )
    Column{
        Text(text = "Educa Tool")
        MoodleToolPanel(
            questionList = uiState.questionList,
            onGenerateXML = viewModel::generateXml,
            onAddQuestion = viewModel::addQuestion,
            onModifyQuestion = viewModel::modifyQuestion,
            onDeleteQuestion = viewModel::deleteQuestion,
            parseQuestions = viewModel::parseQuestions,
            configureApiKey = viewModel::configureApiKey,
            message = uiState.message,
            onMessageClosed = viewModel::messageClosed,
            hasApiKey = uiState.hasApiKey
        )
    }
}

@Composable
fun MoodleToolPanel(
    questionList: List<Question> = emptyList(),
    onGenerateXML: () -> Unit = {},
    onAddQuestion: (Question) -> Unit = {},
    onModifyQuestion: (old:Question, new:Question) -> Unit = {_,_->},
    onDeleteQuestion: (Question) -> Unit = {},
    parseQuestions: (rawText: String) -> Unit = {},
    configureApiKey: (newKey: String) -> Unit = {},
    message: Pair<String,Boolean>? = null,
    onMessageClosed: () -> Unit = {},
    hasApiKey: Boolean? = null,
) {
    var showDialogImportQuestions by remember { mutableStateOf(false) }
    var showDialogConfigureApiKey by remember { mutableStateOf(false) }
    if(message != null) {
        Dialog(
            onDismissRequest = onMessageClosed
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(10.dp)
                ,
            ) {
                Text(text = message.first)
                if (message.second) {
                    LinearProgressIndicator()
                }
                QuestionButton(
                    text = "Ok",
                    onClick = onMessageClosed
                )
            }
        }
    }
    if(showDialogImportQuestions) {
        if(showDialogConfigureApiKey) {
            Dialog(
                onDismissRequest = {
                    showDialogConfigureApiKey = false
                },
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxHeight(0.8f)
                        .padding(10.dp)
                    ,
                ) {
                    var newApiKey by remember { mutableStateOf("") }
                    Text(text = "Para usar funciones de IA es necesario configurar tu API KEY de gemini:")
                    MultiLineInput(
                        modifier = Modifier.border(BorderStroke(1.dp, Color.Black)),
                        value = newApiKey,
                        onValueChange = { newApiKey = it },
                    )
                    Row {
                        QuestionButton(
                            text = "Establecer ApiKey",
                            onClick = {
                                configureApiKey(newApiKey)
                                showDialogConfigureApiKey = false
                            }
                        )
                        Box(modifier = Modifier.weight(1f))
                        QuestionButton(
                            text = "Cancelar",
                            onClick = {
                                showDialogConfigureApiKey = false
                            }
                        )
                    }
                }
            }
        }else {
            Dialog(
                onDismissRequest = {
                    showDialogImportQuestions = false
                },
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(10.dp),
                ) {
                    var rawQuestions by remember { mutableStateOf("") }
                    Text(text = "Pega tu texto aqui:")
                    MultiLineInput(
                        modifier = Modifier.border(BorderStroke(1.dp, Color.Black))
                            .fillMaxHeight(0.8f),
                        value = rawQuestions,
                        onValueChange = { rawQuestions = it },
                    )
                    Row {
                        QuestionButton(
                            text = "Enviar",
                            onClick = {
                                parseQuestions(rawQuestions)
                                showDialogImportQuestions = false
                            }
                        )
                        Box(modifier = Modifier.weight(1f))
                        QuestionButton(
                            text = "Cancelar",
                            onClick = {
                                showDialogImportQuestions = false
                            }
                        )
                    }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxHeight()) {
        Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
            QuestionList(
                modifier = Modifier.weight(1f, fill = false),
                questionList = questionList,
                onModified = onModifyQuestion,
                onDeleted = onDeleteQuestion
            )
            QuestionButton(
                modifier = Modifier.align(Alignment.End),
                text = "+ Nueva pregunta",
                onClick = {
                    onAddQuestion(
                        Question(
                            summary = "Nueva pregunta",
                            listOf(
                                Question.Response("Respuesta correcta", true),
                                Question.Response("Respuesta incorrecta", false)
                            )
                        )
                    )
                }
            )
        }
        QuestionButton(
            modifier = Modifier.align(Alignment.End),
            text = "Importar preguntas",
            onClick = {
                showDialogImportQuestions = true
                if(hasApiKey != true) {
                    showDialogConfigureApiKey = true
                }
            }
        )
        QuestionButton(
            modifier = Modifier.align(Alignment.End),
            text = "Generar XML",
            onClick = onGenerateXML
        )
    }
}
