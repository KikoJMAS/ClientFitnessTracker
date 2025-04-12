package com.example.clientfitnesstracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.clientfitnesstracker.model.Client

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditClientScreen(
    existingClient: Client? = null,
    onSave: (Client) -> Unit,
    onCancel: () -> Unit,
    onDelete: (Client) -> Unit = {}
) {
    var name by remember { mutableStateOf(existingClient?.name ?: "") }
    var age by remember { mutableStateOf(existingClient?.age?.toString() ?: "") }

    var nameError by remember { mutableStateOf("") }
    var ageError by remember { mutableStateOf("") }

    val fitnessLevels = listOf("Beginner", "Intermediate", "Expert")
    var fitnessExpanded by remember { mutableStateOf(false) }
    var selectedFitnessLevel by remember { mutableStateOf(existingClient?.fitnessLevel ?: fitnessLevels[0]) }

    val goalOptions = listOf(
        "Lose weight", "Build muscle", "Improve stamina",
        "Increase flexibility", "Establish routine", "Rehabilitation"
    )
    var goalExpanded by remember { mutableStateOf(false) }
    var selectedGoal by remember { mutableStateOf(existingClient?.goals ?: goalOptions[0]) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    // Validation
    val isNameValid = name.matches(Regex("^[A-Za-z ]+$"))
    val isAgeValid = age.matches(Regex("^\\d{1,2}$"))

    nameError = when {
        name.isBlank() -> "Name cannot be empty"
        !isNameValid -> "Name must contain only letters"
        else -> ""
    }

    ageError = when {
        age.isBlank() -> "Age cannot be empty"
        !isAgeValid -> "Age must be a number between 1 and 99"
        else -> ""
    }

    val isFormValid = nameError.isEmpty() && ageError.isEmpty()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(if (existingClient != null) "Edit Client" else "Add Client")
            })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                isError = nameError.isNotEmpty()
            )
            if (nameError.isNotEmpty()) {
                Text(nameError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // Age Field
            OutlinedTextField(
                value = age,
                onValueChange = { if (it.length <= 2) age = it },
                label = { Text("Age") },
                isError = ageError.isNotEmpty(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            if (ageError.isNotEmpty()) {
                Text(ageError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // Fitness Level Dropdown
            ExposedDropdownMenuBox(expanded = fitnessExpanded, onExpandedChange = { fitnessExpanded = !fitnessExpanded }) {
                OutlinedTextField(
                    value = selectedFitnessLevel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fitness Level") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(fitnessExpanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(expanded = fitnessExpanded, onDismissRequest = { fitnessExpanded = false }) {
                    fitnessLevels.forEach { level ->
                        DropdownMenuItem(text = { Text(level) }, onClick = {
                            selectedFitnessLevel = level
                            fitnessExpanded = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Goals Dropdown
            ExposedDropdownMenuBox(expanded = goalExpanded, onExpandedChange = { goalExpanded = !goalExpanded }) {
                OutlinedTextField(
                    value = selectedGoal,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Goal") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(goalExpanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(expanded = goalExpanded, onDismissRequest = { goalExpanded = false }) {
                    goalOptions.forEach { goal ->
                        DropdownMenuItem(text = { Text(goal) }, onClick = {
                            selectedGoal = goal
                            goalExpanded = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val client = Client(
                            id = existingClient?.id ?: (1..100000).random(),
                            name = name,
                            age = age.toInt(),
                            fitnessLevel = selectedFitnessLevel,
                            goals = selectedGoal
                        )
                        onSave(client)
                    },
                    enabled = isFormValid
                ) {
                    Text("Save")
                }

                OutlinedButton(onClick = onCancel) {
                    Text("Cancel")
                }

                if (existingClient != null) {
                    OutlinedButton(
                        onClick = { showDeleteDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Delete")
                    }
                }
            }

            // Delete Confirmation Dialog
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            onDelete(existingClient!!)
                            showDeleteDialog = false
                        }) {
                            Text("Delete", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Delete Client") },
                    text = { Text("Are you sure you want to delete this client?") }
                )
            }
        }
    }
}
