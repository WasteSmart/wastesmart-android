package com.frxcl.wastesmart.ui.activity.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityMainBinding
import com.frxcl.wastesmart.ui.activity.encyclopedia.EncyclopediaActivity
import com.frxcl.wastesmart.ui.activity.profile.ProfileActivity
import com.frxcl.wastesmart.ui.activity.quiz.QuizStartActivity
import com.frxcl.wastesmart.ui.activity.scan.CameraActivity
import com.frxcl.wastesmart.ui.activity.scan.CameraActivity.Companion.CAMERAX_RESULT
import com.frxcl.wastesmart.ui.activity.scan.ScanResultActivity
import com.frxcl.wastesmart.util.SettingPreferences
import com.frxcl.wastesmart.util.dataStore
import com.frxcl.wastesmart.viewmodel.MainViewModel
import com.frxcl.wastesmart.viewmodel.MainViewModelFactory
import com.frxcl.wastesmart.viewmodel.SettingViewModel
import com.frxcl.wastesmart.viewmodel.SettingViewModelFactory
import com.yalantis.ucrop.UCrop
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usn : String

    private val handler = Handler()
    private val checkInterval: Long = 5000

    private val mainfactory: MainViewModelFactory = MainViewModelFactory.getInstance()
    private val mainviewModel: MainViewModel by viewModels {
        mainfactory
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(this.dataStore)
        val settingviewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        val moveToEncyclopedia = Intent(this, EncyclopediaActivity::class.java)
        val moveToProfile = Intent(this, ProfileActivity::class.java)
        val moveToQuiz = Intent(this, QuizStartActivity::class.java)

        settingviewModel.getUserName().observe(this) { username: String? ->
            usn = username.toString().split(" ").take(2).joinToString(" ")
            moveToProfile.putExtra("name", username)
            binding.textViewGreeting.text = "Halo, $usn"
        }

        loadUserPfp()

        binding.apply {
            imageProfileBtn.setOnClickListener{
                startActivity(moveToProfile)
            }
            imageButtonCamera.setOnClickListener{
                if (hasCameraPermission()) {
                    if (isConnectionOk(this@MainActivity)) {
                        startCameraX()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Fitur ini membutuhkan koneksi internet.", Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    requestCameraPermissions()
                    if (!hasCameraPermission()) {
                        Toast.makeText(
                            this@MainActivity,
                            "Fitur ini membutuhkan izin kamera.", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            imageButtonGallery.setOnClickListener{
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (isConnectionOk(this@MainActivity)) {
                        startGallery()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Fitur ini membutuhkan koneksi internet.", Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    if (hasFilePermission()) {
                        if (isConnectionOk(this@MainActivity)) {
                            startGallery()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Fitur ini membutuhkan koneksi internet.", Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        requestFileWritePermission()
                        requestFileReadPermission()
                        if (!hasFilePermission()) {
                            Toast.makeText(
                                this@MainActivity,
                                "Fitur ini membutuhkan izin membaca penyimpanan.", Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
            constraintLayout3.setOnClickListener{
                if (isConnectionOk(this@MainActivity)) {
                    startActivity(moveToQuiz)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Fitur ini membutuhkan koneksi internet.", Toast.LENGTH_LONG
                    ).show()
                }
            }
            constraintLayout4.setOnClickListener{
                if (isConnectionOk(this@MainActivity)) {
                    startActivity(moveToEncyclopedia)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Fitur ini membutuhkan koneksi internet.", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        if (isConnectionOk(this)) {
            getFunFact()
        } else {
            checkInternetConnection()
            Toast.makeText(
                this,
                "Periksa koneksi internet anda.", Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getFunFact() {
        val animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fade_in_medium)
        binding.constraintLayout7.visibility = View.GONE
        mainviewModel.getFunFacts()
        mainviewModel.funFactsData.observe(this) { result ->
            result?.let {
                val funFacts = it[Random.nextInt(0, result.size)]
                binding.apply {
                    textViewFF.visibility = View.VISIBLE
                    textViewFFList.visibility = View.VISIBLE
                    textViewFFList.text = funFacts
                    constraintLayout7.visibility = View.VISIBLE
                    constraintLayout7.startAnimation(animation)
                }
            }
        }
    }

    private fun loadUserPfp() {
        val fileName = "user_pfp.png"
        val directory = getDir("images", Context.MODE_PRIVATE)
        val file = File(directory, fileName)

        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            Glide.with(this)
                .load(bitmap)
                .circleCrop()
                .into(binding.imageProfileBtn)
        }
    }

    private fun requestCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
        }
    }

    private fun requestFileReadPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        }
    }

    private fun requestFileWritePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasFilePermission(): Boolean {
        val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        val read =  ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        return write && read
    }

    private fun startCameraX() {
        val moveToCamera = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(moveToCamera)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            val currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val currentImageUri = result.data?.data as Uri
            startCrop(currentImageUri)
        }
    }

    private fun startCrop(uri: Uri) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        val destinationUri = Uri.fromFile(File(cacheDir, "imageScan${dateFormat.format(date)}"))
        uri.let {
            UCrop.of(it, destinationUri)
                .withAspectRatio(3f, 4f)
                .withMaxResultSize(1200, 1200)
                .withOptions(UCrop.Options().apply {
                    setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
                    setStatusBarColor(
                        ContextCompat.getColor(this@MainActivity,
                            R.color.colorPrimary
                        ))
                    setActiveControlsWidgetColor(
                        ContextCompat.getColor(this@MainActivity,
                            R.color.colorSecondary
                        ))
                })
                .start( this)
        }
    }

    private fun checkInternetConnection() {
        handler.post(object : Runnable {
            override fun run() {
                if (isConnectionOk(this@MainActivity)) {
                    recreate() // Reload the activity
                } else {
                    handler.postDelayed(this, checkInterval)
                }
            }
        })
    }

    @SuppressLint("ServiceCast", "ObsoleteSdkInt")
    fun isConnectionOk(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork?.let {
                connectivityManager.getNetworkCapabilities(it)
            }
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            val moveToResult = Intent(this, ScanResultActivity::class.java)
            moveToResult.putExtra("scanImageUri",resultUri)
            startActivity(moveToResult)
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Toast.makeText(
                this,
                "Kesalahan $cropError .", Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}