package com.pascal.qris_ku.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val keyId: Int,
    val idTransaction: String,
    val bank: String,
    val name: String,
    val total: String
)