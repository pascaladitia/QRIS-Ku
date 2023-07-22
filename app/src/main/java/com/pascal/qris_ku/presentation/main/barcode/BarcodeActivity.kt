package com.pascal.qris_ku.presentation.main.barcode

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.pascal.qris_ku.common.utils.Constant
import com.pascal.qris_ku.common.utils.formatToRupiah
import com.pascal.qris_ku.common.utils.isVisibility
import com.pascal.qris_ku.common.utils.showAlert
import com.pascal.qris_ku.common.wrapper.Resource
import com.pascal.qris_ku.data.entity.SaldoEntity
import com.pascal.qris_ku.data.entity.TransactionEntity
import com.pascal.qris_ku.databinding.ActivityBarcodeBinding
import com.pascal.qris_ku.databinding.DialogSuccessBinding
import com.pascal.qris_ku.presentation.main.viewModel.saldo.SaldoViewModel
import com.pascal.qris_ku.presentation.main.viewModel.transaction.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BarcodeActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: ActivityBarcodeBinding
    private lateinit var dialogSuccess: AlertDialog
    private var scannedValue = ""
    private var saldo: Int? = 0
    private var total: Int? = 0

    private val viewModelSaldo: SaldoViewModel by viewModels()
    private val viewModelTransaction: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        
        initView()
        attachObserve()
    }

    private fun initView() {
        setupPermissions()
        codeScanner()

        viewModelSaldo.getSaldo()
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, binding.camera)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    scannedValue = it.text
                    resultBarcode(scannedValue)
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "codeScanner: ${it.message}")
                }
            }

            codeScanner.startPreview()
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun attachObserve() {
        viewModelSaldo.getSaldoResult.asLiveData().observe(this@BarcodeActivity) {
            if (it is Resource.Success) {
                val input = it.data?.total
                saldo = if (!input.isNullOrBlank() && input != "null") input.toInt() else 0
            } else if (it is Resource.Error) {
                Log.d("TAG", "observeData: ${it.exception}")
            }
        }

        viewModelSaldo.insertSaldoResult.asLiveData().observe(this@BarcodeActivity) {
            if (it is Resource.Success) {
                Log.d("TAG", it.data.toString())
            } else if (it is Resource.Error) {
                Log.d("TAG", "observeData: ${it.exception}")
            }
        }

        viewModelTransaction.insertTransactionResult.asLiveData().observe(this@BarcodeActivity) {
            if (it is Resource.Success) {
                dialogSuccess()
            } else if (it is Resource.Error) {
                Log.d("TAG", "observeData: ${it.exception}")
            }
        }
    }

    private fun resultBarcode(scannedValue: String) {
        val parts = scannedValue.split(".")
        if (parts.size != 4) {
            showAlert(Constant.APP_NAME,"Format QR tidak sesuai")
        } else {
            val bankCode = parts[0]
            val merchantId = parts[1]
            val merchantName = parts[2]
            val transactionAmount = parts[3]

            if (bankCode.length != 3 || !bankCode.matches(Regex("[A-Z]+"))) {
                showAlert(Constant.APP_NAME, "Kode bank tidak sesuai")
            } else if (!merchantId.matches(Regex("ID\\d{8}"))) {
                showAlert(Constant.APP_NAME,"ID merchant tidak sesuai")
            } else if (merchantName.isEmpty()) {
                showAlert(Constant.APP_NAME,"Nama merchant tidak boleh kosong")
            } else if (!transactionAmount.matches(Regex("\\d+"))) {
                showAlert(Constant.APP_NAME,"Jumlah transaksi tidak sesuai")
            } else {
                println("Format string sesuai:")
                println("Kode Bank: $bankCode")
                println("ID Merchant: $merchantId")
                println("Nama Merchant: $merchantName")
                println("Jumlah Transaksi: $transactionAmount")

                showLayoutBayar(bankCode, merchantId, merchantName, transactionAmount)
            }
        }
    }

    private fun showLayoutBayar(bankCode: String, merchantId: String, merchantName: String, transactionAmount: String) {
        binding.apply {
            tvName.text = "$bankCode - $merchantName"
            tvId.text = merchantId
            tvTotal.text = formatToRupiah(transactionAmount.toDoubleOrNull() ?: 0.0)
            isVisibility(layoutTransaction, true)

            val bayar = saldo?.minus(transactionAmount.toInt())
            btnBayar.setOnClickListener {
                if (bayar!! < 0) {
                    showAlert(Constant.APP_NAME, "Saldo tidak cukup, harap TopUp!")
                } else {
                    total = transactionAmount.toInt()
                    viewModelSaldo.insertSaldo(SaldoEntity("1", bayar.toString()))
                    viewModelTransaction.insertTransaction(TransactionEntity(0, merchantId, bankCode, merchantName, transactionAmount))
                }
            }
        }
    }

    private fun dialogSuccess() {
        var dialogBuilder = AlertDialog.Builder(this)
        val bindDialog: DialogSuccessBinding = DialogSuccessBinding.inflate(layoutInflater)
        dialogBuilder.setView(bindDialog.root)
        dialogSuccess = dialogBuilder.create()
        dialogSuccess.show()
        dialogSuccess.setCancelable(false)

        bindDialog.tvTotal.text = formatToRupiah(total?.toDouble() ?: 0.0)

        bindDialog.btnBack.setOnClickListener {
            dialogSuccess.dismiss()
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }


    companion object {
        private const val CAMERA_REQ = 101
    }
}