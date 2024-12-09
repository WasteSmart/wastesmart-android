package com.frxcl.wastesmart.ui.activity.quiz

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.data.remote.response.QuizItem
import com.frxcl.wastesmart.data.remote.response.QuizResponse
import com.frxcl.wastesmart.databinding.ActivityQuizBinding
import com.frxcl.wastesmart.ui.activity.encyclopedia.EncyclopediaViewModel
import com.frxcl.wastesmart.ui.activity.encyclopedia.EncyclopediaViewModelFactory

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val moveToStartQuiz = Intent(this, QuizStartActivity::class.java)
        binding.apply {
            backBtn.setOnClickListener{
                onBackPressed()
            }
            startQuizBtn.setOnClickListener {
                startActivity(moveToStartQuiz)
            }
        }

    }

}