package com.pascal.qris_ku.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.pascal.qris_ku.R
import com.pascal.qris_ku.databinding.ActivityMainBinding
import com.pascal.qris_ku.presentation.main.barcode.BarcodeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initView()
    }

    private fun initView() {
        with(binding) {

            btnQuickCount.setOnClickListener {
                startActivity(Intent(this@MainActivity, BarcodeActivity::class.java))
            }

            navView.background = null
            navView.menu.getItem(2).isEnabled = false

            val navController = findNavController(R.id.nav_host_fragment_activity_bottom)
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
    }
}