package com.aplikasistunting.repository

import androidx.lifecycle.LiveData
import com.aplikasistunting.data.ChildDao
import com.aplikasistunting.data.ChildEntity

class ChildRepository(private val childDao: ChildDao) {

    fun getChildrenByParentId(parentId: Int): LiveData<List<ChildEntity>> {
        return childDao.getChildrenByParentId(parentId)
    }

    fun getActiveChild(): LiveData<ChildEntity?> {
        return childDao.getActiveChild()
    }

    suspend fun insert(child: ChildEntity) {
        childDao.insert(child)
    }

    suspend fun delete(child: ChildEntity) {
        childDao.delete(child)
    }

    suspend fun clearActiveStatus(parentId: Int) {
        childDao.clearActiveStatus(parentId)
    }

    suspend fun setActiveChild(id: Int) {
        childDao.setActiveChild(id)
    }
}
