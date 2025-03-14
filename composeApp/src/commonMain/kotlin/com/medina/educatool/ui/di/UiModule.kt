package com.medina.educatool.ui.di

import org.koin.dsl.module
import com.medina.educatool.ui.screens.generateHistories.viewmodel.GenerateHistoriesViewModel
import org.koin.core.module.dsl.viewModelOf

val uiModule = module {
    viewModelOf(::GenerateHistoriesViewModel)
}