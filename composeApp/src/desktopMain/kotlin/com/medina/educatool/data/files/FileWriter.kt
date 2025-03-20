package com.medina.educatool.data.files

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FileOutputStream

class JVMFileWriter(): FileWriter {
    override suspend fun writeFile(fileName: String, content: String): Flow<FileSaveResult> = flow {
        val fileDialog = FileDialog(Frame(), "Save File", FileDialog.SAVE)
        fileDialog.file = fileName
        fileDialog.isVisible = true

        val directory = fileDialog.directory
        val file = fileDialog.file

        if (directory == null || file == null) {
            emit(FileSaveResult.Canceled)
        } else {
            try {
                val selectedFile = File(directory, file)
                FileOutputStream(selectedFile).use { output ->
                    output.write(content.toByteArray())
                }
                emit(FileSaveResult.Success(selectedFile.absolutePath))
            } catch (exception: Exception) {
                emit(FileSaveResult.Error(exception))
            }
        }
    }
}

actual fun getFileWriter(): FileWriter = JVMFileWriter()
