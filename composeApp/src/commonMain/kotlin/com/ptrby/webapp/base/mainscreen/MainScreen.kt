package com.ptrby.webapp.base.mainscreen

import Task
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//Компонент, представляющий главный экран приложения. Отображает список дел и панель управления.
// При нажатии на элементы списка позволяет редактировать и удалять задачи
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val vm = MainViewModel()
    var expandedState by remember { mutableStateOf(false) }
    val isLoggedIn by remember { vm.isLogged }
    var isAlertOpen by remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()

    val list = vm.list

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Списки")
                },
                actions = {
                    DropdownMenu(
                        expanded = expandedState,
                        onDismissRequest = {
                            expandedState = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    if (isLoggedIn) {
                                        "Выйти"
                                    } else {
                                        "Авторизироваться"
                                    }
                                )
                            },
                            onClick = {
                                if (isLoggedIn) {
                                    // TODO: Реализовать выход
                                } else {
                                    isAlertOpen = true
                                }
                                expandedState = false
                            }
                        )
                    }
                    IconButton(
                        onClick = {
                            expandedState = true
                        }
                    ) {
                        Icon(Icons.Default.Menu, "Открыть выпадающее меню")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn {
                items(list, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        isLoggedIn = isLoggedIn,
                        onDeleteClick = {

                        },
                        onEditClick = {
                            coroutine.launch { vm.edit(it) }
                        }
                    )
                }
            }
        }
    }
    if (isAlertOpen) {
        var login by remember { mutableStateOf("") }
        var isError by remember { mutableStateOf(false) }
        var password by remember { mutableStateOf("") }
        AuthenticationDialog(
            login = login,
            onLoginChange = { login = it },
            password = password,
            onPasswordChange = { password = it },
            onDismiss = { isAlertOpen = false },
            onConfirm = { authLogin, authPassword ->
                coroutine.launch {
                    val status = vm.logging(authLogin, authPassword)
                    if (! status) {
                        isError = true
                    }
                    delay(5_000)
                    isError = false
                }
            },

            isError = isError,
            clearError = {
                isError = false
            }
        )
    }
}

//Компонент, отображающий карточку дела. Позволяет редактировать и удалять задачи при наличии соответствующих прав
@Composable
fun TaskCard(
    task: Task,
    isLoggedIn: Boolean,
    onDeleteClick: (Int) -> Unit,
    onEditClick: (Task) -> Unit
) {
    var editOpened by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (isLoggedIn) {
                Row {
                    IconButton(onClick = {
                        editOpened = true
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { onDeleteClick(task.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }

    if (editOpened) {
        EditTaskDialog(
            task = task,
            onSaveClick = {
                onEditClick(it)
            },
            onDismissClick = {
                editOpened = false
            }
        )
    }
}

//Диалоговое окно для редактирования задачи. Позволяет изменить заголовок и описание задачи
@Composable
fun EditTaskDialog(
    task: Task,
    onSaveClick: (Task) -> Unit,
    onDismissClick: () -> Unit
) {
    val title = remember { mutableStateOf(task.title) }
    val description = remember { mutableStateOf(task.description) }

    AlertDialog(
        onDismissRequest = onDismissClick,
        title = { Text(text = "Edit Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text("Title") }
                )
                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSaveClick(task.copy(title = title.value, description = description.value)) }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissClick
            ) {
                Text("Dismiss")
            }
        }
    )
}

//Диалоговое окно для аутентификации пользователя. Позволяет пользователю ввести логин и пароль.
//Отображает ошибку, если введены некорректные данные
@Composable
fun AuthenticationDialog(
    isError: Boolean,
    clearError: () -> Unit,
    login: String,
    onLoginChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Login") },
        text = {
            Column {
                OutlinedTextField(
                    value = login,
                    onValueChange = {
                        clearError()
                        onLoginChange(it)
                    },
                    label = { Text("Email") },
                    isError = isError
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        clearError()
                        onPasswordChange(it)
                    },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = isError
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(login, password) }
            ) {
                Text("Авторизоваться")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Выйти")
            }
        }
    )
}