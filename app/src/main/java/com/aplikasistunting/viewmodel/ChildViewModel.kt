package com.aplikasistunting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplikasistunting.data.ChildEntity
import com.aplikasistunting.repository.ChildRepository
import kotlinx.coroutines.launch

class ChildViewModel(
    private val repository: ChildRepository,
    private val parentId: Int
) : ViewModel() {

    val allChildren = repository.getChildrenByParentId(parentId)
    val activeChild = repository.getActiveChild()

    fun insert(child: ChildEntity) = viewModelScope.launch {
        repository.insert(child.copy(parentId = parentId))
    }

    fun delete(child: ChildEntity) = viewModelScope.launch {
        repository.delete(child)
    }

    fun setActive(id: Int) = viewModelScope.launch {
        repository.clearActiveStatus(parentId)
        repository.setActiveChild(id)
    }
}