package com.example.uitestingexample.ui

import androidx.annotation.StringRes
import com.example.uitestingexample.R

data class Task(
    val id: Int,
    val title: String,
    val done: Boolean,
)

enum class TaskFilter(@param:StringRes val labelRes: Int, val testTag: String) {
    All(R.string.filter_all, TestTags.FILTER_ALL),
    Active(R.string.filter_active, TestTags.FILTER_ACTIVE),
    Done(R.string.filter_done, TestTags.FILTER_DONE),
}

object SampleTasks {
    val titles: List<String> = listOf(
        "Открыть экран входа",
        "Проверить пустой email",
        "Проверить короткий пароль",
        "Войти с демо-доступом",
        "Добавить задачу",
        "Отметить задачу выполненной",
        "Отфильтровать список",
        "Прокрутить к нижней задаче",
        "Удалить задачу через диалог",
        "Выйти на экран входа",
    )

    fun initial(): List<Task> = titles.mapIndexed { index, title ->
        Task(id = index + 1, title = title, done = false)
    }
}
