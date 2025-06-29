package com.aplikasistunting.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ChildDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(child: ChildEntity)

    @Update
    suspend fun update(child: ChildEntity)

    @Delete
    suspend fun delete(child: ChildEntity)

    @Query("SELECT * FROM child")
    fun getAllChildren(): LiveData<List<ChildEntity>>

    @Query("UPDATE child SET isActive = 0")
    suspend fun clearActiveStatus()

    @Query("UPDATE child SET isActive = 1 WHERE id = :childId")
    suspend fun setActiveChild(childId: Int)

    @Query("SELECT * FROM child")
    suspend fun getAllChildrenOnce(): List<ChildEntity>

    @Query("SELECT * FROM child WHERE id = :childId LIMIT 1")
    suspend fun getChildById(childId: Int): ChildEntity?

    @Query("SELECT * FROM child WHERE isActive = 1 LIMIT 1")
    fun getActiveChild(): LiveData<ChildEntity?>

    @Query("SELECT * FROM child WHERE parentId = :parentId")
    fun getChildrenByParentId(parentId: Int): LiveData<List<ChildEntity>>

    @Query("SELECT * FROM child WHERE parentId = :parentId")
    suspend fun getChildrenByParentIdOnce(parentId: Int): List<ChildEntity>

    @Query("UPDATE child SET isActive = 0 WHERE parentId = :parentId")
    suspend fun clearActiveStatus(parentId: Int)
}