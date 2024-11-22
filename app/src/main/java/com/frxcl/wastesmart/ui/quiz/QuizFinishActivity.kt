package com.frxcl.wastesmart.ui.quiz

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.frxcl.wastesmart.MainActivity
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityQuizFinishBinding
import kotlinx.coroutines.MainScope

class QuizFinishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizFinishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuizFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backToHome = Intent(this, MainActivity::class.java)
        binding.apply {
            homeBtn.setOnClickListener {
                startActivity(backToHome)
            }
        }
    }
}