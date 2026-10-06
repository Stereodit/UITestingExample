package com.example.uitestingexample

import com.example.uitestingexample.ui.DemoCredentials
import com.example.uitestingexample.ui.EmailError
import com.example.uitestingexample.ui.PasswordError
import com.example.uitestingexample.ui.validateLogin
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginValidationTest {
    @Test
    fun emptyFields_returnBothErrors() {
        val result = validateLogin("  ", "")

        assertEquals(EmailError.Empty, result.emailError)
        assertEquals(PasswordError.Empty, result.passwordError)
        assertFalse(result.credentialsError)
        assertFalse(result.isSuccess)
    }

    @Test
    fun invalidEmail_andShortPassword_returnFieldErrors() {
        val result = validateLogin("demo", "123")

        assertEquals(EmailError.Invalid, result.emailError)
        assertEquals(PasswordError.TooShort, result.passwordError)
        assertFalse(result.credentialsError)
    }

    @Test
    fun validFormat_wrongPassword_returnsCredentialsError() {
        val result = validateLogin(DemoCredentials.EMAIL, "123456")

        assertNull(result.emailError)
        assertNull(result.passwordError)
        assertTrue(result.credentialsError)
    }

    @Test
    fun passwordWithExtraSpace_isNotAccepted() {
        val result = validateLogin(DemoCredentials.EMAIL, " ${DemoCredentials.PASSWORD}")

        assertTrue(result.credentialsError)
        assertFalse(result.isSuccess)
    }

    @Test
    fun email_isTrimmedAndCaseInsensitive() {
        val result = validateLogin("  Demo@Mail.com  ", DemoCredentials.PASSWORD)

        assertTrue(result.isSuccess)
    }
}
