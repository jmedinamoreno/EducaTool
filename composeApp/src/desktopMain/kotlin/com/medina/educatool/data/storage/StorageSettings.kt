package com.medina.educatool.data.storage

import java.util.prefs.Preferences

class JVMLocalStorage: LocalStorage {
    private val prefs: Preferences = Preferences.userNodeForPackage(LocalStorage::class.java)
    override suspend fun saveData(key: String, value: String) {
        prefs.put(key, value)
    }

    override suspend fun retrieveData(key: String): String? {
        return prefs.get(key, null)
    }
}
actual fun getLocalStorage(): LocalStorage = JVMLocalStorage()