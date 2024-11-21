package com.frxcl.wastesmart

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.frxcl.wastesmart.databinding.ActivityEncyclopediaDetailBinding
import com.frxcl.wastesmart.databinding.ActivityMainBinding

class EncyclopediaDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEncyclopediaDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEncyclopediaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        binding.apply { 
            backBtn.setOnClickListener{onBackPressed()}
        }
    }
}