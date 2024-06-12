package com.example.server.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: LogEntry): Long

    @Query("SELECT * FROM logs")
    suspend fun getAllLogs(): List<LogEntry>
}