package com.example.clientfitnesstracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarScreen() {
    val currentDate = remember { mutableStateOf(LocalDate.now()) }
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }

    val yearMonth = YearMonth.from(currentDate.value)
    val firstDayOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    val dates = remember {
        mutableStateListOf<LocalDate?>()
    }

    dates.clear()
    repeat(firstDayOfWeek) { dates.add(null) } // Empty slots before 1st
    repeat(daysInMonth) { day ->
        dates.add(yearMonth.atDay(day + 1))
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "${yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${yearMonth.year}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                items(dates) { date ->
                    CalendarDateItem(
                        date = date,
                        isSelected = date == selectedDate.value,
                        onClick = { selectedDate.value = date },
                        isHoliday = date?.dayOfWeek?.value == 7, // Sundays are holidays
                        isToday = date == LocalDate.now()
                    )
                }
            }
        )
    }
}

@Composable
fun CalendarDateItem(
    date: LocalDate?,
    isSelected: Boolean,
    onClick: () -> Unit,
    isHoliday: Boolean = false,
    isToday: Boolean = false
) {
    val bgColor = when {
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        isHoliday -> Color.Red.copy(alpha = 0.1f)
        isToday -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(bgColor)
            .clickable(enabled = date != null) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date?.dayOfMonth?.toString() ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = if (isHoliday) Color.Red else MaterialTheme.colorScheme.onBackground
        )
    }
}
