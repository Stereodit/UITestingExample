package com.example.uitestingexample.ui

object DemoCredentials {
    const val EMAIL = "demo@mail.com"
    const val PASSWORD = "Test1234"
}

const val MIN_PASSWORD_LENGTH = 6

enum class EmailError { Empty, Invalid }

enum class PasswordError { Empty, TooShort }

data class LoginValidation(
    val emailError: EmailError?,
    val passwordError: PasswordError?,
    val credentialsError: Boolean,
) {
    val isSuccess: Boolean =
        emailError == null && passwordError == null && !credentialsError
}

fun validateLogin(email: String, password: String): LoginValidation {
    val normalizedEmail = email.trim().lowercase()
    val emailError = when {
        normalizedEmail.isEmpty() -> EmailError.Empty
        !isValidEmail(normalizedEmail) -> EmailError.Invalid
        else -> null
    }
    val passwordError = when {
        password.isEmpty() -> PasswordError.Empty
        password.length < MIN_PASSWORD_LENGTH -> PasswordError.TooShort
        else -> null
    }
    val credentialsError = emailError == null &&
        passwordError == null &&
        (normalizedEmail != DemoCredentials.EMAIL || password != DemoCredentials.PASSWORD)
    return LoginValidation(emailError, passwordError, credentialsError)
}

private fun isValidEmail(email: String): Boolean {
    if (email.any { it.isWhitespace() }) return false
    val at = email.indexOf('@')
    if (at <= 0 || at != email.lastIndexOf('@')) return false
    val domain = email.substring(at + 1)
    val dot = domain.lastIndexOf('.')
    return dot > 0 && dot < domain.lastIndex
}
