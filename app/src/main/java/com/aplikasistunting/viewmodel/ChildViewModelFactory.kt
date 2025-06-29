package com.aplikasistunting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aplikasistunting.repository.ChildRepository

class ChildViewModelFactory(
    private val repository: ChildRepository,
    private val parentId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChildViewModel::class.java)) {
            return ChildViewModel(repository, parentId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}