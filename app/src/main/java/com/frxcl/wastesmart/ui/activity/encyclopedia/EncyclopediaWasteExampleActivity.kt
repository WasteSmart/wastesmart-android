package com.frxcl.wastesmart.ui.activity.encyclopedia

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityEncyclopediaWasteExampleBinding

class EncyclopediaWasteExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEncyclopediaWasteExampleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEncyclopediaWasteExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (isConnectionOk(this)) {
            getData()
        } else {
            Toast.makeText(
                this,
                "Periksa koneksi internet anda.", Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getData() {
        setLoading(true)

        val title = intent.getStringExtra("titleExa")
        val desc = intent.getStringExtra("descExa")
        val imageUrl = intent.getStringExtra("imageUrlExa")

        binding.apply {
            textViewTitleExa.text = title
            Glide.with(this@EncyclopediaWasteExampleActivity)
                .load(imageUrl)
                .into(imageViewWaste)
            textViewDesc.text = desc
            backBtn.setOnClickListener{onBackPressed()}
        }
        setLoading(false)
    }

    @SuppressLint("ServiceCast", "ObsoleteSdkInt")
    fun isConnectionOk(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork?.let {
                connectivityManager.getNetworkCapabilities(it)
            }
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    private fun setLoading(p1: Boolean) {
        binding.apply {
            if (p1) {
                progressBar.visibility = View.VISIBLE
                imageViewWaste.visibility = View.GONE
                textViewDesc.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                imageViewWaste.visibility = View.VISIBLE
                textViewDesc.visibility = View.VISIBLE
            }
        }
    }
}