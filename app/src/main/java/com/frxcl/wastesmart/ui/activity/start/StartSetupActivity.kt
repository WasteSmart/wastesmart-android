package com.frxcl.wastesmart.ui.activity.start

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityStartBinding
import com.frxcl.wastesmart.databinding.ActivityStartSetupBinding
import com.frxcl.wastesmart.ui.activity.home.MainActivity
import com.frxcl.wastesmart.util.SettingPreferences
import com.frxcl.wastesmart.util.dataStore
import com.frxcl.wastesmart.viewmodel.MainViewModel
import com.frxcl.wastesmart.viewmodel.MainViewModelFactory

class StartSetupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartSetupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStartSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val moveToSetup = Intent(this, StartGuideActivity::class.java)

        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        binding.apply {
            imageView.startAnimation(fadeInAnimation)
            startBtn.setOnClickListener{
                startActivity(moveToSetup)
            }
        }
    }
}