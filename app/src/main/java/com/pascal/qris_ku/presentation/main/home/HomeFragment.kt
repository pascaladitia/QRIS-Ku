package com.pascal.qris_ku.presentation.main.home

import android.app.AlertDialog
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.pascal.qris_ku.common.base.BaseFragment
import com.pascal.qris_ku.common.utils.Constant
import com.pascal.qris_ku.common.utils.formatToRupiah
import com.pascal.qris_ku.common.utils.isVisibility
import com.pascal.qris_ku.common.utils.showAlert
import com.pascal.qris_ku.common.wrapper.Resource
import com.pascal.qris_ku.data.entity.SaldoEntity
import com.pascal.qris_ku.data.entity.TransactionEntity
import com.pascal.qris_ku.databinding.DialogTopupBinding
import com.pascal.qris_ku.databinding.FragmentHomeBinding
import com.pascal.qris_ku.presentation.adapter.AdapterTransaction
import com.pascal.qris_ku.presentation.main.viewModel.saldo.SaldoViewModel
import com.pascal.qris_ku.presentation.main.viewModel.transaction.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {

    private val viewModelSaldo: SaldoViewModel by viewModels()
    private val viewModelTransaction: TransactionViewModel by viewModels()
    private lateinit var dialogTopUp: AlertDialog
    private var saldo: Int? = 0

    override fun initView() {
        onView()
        onClick()
    }

    private fun onView() {
        viewModelSaldo.getSaldo()
        viewModelTransaction.getTransaction()
    }

    private fun onClick() {
        with(binding) {
            btnTopUp.setOnClickListener {
                dialogTopUp()
            }
        }
    }

    override fun observeData() {
        lifecycleScope.apply {
            launchWhenStarted {
                viewModelSaldo.getSaldoResult.collect {
                    when (it) {
                        is Resource.Empty -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            showSaldo(it.data)
                            Log.d("TAG", "observeData: ${it.data}")
                        }
                        is Resource.Error -> {
                            context?.showAlert(Constant.ERROR_MESSAGE, it.message.toString())
                            Log.d("TAG", "observeData: ${it.exception} or ${it.message}")
                        }
                    }
                }
            }

            viewModelSaldo.insertSaldoResult.asLiveData().observe(requireActivity()) {
                if (it is Resource.Success) {
                    viewModelSaldo.getSaldo()
                } else if (it is Resource.Error) {
                    Log.d("TAG", "observeData: ${it.exception}")
                }
            }

            launchWhenStarted {
                viewModelTransaction.getTransactionResult.collect {
                    when (it) {
                        is Resource.Empty -> {}
                        is Resource.Loading -> {
                            showLoading(true)
                        }
                        is Resource.Success -> {
                            showLoading(false)
                            showTransaction(it.data)
                            Log.d("TAG", "observeData: ${it.data}")
                        }
                        is Resource.Error -> {
                            showLoading(false)
                            context?.showAlert(Constant.ERROR_MESSAGE, it.message.toString())
                            Log.d("TAG", "observeData: ${it.exception} or ${it.message}")
                        }
                    }
                }
            }
        }
    }

    private fun showSaldo(data: SaldoEntity?) {
        val input = data?.total
        val amount = input?.toDoubleOrNull()
        saldo = if (!input.isNullOrBlank() && input != "null") input.toInt() else 0

        if (amount != null) {
            val formattedAmount = formatToRupiah(amount)
            if (formattedAmount != null) {
                binding.tvSaldo.text = formattedAmount
            }
        } else {
            binding.tvSaldo.text = "Rp. 0"
        }
    }

    private fun showTransaction(data: List<TransactionEntity>?) {
        binding.apply {
            if (!data.isNullOrEmpty()) {
                isVisibility(imgEmpty, false)
                isVisibility(rvTransaction, true)

                val adapter = AdapterTransaction(data, object : AdapterTransaction.OnClickListener {
                    override fun detail(item: TransactionEntity) {}
                })
                binding.rvTransaction.adapter = adapter
            }
        }
    }

    override fun showLoading(isVisible: Boolean) {
    }

    private fun dialogTopUp() {
        var dialogBuilder = AlertDialog.Builder(requireContext())
        val bindDialog: DialogTopupBinding = DialogTopupBinding.inflate(layoutInflater)
        dialogBuilder.setView(bindDialog.root)
        dialogTopUp = dialogBuilder.create()
        dialogTopUp.show()
        dialogTopUp.setCancelable(true)

        bindDialog.apply {
            btnTopUp.setOnClickListener {
                if (etTopup.text.toString().isEmpty()) {
                    etTopup.error = "Topup tidak boleh kosong"
                } else if (etTopup.text.toString().toInt() <= 0) {
                    etTopup.error = "Topup tidak boleh 0"
                } else {
                    val topUp = saldo?.plus(etTopup.text.toString().toInt())
                    viewModelSaldo.insertSaldo(SaldoEntity("1", topUp.toString()))
                    dialogTopUp.dismiss()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModelSaldo.getSaldo()
        viewModelTransaction.getTransaction()
    }
}