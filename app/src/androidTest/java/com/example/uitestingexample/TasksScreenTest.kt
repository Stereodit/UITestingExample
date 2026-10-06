package com.example.uitestingexample

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.uitestingexample.ui.SampleTasks
import com.example.uitestingexample.ui.TestTags
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun openTasks() {
        composeRule.loginWithDemoCredentials()
    }

    @Test
    fun addButton_staysDisabledUntilTitleIsNotBlank() {
        composeRule.onNodeWithTag(TestTags.ADD_BUTTON).assertIsNotEnabled()
        composeRule.onNodeWithTag(TestTags.ADD_FIELD).performTextReplacement("   ")
        composeRule.onNodeWithTag(TestTags.ADD_BUTTON).assertIsNotEnabled()
        composeRule.onNodeWithTag(TestTags.ADD_FIELD).performTextReplacement("Проверка пробелов")
        composeRule.onNodeWithTag(TestTags.ADD_BUTTON).assertIsEnabled()
    }

    @Test
    fun addTask_insertsItAndClearsField() {
        val title = "Проверить счётчик"
        composeRule.onNodeWithTag(TestTags.ADD_FIELD).performTextReplacement(title)
        composeRule.onNodeWithTag(TestTags.ADD_BUTTON).performClick()

        composeRule.onNodeWithText(title).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.ADD_FIELD).assert(hasEditableText(""))
        composeRule.onNodeWithTag(TestTags.ACTIVE_COUNT).assertTextEquals(
            composeRule.stringRes(R.string.active_count, SampleTasks.titles.size + 1),
        )
    }

    @Test
    fun toggleAndFilters_moveTaskBetweenChips() {
        val firstTitle = SampleTasks.titles.first()
        composeRule.onNodeWithTag(TestTags.taskCheckbox(1)).assertIsOff().performClick()
        composeRule.onNodeWithTag(TestTags.taskCheckbox(1)).assertIsOn()
        composeRule.onNodeWithTag(TestTags.ACTIVE_COUNT).assertTextEquals(
            composeRule.stringRes(R.string.active_count, SampleTasks.titles.size - 1),
        )

        composeRule.onNodeWithTag(TestTags.FILTER_ACTIVE).performClick()
        composeRule.onNodeWithTag(TestTags.FILTER_ACTIVE).assertIsSelected()
        composeRule.onNodeWithTag(TestTags.taskItem(1)).assertDoesNotExist()

        composeRule.onNodeWithTag(TestTags.FILTER_DONE).performClick()
        composeRule.onNodeWithTag(TestTags.FILTER_DONE).assertIsSelected()
        composeRule.onNodeWithText(firstTitle).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.taskCheckbox(1)).assertIsOn()
    }

    @Test
    fun doneFilter_showsEmptyState() {
        composeRule.onNodeWithTag(TestTags.FILTER_DONE).performClick()

        composeRule.onNodeWithTag(TestTags.TASKS_EMPTY)
            .assertTextEquals(composeRule.stringRes(R.string.tasks_empty))
        composeRule.onNodeWithTag(TestTags.TASKS_LIST).assertDoesNotExist()
    }

    @Test
    fun list_scrollsToLastTask() {
        val lastIndex = SampleTasks.titles.lastIndex
        composeRule.onNodeWithTag(TestTags.TASKS_LIST).performScrollToIndex(lastIndex)
        composeRule.onNodeWithText(SampleTasks.titles.last()).assertIsDisplayed()
    }

    @Test
    fun deleteDialog_cancelKeepsTask() {
        val title = SampleTasks.titles.first()
        composeRule.onNodeWithTag(TestTags.taskDelete(1)).performClick()
        composeRule.onNodeWithTag(TestTags.DELETE_DIALOG).assertIsDisplayed()
        composeRule.onNodeWithText(composeRule.stringRes(R.string.delete_message, title))
            .assertIsDisplayed()

        composeRule.onNodeWithTag(TestTags.DELETE_DISMISS).performClick()

        composeRule.onNodeWithTag(TestTags.DELETE_DIALOG).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.taskItem(1)).assertIsDisplayed()
    }

    @Test
    fun deleteDialog_confirmRemovesTask() {
        composeRule.onNodeWithTag(TestTags.taskDelete(1)).performClick()
        composeRule.onNodeWithTag(TestTags.DELETE_CONFIRM).performClick()

        composeRule.onNodeWithTag(TestTags.DELETE_DIALOG).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.taskItem(1)).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.ACTIVE_COUNT).assertTextEquals(
            composeRule.stringRes(R.string.active_count, SampleTasks.titles.size - 1),
        )
    }

    @Test
    fun logout_returnsToEmptyLogin() {
        composeRule.onNodeWithTag(TestTags.LOGOUT).performClick()

        composeRule.onNodeWithTag(TestTags.LOGIN_SCREEN).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.EMAIL).assert(hasEditableText(""))
        composeRule.onNodeWithTag(TestTags.PASSWORD).assert(hasEditableText(""))
        composeRule.onNodeWithTag(TestTags.TASKS_SCREEN).assertDoesNotExist()
    }
}
