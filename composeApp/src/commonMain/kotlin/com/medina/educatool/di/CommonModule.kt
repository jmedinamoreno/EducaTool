package com.medina.educatool.di

import com.medina.educatool.Greeting
import com.medina.educatool.data.ia.di.artificialIntelligenceModule
import com.medina.educatool.data.ia.di.geminiModule
import com.medina.educatool.data.ia.di.ktorModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonModule = module {
    singleOf(::Greeting)
    includes(artificialIntelligenceModule)
}