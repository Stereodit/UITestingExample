package com.example.uitestingexample.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uitestingexample.R
import com.example.uitestingexample.ui.theme.UITestingExampleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    signedInEmail: String,
    tasks: List<Task>,
    draft: String,
    filter: TaskFilter,
    pendingDelete: Task?,
    onDraftChange: (String) -> Unit,
    onAdd: () -> Unit,
    onFilter: (TaskFilter) -> Unit,
    onToggle: (Int) -> Unit,
    onDeleteRequest: (Task) -> Unit,
    onDeleteDismiss: () -> Unit,
    onDeleteConfirm: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val visible = tasks.filter { task ->
        when (filter) {
            TaskFilter.All -> true
            TaskFilter.Active -> !task.done
            TaskFilter.Done -> task.done
        }
    }
    val activeCount = tasks.count { !it.done }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(TestTags.TASKS_SCREEN),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.tasks_title),
                            modifier = Modifier
                                .testTag(TestTags.TASKS_TITLE)
                                .semantics { heading() },
                        )
                    },
                    actions = {
                        TextButton(
                            onClick = onLogout,
                            modifier = Modifier.testTag(TestTags.LOGOUT),
                        ) {
                            Text(stringResource(R.string.logout))
                        }
                    },
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(R.string.signed_in_as, signedInEmail),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .testTag(TestTags.GREETING),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value = draft,
                        onValueChange = onDraftChange,
                        modifier = Modifier
                            .weight(1f)
                            .testTag(TestTags.ADD_FIELD),
                        label = { Text(stringResource(R.string.new_task_label)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { onAdd() }),
                    )
                    Button(
                        onClick = onAdd,
                        enabled = draft.isNotBlank(),
                        modifier = Modifier.testTag(TestTags.ADD_BUTTON),
                    ) {
                        Text(stringResource(R.string.add_task))
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TaskFilter.entries.forEach { item ->
                        FilterChip(
                            selected = filter == item,
                            onClick = { onFilter(item) },
                            label = { Text(stringResource(item.labelRes)) },
                            modifier = Modifier.testTag(item.testTag),
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.active_count, activeCount),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.testTag(TestTags.ACTIVE_COUNT),
                )
                HorizontalDivider()
                if (visible.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.tasks_empty),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.testTag(TestTags.TASKS_EMPTY),
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .testTag(TestTags.TASKS_LIST),
                        contentPadding = PaddingValues(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        items(visible, key = { it.id }) { task ->
                            TaskRow(
                                task = task,
                                onToggle = onToggle,
                                onDelete = onDeleteRequest,
                            )
                        }
                    }
                }
            }
        }

        if (pendingDelete != null) {
            AlertDialog(
                onDismissRequest = onDeleteDismiss,
                modifier = Modifier
                    .testTag(TestTags.DELETE_DIALOG)
                    .semantics { testTagsAsResourceId = true },
                title = { Text(stringResource(R.string.delete_title)) },
                text = { Text(stringResource(R.string.delete_message, pendingDelete.title)) },
                confirmButton = {
                    TextButton(
                        onClick = onDeleteConfirm,
                        modifier = Modifier.testTag(TestTags.DELETE_CONFIRM),
                    ) {
                        Text(stringResource(R.string.delete_confirm))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = onDeleteDismiss,
                        modifier = Modifier.testTag(TestTags.DELETE_DISMISS),
                    ) {
                        Text(stringResource(R.string.delete_dismiss))
                    }
                },
            )
        }
    }
}

@Composable
private fun TaskRow(
    task: Task,
    onToggle: (Int) -> Unit,
    onDelete: (Task) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TestTags.taskItem(task.id)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = task.done,
            onCheckedChange = { onToggle(task.id) },
            modifier = Modifier
                .testTag(TestTags.taskCheckbox(task.id))
                .semantics { contentDescription = task.title },
        )
        Text(
            text = task.title,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None,
            color = if (task.done) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.onSurface
            },
        )
        TextButton(
            onClick = { onDelete(task) },
            modifier = Modifier.testTag(TestTags.taskDelete(task.id)),
        ) {
            Text(stringResource(R.string.delete_task))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TasksScreenPreview() {
    UITestingExampleTheme(dynamicColor = false) {
        TasksScreen(
            signedInEmail = DemoCredentials.EMAIL,
            tasks = SampleTasks.initial(),
            draft = "",
            filter = TaskFilter.All,
            pendingDelete = null,
            onDraftChange = {},
            onAdd = {},
            onFilter = {},
            onToggle = {},
            onDeleteRequest = {},
            onDeleteDismiss = {},
            onDeleteConfirm = {},
            onLogout = {},
        )
    }
}
