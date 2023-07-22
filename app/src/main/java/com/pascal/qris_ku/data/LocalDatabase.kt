package com.pascal.qris_ku.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pascal.qris_ku.data.dao.LocalService
import com.pascal.qris_ku.data.entity.SaldoEntity
import com.pascal.qris_ku.data.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class, SaldoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun localService(): LocalService
}