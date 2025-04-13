package com.example.clientfitnesstracker.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class Appointment (
    val client: String = "",
    val startTime: LocalTime = LocalTime.now(),
    val endTime: LocalTime = LocalTime.now(),
    val exercises: List<String> = emptyList()
)

data class CalendarUIState(
    val dateSelected: Boolean = false,
    val selectedDate: LocalDate = LocalDate.ofEpochDay(0),
    val appointments: Map<LocalDate, List<Appointment>> = emptyMap(),
    val showDialog: Boolean = false,
    val clientName: String = "",
    val startTime: String = "09:00",
    val endTime: String = "10:00",
    val exercise: String = "",
    val exerciseList: List<String> = emptyList(),
    val showStartPicker: Boolean = false,
    val showEndPicker: Boolean = false,
    )

class CalendarViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUIState())
    val uiState: StateFlow<CalendarUIState> = _uiState.asStateFlow()

    fun onDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(dateSelected = true, selectedDate = date)
    }

    fun beforeToday(): Boolean {
        return _uiState.value.selectedDate.isBefore(LocalDate.now())
    }

    fun dialogOn() {
        _uiState.value = _uiState.value.copy(showDialog = true)
    }

    fun dialogOff() {
        _uiState.value = _uiState.value.copy(showDialog = false)
    }

    fun clientNameChange(name: String) {
        _uiState.value = _uiState.value.copy(clientName = name)
    }

    fun getHour(time: String): Int {
        return time.take(2).toInt()
    }

    fun getMinutes(time: String): Int {
        return time.takeLast(2).toInt()
    }

    fun exerciseChange(exercise: String) {
        _uiState.value = _uiState.value.copy(exercise = exercise)
    }

    fun addExercise() {
        val newList = _uiState.value.exerciseList + _uiState.value.exercise
        _uiState.value = _uiState.value.copy(exerciseList = newList)
    }

    fun removeExercise(index: Int) {
        val newList = _uiState.value.exerciseList.filterIndexed { i, _ -> i != index }
        _uiState.value = _uiState.value.copy(exerciseList = newList)
    }

    fun toggleStartPicker(show: Boolean) {
        _uiState.value = _uiState.value.copy(showStartPicker = show)
    }

    fun toggleEndPicker(show: Boolean) {
        _uiState.value = _uiState.value.copy(showEndPicker = show)
    }

    fun updateStartTime(hour: Int, minute: Int) {
        _uiState.value = _uiState.value.copy(startTime = "%02d:%02d".format(hour, minute))
    }

    fun updateEndTime(hour: Int, minute: Int) {
        _uiState.value = _uiState.value.copy(endTime = "%02d:%02d".format(hour, minute))
    }

    fun addAppointment() {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val startTime = LocalTime.parse(_uiState.value.startTime, formatter)
        val endTime = LocalTime.parse(_uiState.value.endTime, formatter)
        val currentDate = _uiState.value.selectedDate

        val newAppointment = Appointment(
            client = _uiState.value.clientName,
            startTime = startTime,
            endTime = endTime,
            exercises = _uiState.value.exerciseList
        )

        val currentAppointments = getAppointments()
        val updatedList = currentAppointments + newAppointment
        val updatedMap = _uiState.value.appointments + mapOf(currentDate to updatedList)

        _uiState.value = _uiState.value.copy(
            appointments = updatedMap,
            clientName = "",
            startTime = "09:00",
            endTime = "10:00",
            exercise = "",
            exerciseList = emptyList()
        )
    }

    fun removeAppointment(appointment: Appointment) {
        val currentDate = _uiState.value.selectedDate
        val currentAppointments = getAppointments()
        val updatedAppointments = currentAppointments.filter { it != appointment }

        val updatedMap = _uiState.value.appointments.toMutableMap().apply {
            put(currentDate, updatedAppointments)
        }
        _uiState.value = _uiState.value.copy(appointments = updatedMap)
    }

    fun getAppointments(): List<Appointment> {
        return _uiState.value.appointments[_uiState.value.selectedDate] ?: emptyList()
    }
}
