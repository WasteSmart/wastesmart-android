package com.frxcl.wastesmart.ui.activity.scan

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.data.remote.response.ResultItem
import com.frxcl.wastesmart.data.remote.retrofit.ApiConfig
import com.frxcl.wastesmart.databinding.ActivityScanResultBinding
import com.frxcl.wastesmart.ui.activity.scan.CameraActivity.Companion.EXTRA_CAMERAX_IMAGE
import com.frxcl.wastesmart.util.reduceFileImage
import com.frxcl.wastesmart.util.uriToFile
import com.frxcl.wastesmart.viewmodel.MainViewModel
import com.frxcl.wastesmart.viewmodel.MainViewModelFactory
import cz.msebera.android.httpclient.HttpException
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import kotlin.random.Random

class ScanResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanResultBinding
    private lateinit var imageUri: Uri

    private val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
    private val viewModel: MainViewModel by viewModels {
        factory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageUri = intent.getParcelableExtra("scanImageUri")!!

        binding.apply {
            backBtn.setOnClickListener{
                onBackPressed()
            }
            imageUri.let {
                imageViewScan.setImageURI(it)
            }
        }

        if (isConnectionOk(this)) {
            uploadWasteImage(imageUri)
        } else {
            Toast.makeText(
                this@ScanResultActivity,
                "Periksa koneksi internet anda.", Toast.LENGTH_LONG
            ).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun uploadWasteImage(imageUri: Uri){
        setLoading(true)
        val imageFile = uriToFile(imageUri, this).reduceFileImage()

        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )

        lifecycleScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val successResponse = apiService.uploadWasteImage(multipartBody)
                with(successResponse.result){
                    val resultOne = successResponse.result!![0]
                    val resultTwo = successResponse.result[1]
                    val resultThree = successResponse.result[2]

                    binding.apply {
                        if (resultOne != null) {
                            wasteCategoryTextView.text = resultOne.name!!.uppercase()
                            titleTop1.text = resultOne.name
                            progressBarTop1.progress = resultOne.percentage!!
                            confidentTop1.text = resultOne.percentage.toString() + "%"
                        }
                        if (resultTwo != null) {
                            titleTop2.text = resultTwo.name
                            progressBarTop2.progress = resultTwo.percentage!!
                            confidentTop2.text = resultTwo.percentage.toString()  + "%"
                        }
                        if (resultThree != null) {
                            titleTop3.text = resultThree.name
                            progressBarTop3.progress = resultThree.percentage!!
                            confidentTop3.text = resultThree.percentage.toString()  + "%"
                        }
                    }
                    setLoading(false)
                    showTips(resultOne!!.name!!)
                }

            } catch (e: HttpException) {
                Toast.makeText(
                    this@ScanResultActivity,
                    "Gagal memuat hasil.", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showTips(p1: String) {
        setLoading2(true)
        viewModel.getTips()
        viewModel.tipsData.observe(this) { result ->
            val tips: Map<String, String> = mapOf(
                "biological" to "${result?.tips?.sisaMakanan}",
                "plastic" to "${result?.tips?.plastik}",
                "metal" to "${result?.tips?.logam}",
                "glass" to "${result?.tips?.kaca}",
                "paper" to "${result?.tips?.kertas}",
                "cardboard" to "${result?.tips?.kardus}",
                "clothes" to "${result?.tips?.baju}",
                "shoes" to "${result?.tips?.sepatu}",
                "battery" to "${result?.tips?.baterai}"
            )

            when (p1) {
                "biological" -> binding.textViewTips.text = tips["biological"]
                "plastic" -> binding.textViewTips.text = tips["plastic"]
                "metal" -> binding.textViewTips.text = tips["metal"]
                "glass" -> binding.textViewTips.text = tips["glass"]
                "paper" -> binding.textViewTips.text = tips["paper"]
                "cardboard" -> binding.textViewTips.text = tips["cardboard"]
                "clothes" -> binding.textViewTips.text = tips["clothes"]
                "shoes" -> binding.textViewTips.text = tips["shoes"]
                "battery" -> binding.textViewTips.text = tips["battery"]
                else -> {
                    binding.textViewTips.text = tips.entries.random().toString()
                }
            }
            if (result != null) {
                setLoading2(false)
            }
        }
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

    fun setLoading(p1: Boolean) {
        if (p1) {
            binding.apply {
                progressBarLoading.visibility = View.VISIBLE
                wasteCategoryTextView.visibility = View.GONE
                titleTop1.visibility = View.GONE
                confidentTop1.visibility = View.GONE
                progressBarTop1.visibility = View.GONE
                titleTop2.visibility = View.GONE
                confidentTop2.visibility = View.GONE
                progressBarTop2.visibility = View.GONE
                titleTop3.visibility = View.GONE
                confidentTop3.visibility = View.GONE
                progressBarTop3.visibility = View.GONE
            }
        } else {
            binding.apply {
                progressBarLoading.visibility = View.GONE
                wasteCategoryTextView.visibility = View.VISIBLE
                titleTop1.visibility = View.VISIBLE
                confidentTop1.visibility = View.VISIBLE
                progressBarTop1.visibility = View.VISIBLE
                titleTop2.visibility = View.VISIBLE
                confidentTop2.visibility = View.VISIBLE
                progressBarTop2.visibility = View.VISIBLE
                titleTop3.visibility = View.VISIBLE
                confidentTop3.visibility = View.VISIBLE
                progressBarTop3.visibility = View.VISIBLE
            }
        }
    }
    fun setLoading2(p1: Boolean) {
        if (p1) {
            binding.apply {
                progressBarLoading2.visibility = View.VISIBLE
                textView29.visibility = View.GONE
                textViewTips.visibility = View.GONE
            }
        } else {
            binding.apply {
                binding.progressBarLoading2.visibility = View.GONE
                textView29.visibility = View.VISIBLE
                textViewTips.visibility = View.VISIBLE
            }
        }
    }
}