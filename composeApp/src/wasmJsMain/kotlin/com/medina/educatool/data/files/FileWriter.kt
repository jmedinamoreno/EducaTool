package com.medina.educatool.data.files

import kotlinx.browser.document
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.events.Event
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

class WasmFileWriter: FileWriter {
    override suspend fun writeFile(fileName: String, content: String): Flow<FileSaveResult> = flow {
        try {
            val bytes:JsAny? = content.toJsString()
            val file = Blob(arrayOf(bytes).toJsArray(), BlobPropertyBag("application/xml"))
            val url = URL.createObjectURL(file)
            val anchor = document.createElement("a") as HTMLAnchorElement
            anchor.href = url
            anchor.download = fileName
            anchor.style.display = "none"
            document.body?.appendChild(anchor)
            anchor.click()
            document.body?.removeChild(anchor)
            URL.revokeObjectURL(url)
            emit(FileSaveResult.Success(fileName))
        } catch (exception: Exception) {
            emit(FileSaveResult.Error(exception))
        }
    }
}

actual fun getFileWriter(): FileWriter = WasmFileWriter()