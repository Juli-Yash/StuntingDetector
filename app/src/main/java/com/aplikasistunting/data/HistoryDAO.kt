package com.aplikasistunting.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(history: HistoryEntity)

    @Delete
    suspend fun delete(history: HistoryEntity)

    @Query("SELECT * FROM history ORDER BY id DESC")
    fun getAll(): LiveData<List<HistoryEntity>>
}