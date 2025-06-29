package com.aplikasistunting.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parents")
data class ParentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val email: String,
    val password: String // disimpan dalam bentuk hash di versi lebih lanjut
)