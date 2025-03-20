package com.medina.educatool

import com.medina.educatool.data.files.getFileWriter

class File {
    private val fileWriter = getFileWriter()

    suspend fun saveFile(fileName: String, content: String) = fileWriter.writeFile(fileName, content)
}