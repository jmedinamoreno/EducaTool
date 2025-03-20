package com.medina.educatool.data.files

import kotlinx.coroutines.flow.Flow

interface FileWriter{
    suspend fun writeFile(fileName: String, content: String): Flow<FileSaveResult>
}

sealed class FileSaveResult {
    data class Success(val path: String) : FileSaveResult()
    data class Error(val exception: Exception) : FileSaveResult()
    data object Canceled : FileSaveResult()
}

expect fun getFileWriter(): FileWriter