package com.medina.educatool.data.ia.gemini.remote.ktor

import com.medina.educatool.AppLogger
import com.medina.educatool.data.ia.gemini.GeminiDataSource
import com.medina.educatool.data.ia.gemini.model.GenerateContentRequest
import com.medina.educatool.data.ia.gemini.model.GenerateContentResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.cancel
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.io.readString
import kotlinx.serialization.json.Json

class KtorGeminiDataSource(
): GeminiDataSource {

    private var dispatcher: CoroutineDispatcher = Dispatchers.Default
    private val jsonDecoder = Json{
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }
    private val httpClient = HttpClient{
        install(ContentNegotiation) {
            json(jsonDecoder)
        }
        install(HttpTimeout){
            requestTimeoutMillis = 180000
        }
        install(Logging) {
            logger = object : io.ktor.client.plugins.logging.Logger{
                override fun log(message: String) {
                    AppLogger.i("Network", message)
                }
            }
            level = LogLevel.ALL
        }
    }
    private var apiKey = ""
    override fun configureApiKey(apiKey: String) {
        this.apiKey = apiKey
    }
    override fun generateContent(model: String, request: GenerateContentRequest): Flow<GenerateContentResponse?> = flow<GenerateContentResponse?> {
        val response = httpClient.post {
            url {
                protocol = URLProtocol.HTTPS
                host = "generativelanguage.googleapis.com"
                appendPathSegments(
                    "v1beta/models",
                    "$model:generateContent"
                )
                parameters.append("key", apiKey)
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body() as GenerateContentResponse
        emit(response)
    }.flowOn(dispatcher)

    override fun streamGenerateContent(model: String, request: GenerateContentRequest): Flow<GenerateContentResponse?> = flow<GenerateContentResponse?> {
        val response: HttpResponse = httpClient.post{
            url {
                protocol = URLProtocol.HTTPS
                host = "generativelanguage.googleapis.com"
                appendPathSegments(
                    "v1beta/models",
                    "$model:streamGenerateContent")
                parameters.append("key", apiKey)
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val channel: ByteReadChannel = response.bodyAsChannel()
        try {
            while (!channel.isClosedForRead) {
                emit(jsonDecoder.decodeFromString<GenerateContentResponse>(channel.readRemaining().readString()))
            }
        } finally {
            channel.cancel()
        }
    }.flowOn(dispatcher)
}