package com.medina.educatool.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.medina.educatool.ui.screens.moodleTool.model.Question

@Composable
fun QuestionList(
    modifier: Modifier = Modifier,
    questionList: List<Question> = emptyList(),
    onModified: (old:Question, new:Question) -> Unit = {_,_->},
    onDeleted: (Question) -> Unit = {}

){
    LazyColumn(modifier = modifier){
        items(questionList) { item ->
            Column{
                Row {
                    ParagraphInput(
                        modifier = Modifier.align(Alignment.CenterVertically).weight(1f),
                        value = item.summary,
                        onValueChange = {
                            onModified(item, item.copy(summary = it))
                        }
                    )
                    QuestionButton(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = "Borrar pregunta",
                        onClick = { onDeleted(item)}
                    )
                }
                for (response in item.responses) {
                    Row(modifier = Modifier.padding(end = 4.dp)) {
                        Checkbox(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            checked = response.correct,
                            onCheckedChange = {
                                onModified(
                                    item, item.copy(
                                        responses = item.responses.map {
                                            if (it == response) {
                                                it.copy(correct = it.correct.not())
                                            } else {
                                                it.copy(correct = it.correct)
                                            }
                                        }
                                    )
                                )
                            }
                        )
                        ParagraphInput(
                            modifier = Modifier.align(Alignment.CenterVertically).weight(1f),
                            value = response.text,
                            onValueChange = { value ->
                                onModified(
                                    item, item.copy(
                                        responses = item.responses.map {
                                            if (it == response) {
                                                it.copy(text = value)
                                            } else {
                                                it.copy()
                                            }
                                        }
                                    )
                                )
                            }
                        )
                        QuestionButton(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            text = "Borrar respuesta",
                            onClick = {
                                onModified(
                                    item, item.copy(
                                        responses = item.responses.filter { it != response }
                                    )
                                )
                            }
                        )
                    }
                }
                QuestionButton(
                    modifier = Modifier.align(Alignment.Start),
                    text = "+ Nueva respuesta",
                    onClick = {
                        onModified(
                            item, item.copy(
                                responses = item.responses.plus(Question.Response("Otra", false))
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun QuestionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = object : ButtonColors{
            @Composable
            override fun backgroundColor(enabled: Boolean): State<Color> {
                return rememberUpdatedState(Color.LightGray)
            }

            @Composable
            override fun contentColor(enabled: Boolean): State<Color> {
                return rememberUpdatedState(Color.Black)
            }
        },
        shape = CutCornerShape(4.dp),
    ) {
        Text(text = text)
    }
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

@Composable
fun ParagraphInput(
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