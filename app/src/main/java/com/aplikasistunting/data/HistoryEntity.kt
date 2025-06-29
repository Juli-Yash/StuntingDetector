package com.aplikasistunting.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tanggal: String,
    val nama: String,
    val statusStunting: String,
    val statusUnderweight: String,
    val statusWasting: String
)