package com.aplikasistunting.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child")
data class ChildEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val namaAnak: String,
    val umur: Int,
    val jenisKelamin: String,
    val parentId: Int,
    val isActive: Boolean = false
)