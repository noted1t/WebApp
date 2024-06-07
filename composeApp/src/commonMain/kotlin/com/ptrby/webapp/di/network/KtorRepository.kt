package com.ptrby.webapp.di.network

import LoginObject
import Message
import Task
import com.ptrby.webapp.di.settings.SettingsRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
//Класс с методами для взаимодействия с сервером через HTTP-запросы.
class KtorRepository(private val httpClient: HttpClient, private val settings: SettingsRepository) {
    private val baseUrl = "http://localhost:8080"
//Авторизация пользователя, сохранение ключа авторизации
    suspend fun login(login: LoginObject) : HttpStatusCode {
        val loginUrl = "${baseUrl}/login"
        val answer = httpClient.post(loginUrl) {
            url {
                contentType(Json)
                setBody(login)
            }
        }
        val id = answer.body<Message>().message
        settings.addValue("login_key", id)
        return answer.status
    }
//Получение Id пользователя по токену
    suspend fun getId(token: String) : Int? {
        val url = "$baseUrl/login/getId/${token}"
        val answer = httpClient.get(url).body<Message>()
        return answer.message.toIntOrNull()
    }
//Получение списка всех дел
    suspend fun getAllTasks() : List<Task> {
        val url = "$baseUrl/tasks"
        return httpClient.get(url).body<List<Task>>()
    }
//Получение дела по Id
    suspend fun getTaskById(id: Int) : Task {
        val url = "$baseUrl/tasks/$id"
        return httpClient.get(url).body<Task>()
    }
//Размещение дела
    suspend fun postTask(task: Task) : Int {
        val url = "$baseUrl/tasks/add"
        return httpClient.post(url) {
            contentType(Json)
            setBody(task)
        }.body<Int>()
    }
//Обновление дела
    suspend fun updateTask(task: Task): HttpStatusCode {
        val url = "$baseUrl/tasks/${task.id}"
        return httpClient.put(url) {
            contentType(Json)
            setBody(task)
        }.status
    }
//Удаление дела
    suspend fun deleteTask(id: Int) : HttpStatusCode {
        val url = "$baseUrl/tasks/$id"
        return httpClient.delete(url).status
    }
}