package com.aplikasistunting.repository

import androidx.lifecycle.LiveData
import com.aplikasistunting.data.HistoryDao
import com.aplikasistunting.data.HistoryEntity

class HistoryRepository(private val dao: HistoryDao) {
    suspend fun insert(history: HistoryEntity) = dao.insert(history)

    fun getAll(): LiveData<List<HistoryEntity>> = dao.getAll()

    suspend fun deleteHistory(history: HistoryEntity) {
        dao.delete(history)
    }
}
