package com.example.uitestingexample.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId

private enum class AppScreen { Login, Tasks }

@Composable
fun UiTestingApp() {
    var screen by remember { mutableStateOf(AppScreen.Login) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<EmailError?>(null) }
    var passwordError by remember { mutableStateOf<PasswordError?>(null) }
    var credentialsError by remember { mutableStateOf(false) }
    var signedInEmail by remember { mutableStateOf("") }

    var tasks by remember { mutableStateOf(SampleTasks.initial()) }
    var draft by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(TaskFilter.All) }
    var pendingDeleteId by remember { mutableStateOf<Int?>(null) }

    fun submitLogin() {
        val result = validateLogin(email, password)
        emailError = result.emailError
        passwordError = result.passwordError
        credentialsError = result.credentialsError
        if (result.isSuccess) {
            signedInEmail = email.trim().lowercase()
            screen = AppScreen.Tasks
        }
    }

    fun addTask() {
        val title = draft.trim()
        if (title.isEmpty()) return
        val nextId = (tasks.maxOfOrNull { it.id } ?: 0) + 1
        tasks = listOf(Task(id = nextId, title = title, done = false)) + tasks
        draft = ""
    }

    fun logout() {
        screen = AppScreen.Login
        email = ""
        password = ""
        passwordVisible = false
        emailError = null
        passwordError = null
        credentialsError = false
        signedInEmail = ""
        tasks = SampleTasks.initial()
        draft = ""
        filter = TaskFilter.All
        pendingDeleteId = null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics { testTagsAsResourceId = true },
    ) {
        when (screen) {
            AppScreen.Login -> LoginScreen(
                email = email,
                password = password,
                passwordVisible = passwordVisible,
                emailError = emailError,
                passwordError = passwordError,
                credentialsError = credentialsError,
                onEmailChange = {
                    email = it
                    emailError = null
                    credentialsError = false
                },
                onPasswordChange = {
                    password = it
                    passwordError = null
                    credentialsError = false
                },
                onTogglePassword = { passwordVisible = !passwordVisible },
                onSubmit = ::submitLogin,
            )

            AppScreen.Tasks -> TasksScreen(
                signedInEmail = signedInEmail,
                tasks = tasks,
                draft = draft,
                filter = filter,
                pendingDelete = tasks.find { it.id == pendingDeleteId },
                onDraftChange = { draft = it },
                onAdd = ::addTask,
                onFilter = { filter = it },
                onToggle = { id ->
                    tasks = tasks.map { task ->
                        if (task.id == id) task.copy(done = !task.done) else task
                    }
                },
                onDeleteRequest = { pendingDeleteId = it.id },
                onDeleteDismiss = { pendingDeleteId = null },
                onDeleteConfirm = {
                    tasks = tasks.filterNot { it.id == pendingDeleteId }
                    pendingDeleteId = null
                },
                onLogout = ::logout,
            )
        }
    }
}
