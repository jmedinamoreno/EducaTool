package com.medina.educatool.data.storage

interface LocalStorage {
    suspend fun saveData(key: String, value: String)
    suspend fun retrieveData(key: String): String?
}

expect fun getLocalStorage(): LocalStorage