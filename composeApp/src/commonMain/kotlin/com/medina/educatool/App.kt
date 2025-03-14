package com.medina.educatool

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.medina.educatool.di.commonModule
import com.medina.educatool.ui.di.uiModule
import com.medina.educatool.ui.screens.generateHistories.GenerateHistoriesScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.core.logger.Level

@Composable
@Preview
fun App() {
    // Set current Koin instance to Compose context
    KoinApplication(
        application = {
            modules(commonModule,uiModule)
            printLogger(Level.DEBUG)
        }
    ) {
        MaterialTheme {
            GenerateHistoriesScreen()
        }
    }
}

