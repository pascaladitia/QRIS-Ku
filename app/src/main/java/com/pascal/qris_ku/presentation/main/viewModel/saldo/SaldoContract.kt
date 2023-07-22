package com.pascal.qris_ku.presentation.main.viewModel.saldo

import com.pascal.qris_ku.common.wrapper.Resource
import com.pascal.qris_ku.data.entity.SaldoEntity
import kotlinx.coroutines.flow.StateFlow

interface SaldoContract {
    val getSaldoResult: StateFlow<Resource<SaldoEntity>>
    fun getSaldo()

    val insertSaldoResult: StateFlow<Resource<Unit>>
    fun insertSaldo(Saldo: SaldoEntity)
}