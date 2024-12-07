package com.frxcl.wastesmart.ui.activity.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var usn: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val moveToEditProfile = Intent(this, EditProfileActivity::class.java)
        val moveToChangeTheme = Intent(this, DisplayActivity::class.java)

        usn = intent.getStringExtra("name").toString()
        moveToEditProfile.putExtra("name", usn)

        binding.apply {
            textViewUsn.text = usn
            backBtn.setOnClickListener{onBackPressed()}
            textView2.setOnClickListener {
                startActivity(moveToEditProfile)
            }
            textView3.setOnClickListener {
                startActivity(moveToChangeTheme)
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
                .into(binding.imageViewPfp)
        }
    }
}