package com.frxcl.wastesmart.ui.activity.quiz

import android.os.Bundle
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.data.remote.response.QuizItem
import com.frxcl.wastesmart.databinding.ActivityQuizStartBinding

class QuizStartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizStartBinding
    private val factory: QuizViewModelFactory = QuizViewModelFactory.getInstance(this)
    private val viewModel: QuizViewModel by viewModels {
        factory
    }
    private lateinit var quiz: List<QuizItem> = listOf()
    private var userAnswers = emptyArray<String>()
    private var counter: Int = 0
    private var totalQuestion: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuizStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.getQuiz()
        viewModel.quizData.observe(this, Observer { list ->
            quiz = list ?: emptyList()

        })

        println("kuis data di luar viewmodel $quiz")

        binding.apply {
            closeQuizBtn.setOnClickListener {
                onBackPressed()
            }
            nextBtn.setOnClickListener {
                saveAnswer()

            }
        }
    }

    private fun setQuestion(question: List<QuizItem>?) {
        binding.apply {
            textViewQuestion.text = question!![counter].question
            option1.text = question!![counter].options!![0]!!.text.toString()
            option2.text = question!![counter].options!![1]!!.text.toString()
            option3.text = question!![counter].options!![2]!!.text.toString()
            option4.text = question!![counter].options!![3]!!.text.toString()
        }
        clearOption()
    }

    private fun saveAnswer() {
        binding.apply {
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                val selectedRadioButton: RadioButton = findViewById(checkedId)
                val selectedRadioText = selectedRadioButton.text.toString()
                selectedRadioText.also { userAnswers[counter] = it }
            }
        }
        viewModel.quizData.observe(this, Observer { result ->
            if (counter <= totalQuestion) {
                println("jawaban " + userAnswers[counter])
                counter++
            } else {
                binding.nextBtn.text = "Selesai"
            }
        })
    }

    private fun clearOption() {
        binding.radioGroup.clearCheck()
    }

    private fun setTotalQuestion(p1: Int): Array<String?> {
        return userAnswers.copyOf(p1)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Quiz")
            .setMessage("Apakah Kamu yakin ingin kembali")
            .setPositiveButton("Ya") { dialog, which -> super.onBackPressed() }
            .setNegativeButton("Tidak") { dialog, which -> dialog.dismiss() }
            .show()
    }
}