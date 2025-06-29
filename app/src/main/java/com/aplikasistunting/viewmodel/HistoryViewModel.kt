package com.aplikasistunting.viewmodel

import androidx.lifecycle.*
import com.aplikasistunting.data.HistoryEntity
import com.aplikasistunting.repository.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {
    val allHistory: LiveData<List<HistoryEntity>> = repository.getAll()

    fun insert(history: HistoryEntity) {
        viewModelScope.launch {
            repository.insert(history)
        }
    }

    fun delete(history: HistoryEntity) {
        viewModelScope.launch {
            repository.deleteHistory(history)
        }
    }

}