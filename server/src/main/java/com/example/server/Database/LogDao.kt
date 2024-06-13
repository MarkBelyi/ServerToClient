package com.example.server.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LogDao {
    @Insert
    suspend fun insert(log: LogEntry)

    @Query("SELECT * FROM logs")
    suspend fun getAllLogs(): List<LogEntry>
}