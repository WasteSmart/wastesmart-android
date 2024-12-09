package com.frxcl.wastesmart.ui.activity.start

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityStartGuideOneBinding
import com.frxcl.wastesmart.ui.activity.profile.SetProfileActivity

class StartGuideOneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartGuideOneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStartGuideOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val moveToTwo = Intent(this, StartGuideTwoActivity::class.java)

        binding.apply {
            backBtn.setOnClickListener{
                onBackPressed()
            }
            startBtn.setOnClickListener {
                startActivity(moveToTwo)
            }
        }

    }
}