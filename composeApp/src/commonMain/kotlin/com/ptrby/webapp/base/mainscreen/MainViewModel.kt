package com.ptrby.webapp.base.mainscreen

import LoginObject
import Task
import androidx.compose.runtime.mutableStateOf
import com.ptrby.webapp.di.network.KtorRepository
import com.ptrby.webapp.di.settings.SettingsRepository
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
//ViewModel главного экрана приложения. Управляет состоянием аутентификации пользователя и списком задач.
//Использует KtorRepository для взаимодействия с сервером и SettingsRepository для работы с настройками


//Класс MainViewModel управляет состоянием аутентификации пользователя и загрузкой списка задач.
class MainViewModel : KoinComponent {
    val isLogged = mutableStateOf(false)
    private val repo: KtorRepository by inject()
    private val settings: SettingsRepository by inject()
    val list = mutableListOf<Task>()
    private val coroutine = CoroutineScope(Dispatchers.Default)

    init {
        isLogged.value = settings.getValue("logged")?.toBooleanStrictOrNull() ?: false
        coroutine.launch {
            list.addAll(repo.getAllTasks())
        }
    }
//Метод logging выполняет аутентификацию пользователя, обновляет состояние аутентификации и возвращает результат
    suspend fun logging(login: String, password: String) : Boolean {
        val status = repo.login(LoginObject(login, password))
        val res = status == HttpStatusCode.OK
        isLogged.value = res
        return status == HttpStatusCode.OK
    }
//Выполняет удаление дел
    suspend fun delete(id: Int) {
        repo.deleteTask(id)
        list.clear()
        list.addAll(repo.getAllTasks())
    }
//Выполняет редактирование дел
    suspend fun edit(task: Task) {
        repo.updateTask(task)
    }
}
