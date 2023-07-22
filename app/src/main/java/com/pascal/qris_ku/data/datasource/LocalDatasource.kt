package com.pascal.qris_ku.data.datasource

import com.pascal.qris_ku.data.dao.LocalService
import com.pascal.qris_ku.data.entity.SaldoEntity
import com.pascal.qris_ku.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalDatasource {
    suspend fun insertTransaction(transactionEntity: TransactionEntity)
    suspend fun getTransaction(): Flow<List<TransactionEntity>>
    suspend fun getDetailTranscation(id: String): Flow<TransactionEntity>
    suspend fun deleteUser()

    suspend fun insertSaldo(saldoEntity: SaldoEntity)
    suspend fun getSaldo(): Flow<SaldoEntity>
    suspend fun deleteSaldo()
}

class LocalDatasourceImpl @Inject constructor(
    private val localService: LocalService
): LocalDatasource {
    override suspend fun insertTransaction(transactionEntity: TransactionEntity) {
        return localService.insertTransaction(transactionEntity)
    }

    override suspend fun getTransaction(): Flow<List<TransactionEntity>> {
        return localService.getTransaction()
    }

    override suspend fun getDetailTranscation(id: String): Flow<TransactionEntity> {
        return localService.getDetailTransaction(id)
    }

    override suspend fun deleteUser() {
        return localService.deleteTransaction()
    }

    override suspend fun insertSaldo(saldoEntity: SaldoEntity) {
        return localService.insertSaldo(saldoEntity)
    }

    override suspend fun getSaldo(): Flow<SaldoEntity> {
        return localService.getSaldo()
    }

    override suspend fun deleteSaldo() {
        return localService.deleteSaldo()
    }
}