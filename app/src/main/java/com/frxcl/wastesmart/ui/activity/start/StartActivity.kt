package com.frxcl.wastesmart.ui.activity.start

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.frxcl.wastesmart.ui.activity.home.MainActivity
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityStartBinding
import com.frxcl.wastesmart.util.SettingPreferences
import com.frxcl.wastesmart.util.dataStore
import com.frxcl.wastesmart.viewmodel.MainViewModel
import com.frxcl.wastesmart.viewmodel.MainViewModelFactory

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = SettingPreferences.getInstance(this.dataStore)
        val viewModel = ViewModelProvider(this, MainViewModelFactory(pref))[MainViewModel::class.java]

        val moveToHome = Intent(this, MainActivity::class.java)
        val moveToStart = Intent(this, StartSetupActivity::class.java)

        viewModel.getSetupState().observe(this@StartActivity) { isSetupDone: Boolean ->
            if (isSetupDone) {
                startActivity(moveToHome)
            } else {
                startActivity(moveToStart)
            }
        }

        enableEdgeToEdge()

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}