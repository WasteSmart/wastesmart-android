package com.frxcl.wastesmart.ui.activity.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityMainBinding
import com.frxcl.wastesmart.ui.activity.encyclopedia.EncyclopediaActivity
import com.frxcl.wastesmart.ui.activity.encyclopedia.EncyclopediaWasteExampleActivity
import com.frxcl.wastesmart.ui.activity.profile.ProfileActivity
import com.frxcl.wastesmart.ui.activity.quiz.QuizActivity
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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainfactory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
        val mainviewModel: MainViewModel by viewModels {
            mainfactory
        }

        val pref = SettingPreferences.getInstance(this.dataStore)
        val settingviewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        val moveToEncyclopedia = Intent(this, EncyclopediaActivity::class.java)
        val moveToProfile = Intent(this, ProfileActivity::class.java)
        val moveToQuiz = Intent(this, QuizActivity::class.java)

        settingviewModel.getUserName().observe(this) { username: String? ->
            usn = username!!
            moveToProfile.putExtra("name", usn)
            binding.textViewGreeting.text = "Halo, $username"
        }

        binding.apply {
            imageProfileBtn.setOnClickListener{
                startActivity(moveToProfile)
            }
            imageButtonCamera.setOnClickListener{
                startCameraX()
            }
            imageButtonGallery.setOnClickListener{
                startGallery()
            }
            constraintLayout3.setOnClickListener{
                startActivity(moveToQuiz)
            }
            constraintLayout4.setOnClickListener{
                startActivity(moveToEncyclopedia)
            }
        }

        val fileName = "cropped_image.png"
        val bitmap = loadImage(fileName)
        setImage(bitmap)

        mainviewModel.getFunFacts()
        mainviewModel.funFactsData.observe(this, Observer { result ->
            val funFacts = result?.get(Random.nextInt(0, result.size))
            binding.textViewFFList.text = funFacts
        })

        requestCameraPermissions()

    }

    private fun loadImage(fileName: String): Bitmap? {
        try {
            val fileInputStream = openFileInput(fileName)
            return BitmapFactory.decodeStream(fileInputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun setImage(bitmap: Bitmap?) {
        if (bitmap != null) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            val moveToResult = Intent(this, ScanResultActivity::class.java)
            moveToResult.putExtra("scanImageUri",resultUri)
            startActivity(moveToResult)
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}