package com.pascal.qris_ku.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saldo")
data class SaldoEntity(
    @PrimaryKey val id: String,
    val total: String
)