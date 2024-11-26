package com.frxcl.wastesmart.ui.activity.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.frxcl.wastesmart.databinding.ActivityMainBinding
import com.frxcl.wastesmart.ui.activity.encyclopedia.EncyclopediaActivity
import com.frxcl.wastesmart.ui.activity.encyclopedia.EncyclopediaDetailActivity
import com.frxcl.wastesmart.ui.activity.profile.ProfileActivity
import com.frxcl.wastesmart.ui.activity.quiz.QuizActivity
import com.frxcl.wastesmart.ui.activity.scan.ScanResultActivity
import com.frxcl.wastesmart.util.SettingPreferences
import com.frxcl.wastesmart.util.dataStore
import com.frxcl.wastesmart.viewmodel.MainViewModel
import com.frxcl.wastesmart.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(this.dataStore)
        val viewModel = ViewModelProvider(this, MainViewModelFactory(pref))[MainViewModel::class.java]

        viewModel.getUserName().observe(this) { username: String? ->
            binding.textViewGreeting.text = "Halo, $username"
        }

        val intent = Intent(this, EncyclopediaActivity::class.java)
        val intentD = Intent(this, EncyclopediaDetailActivity::class.java)
        val moveToProfile = Intent(this, ProfileActivity::class.java)
        val moveToResult = Intent(this, ScanResultActivity::class.java)
        val moveToQuiz = Intent(this, QuizActivity::class.java)


        binding.apply {
            constraintLayout3.setOnClickListener{
                startActivity(intent)
            }
            imageProfileBtn.setOnClickListener{
                startActivity(moveToProfile)
            }
            constraintLayout1.setOnClickListener{
                startActivity(moveToResult)
            }
            constraintLayout2.setOnClickListener{
                startActivity(moveToQuiz)
            }
        }

        val fileName = "cropped_image.png"
        val bitmap = loadImage(fileName)
        setImage(bitmap)

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

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}