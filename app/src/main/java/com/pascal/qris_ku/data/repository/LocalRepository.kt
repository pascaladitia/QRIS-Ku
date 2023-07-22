package com.pascal.qris_ku.data.repository

import com.pascal.qris_ku.common.base.BaseRepository
import com.pascal.qris_ku.common.wrapper.Resource
import com.pascal.qris_ku.data.datasource.LocalDatasource
import com.pascal.qris_ku.data.entity.SaldoEntity
import com.pascal.qris_ku.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface LocalRepository {
    suspend fun insertTransaction(transactionEntity: TransactionEntity): Flow<Resource<Unit>>
    suspend fun getTransaction(): Flow<Resource<List<TransactionEntity>>>
    suspend fun getDetailTransaction(id: String): Flow<Resource<TransactionEntity>>
    suspend fun deleteTransaction(): Flow<Resource<Unit>>

    suspend fun insertSaldo(saldoEntity: SaldoEntity): Flow<Resource<Unit>>
    suspend fun getSaldo(): Flow<Resource<SaldoEntity>>
    suspend fun deleteSaldo(): Flow<Resource<Unit>>
}

class LocalRepositoryImpl @Inject constructor(
    private val localDatasource: LocalDatasource
) : LocalRepository, BaseRepository() {
    override suspend fun insertTransaction(transactionEntity: TransactionEntity): Flow<Resource<Unit>> = flow {
        emit(proceed { localDatasource.insertTransaction(transactionEntity) })
    }

    override suspend fun getTransaction(): Flow<Resource<List<TransactionEntity>>> = flow {
        emit(proceed { localDatasource.getTransaction().first() })
    }

    override suspend fun getDetailTransaction(id: String): Flow<Resource<TransactionEntity>> = flow {
        emit(proceed { localDatasource.getDetailTranscation(id).first() })
    }

    override suspend fun deleteTransaction(): Flow<Resource<Unit>> = flow {
        emit(proceed { localDatasource.deleteUser() })
    }

    override suspend fun insertSaldo(saldoEntity: SaldoEntity): Flow<Resource<Unit>> = flow {
        emit(proceed { localDatasource.insertSaldo(saldoEntity) })
    }

    override suspend fun getSaldo(): Flow<Resource<SaldoEntity>> = flow {
        emit(proceed { localDatasource.getSaldo().first() })
    }

    override suspend fun deleteSaldo(): Flow<Resource<Unit>> = flow {
        emit(proceed { localDatasource.deleteSaldo() })
    }
}