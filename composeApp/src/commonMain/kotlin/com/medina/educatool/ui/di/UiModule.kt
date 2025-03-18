package com.medina.educatool.ui.di

import org.koin.dsl.module
import com.medina.educatool.ui.screens.generateHistories.viewmodel.GenerateHistoriesViewModel
import com.medina.educatool.ui.screens.moodleTool.viewmodel.MoodleToolViewModel
import org.koin.core.module.dsl.viewModelOf

val uiModule = module {
    viewModelOf(::GenerateHistoriesViewModel)
    viewModelOf(::MoodleToolViewModel)
}