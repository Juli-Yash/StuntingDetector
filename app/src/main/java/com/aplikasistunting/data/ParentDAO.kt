package com.aplikasistunting.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ParentDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(parent: ParentEntity)

    @Query("SELECT * FROM parents WHERE email = :email LIMIT 1")
    suspend fun getParentByEmail(email: String): ParentEntity?

    @Query("SELECT * FROM parents WHERE id = :parentId LIMIT 1")
    suspend fun getParentById(parentId: Int): ParentEntity?

    @Query("SELECT * FROM parents WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): ParentEntity?
}
