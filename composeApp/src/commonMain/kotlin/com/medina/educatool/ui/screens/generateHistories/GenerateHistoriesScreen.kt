package com.medina.educatool.ui.screens.generateHistories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medina.educatool.AppLogger
import com.medina.educatool.ui.screens.generateHistories.viewmodel.GenerateHistoriesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GenerateHistoriesScreen() {
    val viewModel = koinViewModel<GenerateHistoriesViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )
    Column {
        Button(onClick = {
            AppLogger.i("info", "some info")
            viewModel.generateStreamedContent("Escribe una historia sobre una niña con un paraguas rojo")
        }) {
            Text(
                text = "Generar historia"
            )
        }
        if(uiState.isLoading){
            Text(text = "No hay historias todavia")
        }else{
            LazyColumn {
                items(uiState.historyStream) { item ->
                    Text(text = item)
                }
            }
        }
    }
}