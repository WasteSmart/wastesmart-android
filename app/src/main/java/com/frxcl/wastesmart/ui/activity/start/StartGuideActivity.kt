package com.frxcl.wastesmart.ui.activity.start

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityStartGuideBinding
import com.frxcl.wastesmart.ui.activity.profile.SetProfileActivity

class StartGuideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartGuideBinding
    private var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStartGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val view = findViewById<View>(android.R.id.content)
        val mLoadAnimation: Animation = AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_in)
        mLoadAnimation.duration = 2000
        view.startAnimation(mLoadAnimation)

        binding.apply {
            backBtn.setOnClickListener{
                counter--
                setContent()
            }
            startBtn.setOnClickListener {
                setContent()
                counter++
            }
        }
    }
    private fun setContent() {
        binding.apply {
            when (counter) {
                -1 -> {
                    image.setImageResource(R.drawable.camera_landing_page)
                    title.text = "Scan Sampah"
                    description.text = "Kenali kategori sampah lewat foto secara langsung dan akurat."
                }
                0 -> {
                    image.setImageResource(R.drawable.funfact_landing_page)
                    title.text = "Fun Fact"
                    description.text = "Dapatkan informasi unik dan menarik seputar sampah."
                }
                1 -> {
                    image.setImageResource(R.drawable.quiz_landing_page)
                    title.text = "Kuis"
                    description.text = "Permainan quiz yang menyenangkan untuk menguji pemahamanmu seputar sampah."
                }
                2 -> {
                    image.setImageResource(R.drawable.encyclopedia_landing_page)
                    title.text = "Ensiklopedia"
                    description.text = "Perluas pengetahuanmu lewat gudang wawasan seputar sampah."
                }
                3 -> {
                    val moveToSetProfile = Intent(this@StartGuideActivity, SetProfileActivity::class.java)
                    startActivity(moveToSetProfile)
                    counter--
                }
                else -> {
                    onBackPressed()
                }
            }
        }
    }
}