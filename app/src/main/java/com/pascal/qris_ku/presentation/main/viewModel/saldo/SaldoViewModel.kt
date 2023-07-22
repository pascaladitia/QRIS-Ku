package com.pascal.qris_ku.presentation.main.viewModel.saldo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.qris_ku.common.utils.Constant
import com.pascal.qris_ku.common.wrapper.Resource
import com.pascal.qris_ku.data.entity.SaldoEntity
import com.pascal.qris_ku.data.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaldoViewModel @Inject constructor(
    private val localRepository: LocalRepository
) : SaldoContract, ViewModel() {
    private val _getSaldoResult = MutableStateFlow<Resource<SaldoEntity>>(Resource.Empty())
    override val getSaldoResult: StateFlow<Resource<SaldoEntity>> = _getSaldoResult

    private val _insertSaldoResult = MutableStateFlow< Resource<Unit>>(Resource.Empty())
    override val insertSaldoResult: StateFlow<Resource<Unit>> = _insertSaldoResult

    override fun getSaldo() {
        _getSaldoResult.value = Resource.Loading()
        viewModelScope.launch {
            localRepository.getSaldo()
                .collect {
                    try {
                        _getSaldoResult.value = Resource.Success(it.data)
                    } catch (e: Exception) {
                        _getSaldoResult.value = Resource.Error(
                            exception = e,
                            message = Constant.ERROR_MESSAGE
                        )
                    }
                }
        }
    }

    override fun insertSaldo(Saldo: SaldoEntity) {
        _insertSaldoResult.value = Resource.Loading()
        viewModelScope.launch {
            localRepository.insertSaldo(Saldo)
                .collect {
                    try {
                        _insertSaldoResult.value = Resource.Success(it.data)
                    } catch (e: Exception) {
                        _insertSaldoResult.value = Resource.Error(
                            exception = e,
                            message = Constant.ERROR_MESSAGE
                        )
                    }
                }
        }
    }
}