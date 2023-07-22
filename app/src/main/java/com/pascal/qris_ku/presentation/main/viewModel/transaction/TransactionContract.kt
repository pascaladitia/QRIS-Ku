package com.pascal.qris_ku.presentation.main.viewModel.transaction

import com.pascal.qris_ku.common.wrapper.Resource
import com.pascal.qris_ku.data.entity.TransactionEntity
import kotlinx.coroutines.flow.StateFlow

interface TransactionContract {
    val getTransactionResult: StateFlow<Resource<List<TransactionEntity>>>
    fun getTransaction()

    val getDetailTransactionResult: StateFlow<Resource<TransactionEntity>>
    fun getDetailTransaction(id: String)

    val insertTransactionResult: StateFlow<Resource<Unit>>
    fun insertTransaction(transaction: TransactionEntity)
}