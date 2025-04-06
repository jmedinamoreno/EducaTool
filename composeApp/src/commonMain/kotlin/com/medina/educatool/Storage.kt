package com.medina.educatool

import com.medina.educatool.data.storage.getLocalStorage

class Storage {
    private val localStorage = getLocalStorage()

    suspend fun saveData(key: String, value: String) = localStorage.saveData(key, value)
    suspend fun retrieveData(key: String): String? = localStorage.retrieveData(key)
}