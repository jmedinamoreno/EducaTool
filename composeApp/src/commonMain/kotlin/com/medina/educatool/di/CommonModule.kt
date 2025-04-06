package com.medina.educatool.di

import com.medina.educatool.File
import com.medina.educatool.Greeting
import com.medina.educatool.Storage
import com.medina.educatool.data.ia.di.artificialIntelligenceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonModule = module {
    singleOf(::Greeting)
    singleOf(::File)
    singleOf(::Storage)
    includes(artificialIntelligenceModule)
}