package com.example.uitestingexample.ui

/**
 * Стабильные селекторы. На корне включён testTagsAsResourceId,
 * поэтому те же id видны Compose-тестам и UiAutomator.
 */
object TestTags {
    const val LOGIN_SCREEN = "login_screen"
    const val LOGIN_TITLE = "login_title"
    const val EMAIL = "login_email"
    const val EMAIL_ERROR = "login_email_error"
    const val PASSWORD = "login_password"
    const val PASSWORD_ERROR = "login_password_error"
    const val PASSWORD_TOGGLE = "login_password_toggle"
    const val LOGIN_BUTTON = "login_submit"
    const val LOGIN_ERROR = "login_error"
    const val DEMO_HINT = "login_demo_hint"

    const val TASKS_SCREEN = "tasks_screen"
    const val TASKS_TITLE = "tasks_title"
    const val GREETING = "tasks_greeting"
    const val ADD_FIELD = "tasks_add_field"
    const val ADD_BUTTON = "tasks_add_button"
    const val FILTER_ALL = "tasks_filter_all"
    const val FILTER_ACTIVE = "tasks_filter_active"
    const val FILTER_DONE = "tasks_filter_done"
    const val ACTIVE_COUNT = "tasks_active_count"
    const val TASKS_LIST = "tasks_list"
    const val TASKS_EMPTY = "tasks_empty"
    const val LOGOUT = "tasks_logout"
    const val DELETE_DIALOG = "delete_dialog"
    const val DELETE_CONFIRM = "delete_confirm"
    const val DELETE_DISMISS = "delete_dismiss"

    fun taskItem(id: Int) = "task_item_$id"
    fun taskCheckbox(id: Int) = "task_checkbox_$id"
    fun taskDelete(id: Int) = "task_delete_$id"
}
