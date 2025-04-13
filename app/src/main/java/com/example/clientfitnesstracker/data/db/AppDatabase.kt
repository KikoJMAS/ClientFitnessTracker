package com.example.clientfitnesstracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.clientfitnesstracker.data.db.dao.ClientDao
import com.example.clientfitnesstracker.data.db.dao.WorkoutDao
import com.example.clientfitnesstracker.data.db.entities.Client
import com.example.clientfitnesstracker.data.db.entities.WorkoutSession
import com.example.clientfitnesstracker.data.db.entities.Exercise

@Database(
    entities = [Client::class, WorkoutSession::class, Exercise::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitness_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}