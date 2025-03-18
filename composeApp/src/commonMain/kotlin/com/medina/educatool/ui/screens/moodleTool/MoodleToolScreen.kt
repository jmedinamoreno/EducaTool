package com.medina.educatool.ui.screens.moodleTool

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medina.educatool.ui.screens.moodleTool.model.Question
import com.medina.educatool.ui.screens.moodleTool.viewmodel.MoodleToolViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MoodleToolScreen() {
    val viewModel = koinViewModel<MoodleToolViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )
    Column {
        Text(text = "#MOODLE_TOOL")
        Row {
            Column(modifier = Modifier.weight(0.5f)){
                var rawQuestions by remember { mutableStateOf("") }
                MultiLineInput(
                    modifier = Modifier.weight(1f).border(BorderStroke(1.dp,Color.Black)),
                    value = rawQuestions,
                    onValueChange = { rawQuestions = it },
                )
                Button(onClick = {
                    viewModel.parseQuestions(rawQuestions)
                }) {
                    Text(
                        text = "#ENVIAR"
                    )
                }
            }
            Column(modifier = Modifier.weight(0.5f)) {
                if(uiState.isLoading){
                    LinearProgressIndicator(
                        color = Color.Blue
                    )
                }else{
                    if(uiState.error != null){
                        Text(
                            text = uiState.error!!,
                            color = Color.Red
                        )
                    }else {
                        QuestionList(uiState.questionList){
                            viewModel.deleteQuestion(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionList(
    questionList: List<Question> = emptyList(),
    onDeleted: (Question) -> Unit = {}
){
    LazyColumn {
        items(questionList) { item ->
            Row {
                Column {
                    Text(text = item.summary)
                    for (response in item.responses) {
                        Text(
                            text = response.text,
                            color = if (response.correct) Color.Green else Color.Red
                        )
                    }
                    Button(onClick = {
                        onDeleted(item)
                    }) {
                        Text(
                            text = "#ELIMINAR"
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MoodleToolQuestionListPreview() {
    QuestionList(
        questionList = listOf(
            Question(
                summary = "Question 1",
                responses = listOf(
                    Question.Response(
                        text = "Response 1",
                        correct = true
                    ),
                    Question.Response(
                        text = "Response 2",
                        correct = false
                    ),
                    Question.Response(
                        text = "Response 3",
                        correct = false
                    )
                )
            ),
            Question(
                summary = "Question 2",
                responses = listOf(
                    Question.Response(
                        text = "Response 1",
                        correct = false
                    ),
                    Question.Response(
                        text = "Response 2",
                        correct = false
                    ),
                    Question.Response(
                        text = "Response 3",
                        correct = true
                    )
                )
            )
        )
    )
}

@Composable
fun MultiLineInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            maxLines = Int.MAX_VALUE,
        )
    }
}
