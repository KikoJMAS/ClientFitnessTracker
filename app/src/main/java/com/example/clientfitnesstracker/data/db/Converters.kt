package com.example.clientfitnesstracker.data.db

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Converters {
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @TypeConverter
    fun fromDate(value: Date?): String? = value?.let { formatter.format(it) }

    @TypeConverter
    fun toDate(value: String?): Date? = value?.let { formatter.parse(it) }
}