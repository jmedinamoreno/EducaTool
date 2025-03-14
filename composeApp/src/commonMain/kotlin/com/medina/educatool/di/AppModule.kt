package com.medina.educatool.di
import org.koin.dsl.module

val appModule = module {
    includes(commonModule)
}