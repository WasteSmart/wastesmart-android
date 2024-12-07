package com.frxcl.wastesmart.ui.activity.encyclopedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityEncyclopediaWasteExampleBinding

class EncyclopediaWasteExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEncyclopediaWasteExampleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEncyclopediaWasteExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val factory: EncyclopediaViewModelFactory = EncyclopediaViewModelFactory.getInstance(this)
        val viewModel: EncyclopediaViewModel by viewModels {
            factory
        }


        val title = intent.getStringExtra("titleExa")
        val desc = intent.getStringExtra("descExa")
        val imageUrl = intent.getStringExtra("imageUrlExa")

        binding.apply {
            textViewTitleExa.text = title
            Glide.with(this@EncyclopediaWasteExampleActivity)
                .load(imageUrl)
                .into(imageViewWaste)
            textViewDesc.text = desc
            backBtn.setOnClickListener{onBackPressed()}
        }
    }
}