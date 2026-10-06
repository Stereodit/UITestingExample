package com.example.uitestingexample.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import com.example.uitestingexample.R
import com.example.uitestingexample.ui.theme.UITestingExampleTheme

@Composable
fun LoginScreen(
    email: String,
    password: String,
    passwordVisible: Boolean,
    emailError: EmailError?,
    passwordError: PasswordError?,
    credentialsError: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePassword: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .testTag(TestTags.LOGIN_SCREEN),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .testTag(TestTags.LOGIN_TITLE)
                    .semantics { heading() },
            )
            Text(
                text = stringResource(R.string.login_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(
                        R.string.demo_hint,
                        DemoCredentials.EMAIL,
                        DemoCredentials.PASSWORD,
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .testTag(TestTags.DEMO_HINT),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            LabeledField(
                value = email,
                onValueChange = onEmailChange,
                label = stringResource(R.string.email_label),
                tag = TestTags.EMAIL,
                error = emailError?.message(),
                errorTag = TestTags.EMAIL_ERROR,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            )
            LabeledField(
                value = password,
                onValueChange = onPasswordChange,
                label = stringResource(R.string.password_label),
                tag = TestTags.PASSWORD,
                error = passwordError?.message(),
                errorTag = TestTags.PASSWORD_ERROR,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                onImeAction = onSubmit,
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
            )
            TextButton(
                onClick = onTogglePassword,
                modifier = Modifier.testTag(TestTags.PASSWORD_TOGGLE),
            ) {
                Text(
                    stringResource(
                        if (passwordVisible) R.string.hide_password else R.string.show_password,
                    ),
                )
            }
            if (credentialsError) {
                Text(
                    text = stringResource(R.string.error_credentials),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .testTag(TestTags.LOGIN_ERROR)
                        .semantics { liveRegion = LiveRegionMode.Polite },
                )
            }
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.LOGIN_BUTTON),
            ) {
                Text(stringResource(R.string.login_button))
            }
        }
    }
}

@Composable
private fun LabeledField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    tag: String,
    error: String?,
    errorTag: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onImeAction: () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(tag),
            label = { Text(label) },
            singleLine = true,
            isError = error != null,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
            ),
            keyboardActions = KeyboardActions(
                onDone = { onImeAction() },
            ),
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .testTag(errorTag)
                    .semantics { liveRegion = LiveRegionMode.Polite },
            )
        }
    }
}

@StringRes
private fun EmailError.messageRes(): Int = when (this) {
    EmailError.Empty -> R.string.error_empty_email
    EmailError.Invalid -> R.string.error_invalid_email
}

@StringRes
private fun PasswordError.messageRes(): Int = when (this) {
    PasswordError.Empty -> R.string.error_empty_password
    PasswordError.TooShort -> R.string.error_short_password
}

@Composable
private fun EmailError.message(): String = stringResource(messageRes())

@Composable
private fun PasswordError.message(): String = stringResource(messageRes())

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    UITestingExampleTheme(dynamicColor = false) {
        LoginScreen(
            email = "",
            password = "",
            passwordVisible = false,
            emailError = EmailError.Empty,
            passwordError = null,
            credentialsError = false,
            onEmailChange = {},
            onPasswordChange = {},
            onTogglePassword = {},
            onSubmit = {},
        )
    }
}
