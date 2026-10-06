package com.example.uitestingexample

import androidx.annotation.StringRes
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.uitestingexample.ui.DemoCredentials
import com.example.uitestingexample.ui.TestTags

fun hasEditableText(expected: String): SemanticsMatcher {
    return SemanticsMatcher("EditableText is '$expected'") { node ->
        node.config.getOrNull(SemanticsProperties.EditableText)?.text == expected
    }
}

fun AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>.stringRes(
    @StringRes id: Int,
    vararg formatArgs: Any,
): String {
    return if (formatArgs.isEmpty()) {
        activity.getString(id)
    } else {
        activity.getString(id, *formatArgs)
    }
}

fun AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>.loginWithDemoCredentials() {
    onNodeWithTag(TestTags.EMAIL).performTextReplacement(DemoCredentials.EMAIL)
    onNodeWithTag(TestTags.PASSWORD).performTextReplacement(DemoCredentials.PASSWORD)
    onNodeWithTag(TestTags.LOGIN_BUTTON).performClick()
    onNodeWithTag(TestTags.TASKS_SCREEN).assertIsDisplayed()
}
