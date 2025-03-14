package com.medina.educatool.data.ia.di

import com.medina.educatool.data.ia.gemini.GeminiDataSource
import com.medina.educatool.data.ia.repository.IAGeminiRepository
import com.medina.educatool.data.ia.gemini.GeminiService
import com.medina.educatool.data.ia.gemini.remote.ktor.KtorGeminiDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val geminiModule = module {
    singleOf(::GeminiService)
}

val ktorModule = module {
    single<GeminiDataSource> { KtorGeminiDataSource() }
}

val artificialIntelligenceModule = module {
    singleOf(::IAGeminiRepository)
    includes(geminiModule, ktorModule)
}