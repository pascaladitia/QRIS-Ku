package com.pascal.qris_ku.presentation.main.viewModel.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.qris_ku.common.utils.Constant
import com.pascal.qris_ku.common.wrapper.Resource
import com.pascal.qris_ku.data.entity.TransactionEntity
import com.pascal.qris_ku.data.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val localRepository: LocalRepository
) : TransactionContract, ViewModel() {
    private val _getTransactionResult = MutableStateFlow<Resource<List<TransactionEntity>>>(Resource.Empty())
    override val getTransactionResult: StateFlow<Resource<List<TransactionEntity>>> = _getTransactionResult

    private val _getDetailTransactionResult = MutableStateFlow<Resource<TransactionEntity>>(Resource.Empty())
    override val getDetailTransactionResult: StateFlow<Resource<TransactionEntity>> = _getDetailTransactionResult

    private val _insertTransactionResult = MutableStateFlow< Resource<Unit>>(Resource.Empty())
    override val insertTransactionResult: StateFlow<Resource<Unit>> = _insertTransactionResult

    override fun getTransaction() {
        _getTransactionResult.value = Resource.Loading()
        viewModelScope.launch {
            localRepository.getTransaction()
                .collect {
                    try {
                        _getTransactionResult.value = Resource.Success(it.data)
                    } catch (e: Exception) {
                        _getTransactionResult.value = Resource.Error(
                            exception = e,
                            message = Constant.ERROR_MESSAGE
                        )
                    }
                }
        }
    }

    override fun getDetailTransaction(id: String) {
        _getDetailTransactionResult.value = Resource.Loading()
        viewModelScope.launch {
            localRepository.getDetailTransaction(id)
                .collect {
                    try {
                        _getDetailTransactionResult.value = Resource.Success(it.data)
                    } catch (e: Exception) {
                        _getDetailTransactionResult.value = Resource.Error(
                            exception = e,
                            message = Constant.ERROR_MESSAGE
                        )
                    }
                }
        }
    }

    override fun insertTransaction(transaction: TransactionEntity) {
        _insertTransactionResult.value = Resource.Loading()
        viewModelScope.launch {
            localRepository.insertTransaction(transaction)
                .collect {
                    try {
                        _insertTransactionResult.value = Resource.Success(it.data)
                    } catch (e: Exception) {
                        _insertTransactionResult.value = Resource.Error(
                            exception = e,
                            message = Constant.ERROR_MESSAGE
                        )
                    }
                }
        }
    }
}