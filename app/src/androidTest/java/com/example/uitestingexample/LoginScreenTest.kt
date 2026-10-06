package com.example.uitestingexample

import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.uitestingexample.ui.DemoCredentials
import com.example.uitestingexample.ui.TestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginScreen_showsHeadingHintAndEnabledButton() {
        composeRule.onNodeWithTag(TestTags.LOGIN_TITLE)
            .assertIsDisplayed()
            .assert(isHeading())
        composeRule.onNodeWithTag(TestTags.DEMO_HINT).assertTextEquals(
            composeRule.stringRes(
                R.string.demo_hint,
                DemoCredentials.EMAIL,
                DemoCredentials.PASSWORD,
            ),
        )
        composeRule.onNodeWithTag(TestTags.EMAIL).assert(hasEditableText(""))
        composeRule.onNodeWithTag(TestTags.PASSWORD).assert(hasEditableText(""))
        composeRule.onNodeWithTag(TestTags.LOGIN_BUTTON)
            .assertHasClickAction()
            .assertIsEnabled()
        composeRule.onNodeWithTag(TestTags.TASKS_SCREEN).assertDoesNotExist()
    }

    @Test
    fun submitEmpty_showsFieldErrorsAndStaysOnLogin() {
        composeRule.onNodeWithTag(TestTags.LOGIN_BUTTON).performClick()

        composeRule.onNodeWithTag(TestTags.EMAIL_ERROR)
            .assertTextEquals(composeRule.stringRes(R.string.error_empty_email))
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite))
        composeRule.onNodeWithTag(TestTags.PASSWORD_ERROR)
            .assertTextEquals(composeRule.stringRes(R.string.error_empty_password))
        composeRule.onNodeWithTag(TestTags.LOGIN_ERROR).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.TASKS_SCREEN).assertDoesNotExist()
    }

    @Test
    fun invalidEmail_andShortPassword_showBothErrors() {
        composeRule.onNodeWithTag(TestTags.EMAIL).performTextReplacement("demo")
        composeRule.onNodeWithTag(TestTags.PASSWORD).performTextReplacement("123")
        composeRule.onNodeWithTag(TestTags.LOGIN_BUTTON).performClick()

        composeRule.onNodeWithTag(TestTags.EMAIL_ERROR)
            .assertTextEquals(composeRule.stringRes(R.string.error_invalid_email))
        composeRule.onNodeWithTag(TestTags.PASSWORD_ERROR)
            .assertTextEquals(composeRule.stringRes(R.string.error_short_password))
    }

    @Test
    fun wrongCredentials_showBanner_editingClearsIt() {
        composeRule.onNodeWithTag(TestTags.EMAIL).performTextReplacement(DemoCredentials.EMAIL)
        composeRule.onNodeWithTag(TestTags.PASSWORD).performTextReplacement("123456")
        composeRule.onNodeWithTag(TestTags.LOGIN_BUTTON).performClick()

        composeRule.onNodeWithTag(TestTags.LOGIN_ERROR)
            .assertTextEquals(composeRule.stringRes(R.string.error_credentials))
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite))

        composeRule.onNodeWithTag(TestTags.PASSWORD).performTextReplacement(DemoCredentials.PASSWORD)
        composeRule.onNodeWithTag(TestTags.LOGIN_ERROR).assertDoesNotExist()
    }

    @Test
    fun editingEmail_clearsOnlyEmailError() {
        composeRule.onNodeWithTag(TestTags.LOGIN_BUTTON).performClick()
        composeRule.onNodeWithTag(TestTags.EMAIL).performTextReplacement("a")

        composeRule.onNodeWithTag(TestTags.EMAIL_ERROR).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.PASSWORD_ERROR)
            .assertTextEquals(composeRule.stringRes(R.string.error_empty_password))
    }

    @Test
    fun passwordToggle_keepsEnteredText() {
        composeRule.onNodeWithTag(TestTags.PASSWORD).performTextReplacement("secret1")
        composeRule.onNodeWithTag(TestTags.PASSWORD_TOGGLE)
            .assertTextEquals(composeRule.stringRes(R.string.show_password))
            .performClick()
        composeRule.onNodeWithTag(TestTags.PASSWORD_TOGGLE)
            .assertTextEquals(composeRule.stringRes(R.string.hide_password))
        composeRule.onNodeWithTag(TestTags.PASSWORD).assert(hasEditableText("secret1"))
    }

    @Test
    fun demoCredentials_ignoreEmailCaseAndOpenTasks() {
        composeRule.onNodeWithTag(TestTags.EMAIL).performTextReplacement("  Demo@Mail.com  ")
        composeRule.onNodeWithTag(TestTags.PASSWORD).performTextReplacement(DemoCredentials.PASSWORD)
        composeRule.onNodeWithTag(TestTags.LOGIN_BUTTON).performClick()

        composeRule.onNodeWithTag(TestTags.TASKS_SCREEN).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.GREETING).assertTextEquals(
            composeRule.stringRes(R.string.signed_in_as, DemoCredentials.EMAIL),
        )
    }
}
