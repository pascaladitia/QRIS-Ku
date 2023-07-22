package com.pascal.qris_ku.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pascal.qris_ku.data.entity.SaldoEntity
import com.pascal.qris_ku.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalService {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transactionEntity: TransactionEntity)

    @Query("SELECT * FROM `transaction`")
    fun getTransaction(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM `transaction` WHERE keyId = :id")
    fun getDetailTransaction(id: String): Flow<TransactionEntity>

    @Query("DELETE FROM `transaction`")
    fun deleteTransaction()

    //saldo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaldo(saldoEntity: SaldoEntity)

    @Query("SELECT * FROM saldo")
    fun getSaldo(): Flow<SaldoEntity>

    @Query("DELETE FROM saldo")
    fun deleteSaldo()
}