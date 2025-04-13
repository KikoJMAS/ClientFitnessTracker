package com.example.clientfitnesstracker.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Preview
@Composable
fun CalendarScreen(calendarViewModel: CalendarViewModel = viewModel()) {
    val calendarUIState by calendarViewModel.uiState.collectAsState()
    val appointmentsForDate = calendarViewModel.getAppointments()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Appointment Calendar", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            item {
                Calendar(
                    selectedDate = calendarUIState.selectedDate,
                    onDateSelected = { calendarViewModel.onDateSelected(it) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (calendarUIState.dateSelected) {
                item {
                    Text(
                        "Appointments on ${calendarUIState.selectedDate}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                items(appointmentsForDate) { appointment ->
                    Card (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = appointment.client,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Time: ${appointment.startTime} - ${appointment.endTime}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Exercises: ${appointment.exercises.joinToString()}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    calendarViewModel.removeAppointment(appointment)
                                },
                                modifier = Modifier.alignByBaseline()
                            ) {
                                Icon(Icons.Default.Delete, "Delete")
                            }
                        }
                    }
                }
                if (!calendarViewModel.beforeToday()) {
                    item {
                        Button(
                            onClick = { calendarViewModel.dialogOn() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Set Appointment")
                        }
                    }
                }
            } else {
                item {
                    Text(
                        "Please select a date.",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        if (calendarUIState.showDialog) {
            SetAppointmentDialog(
                onDismiss = { calendarViewModel.dialogOff() },
                onConfirm = {
                    calendarViewModel.addAppointment()
                    calendarViewModel.dialogOff()
                },
                calendarViewModel = calendarViewModel,
                calendarUIState = calendarUIState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val dateState = rememberDatePickerState()

    LaunchedEffect(dateState.selectedDateMillis) {
        dateState.selectedDateMillis?.let { millis ->
            val selected = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            if (selected != selectedDate) {
                onDateSelected(selected)
            }
        }
    }

    DatePicker(state = dateState)
}

@Composable
fun SetAppointmentDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    calendarViewModel: CalendarViewModel,
    calendarUIState: CalendarUIState
) {
    val context = LocalContext.current

    if (calendarUIState.showStartPicker) {
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                calendarViewModel.updateStartTime(hour, minute)
                calendarViewModel.toggleStartPicker(false)
            },
            calendarViewModel.getHour(calendarUIState.startTime),
            calendarViewModel.getMinutes(calendarUIState.startTime),
            true
        ).apply {
            setOnCancelListener {
                calendarViewModel.toggleStartPicker(false)
            }
        }.show()
    }

    if (calendarUIState.showEndPicker) {
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                calendarViewModel.updateEndTime(hour, minute)
                calendarViewModel.toggleEndPicker(false)
            },
            calendarViewModel.getHour(calendarUIState.endTime),
            calendarViewModel.getMinutes(calendarUIState.endTime),
            true
        ).apply {
            setOnCancelListener {
                calendarViewModel.toggleStartPicker(false)
            }
        }.show()
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("New Appointment", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = calendarUIState.clientName,
                    onValueChange = { calendarViewModel.clientNameChange(it) },
                    label = { Text("Client Name") }
                )

                Button(
                    onClick = { calendarViewModel.toggleStartPicker(true) },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Start Time: ${calendarUIState.startTime}")
                }

                Button(
                    onClick = { calendarViewModel.toggleEndPicker(true) },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("End Time: ${calendarUIState.endTime}")
                }

                OutlinedTextField(
                    value = calendarUIState.exercise,
                    onValueChange = { calendarViewModel.exerciseChange(it) },
                    label = { Text("Add Exercise") },
                    modifier = Modifier.padding(top = 8.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { calendarViewModel.addExercise() })
                )

                LazyColumn {
                    itemsIndexed(calendarUIState.exerciseList) { index, exercise ->
                        Card (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = exercise,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        calendarViewModel.removeExercise(index)
                                    },
                                    modifier = Modifier.alignByBaseline()
                                ) {
                                    Icon(Icons.Default.Delete, "Delete")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row {
                    Button(onClick = { onDismiss() }, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
