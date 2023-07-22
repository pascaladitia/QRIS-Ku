package com.pascal.qris_ku.common.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.widget.Toast
import com.pascal.qris_ku.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.showAlert(title: String, msg: String) {
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(msg)
        setIcon(R.drawable.ic_launcher_background)
        setCancelable(false)

        setPositiveButton("Ok") { dialogInterface, i ->
            dialogInterface?.dismiss()
        }
    }.show()
}

fun Activity.initPermission(): Boolean {
    return if (SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 1
        )
        return true
    } else {
        return false
    }
}

fun validation(txt: String): String {
    var result = ""
    if (txt != "null" && txt != null && txt != "") {
        result = txt
    } else {
        result = "-"
    }
    return result
}

fun isVisibility(view: View, visible: Boolean) {
    if (visible) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

fun formatTime(time: String): String {
    return if (time != "" && time != "-" && time != null) {
        val sdf = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.ENGLISH
        )
        val parsedDate: Date = sdf.parse(time)
        val date = SimpleDateFormat("dd-MM-yyyy")
        val result = date.format(parsedDate).toString()
        return result
    } else {
        return time
    }
}

fun formatToRupiah(amount: Double): String? {
    if (amount < 0) {
        println("Angka tidak boleh negatif.")
        return null
    }

    val symbols = DecimalFormatSymbols()
    symbols.groupingSeparator = '.'
    symbols.decimalSeparator = ','
    val df = DecimalFormat("#,##0.00", symbols)

    return "Rp " + df.format(amount)
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

