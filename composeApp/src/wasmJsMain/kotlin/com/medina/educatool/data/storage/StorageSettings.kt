package com.medina.educatool.data.storage

import kotlinx.browser.localStorage
import org.w3c.dom.Storage
import org.w3c.dom.set

class WasmLocalStorage(): LocalStorage{
    val storage: Storage = localStorage

    override suspend fun saveData(key: String, value: String) {
        storage[key] = value
    }

    override suspend fun retrieveData(key: String): String? {
        return storage.getItem(key)
    }
}

actual fun getLocalStorage(): LocalStorage = WasmLocalStorage()