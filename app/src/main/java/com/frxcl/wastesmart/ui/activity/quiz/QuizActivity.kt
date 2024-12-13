package com.frxcl.wastesmart.ui.activity.quiz

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityQuizBinding
import com.frxcl.wastesmart.ui.activity.home.MainActivity
import com.frxcl.wastesmart.ui.adapter.QuizResultListAdapter
import com.frxcl.wastesmart.util.SettingPreferences
import com.frxcl.wastesmart.util.dataStore
import com.frxcl.wastesmart.viewmodel.SettingViewModel
import com.frxcl.wastesmart.viewmodel.SettingViewModelFactory

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private val factory: QuizViewModelFactory = QuizViewModelFactory.getInstance()
    private val viewModel: QuizViewModel by viewModels {
        factory
    }

    private lateinit var questionTextView: TextView
    private lateinit var optionRadioGroup: RadioGroup
    private lateinit var nextButton: Button

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

        binding.apply {
            questionTextView = textViewQuestion
            optionRadioGroup = radioGroup
            nextButton = nextBtn
            closeQuizBtn.setOnClickListener {
                onBackPressed()
            }
        }

        if (isConnectionOk(this)) {
            setLoading(true)
            viewModel.getQuestions()

            setQuestion()
            nextButton.setOnClickListener { nextQuestion() }
        } else {
            Toast.makeText(
                this,
                "Periksa koneksi internet anda.", Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setQuestion() {
        viewModel.questions.observe(this) { q ->
            q.let {
                setLoading(false)
                if (q != null) {
                    val quiz = viewModel.getCurrentQuestion()
                    binding.apply {
                        questionTextView.text = quiz!!.question
                        option1.text = quiz.options?.get(0)?.text.toString()
                        option2.text = quiz.options?.get(1)?.text.toString()
                        option3.text = quiz.options?.get(2)?.text.toString()
                        option4.text = quiz.options?.get(3)?.text.toString()
                    }
                }
            }
        }
        binding.apply {
            option1.setOnClickListener { setRadioAnswer(true) }
            option2.setOnClickListener { setRadioAnswer(true) }
            option3.setOnClickListener { setRadioAnswer(true) }
            option4.setOnClickListener { setRadioAnswer(true) }
        }
    }

    private fun nextQuestion() {
            viewModel.questions.observe(this) {
                val selectedId = optionRadioGroup.checkedRadioButtonId
                if (selectedId != -1) {
                    val selectedRadioButton: RadioButton = findViewById(selectedId)
                    val selectedRadioText = selectedRadioButton.text.toString()

                    viewModel.isTrueAnswers(selectedRadioText)
                    viewModel.nextQuestion(selectedRadioText)
                    optionRadioGroup.clearCheck()

                    if (!viewModel.isQuizFinished()) {
                        setQuestion()
                        setRadioAnswer(false)
                        if (viewModel.isLastQuestion()) {
                            nextButton.text = "Selesai"
                        }
                    } else {
                        showQuizResult()
                    }
                }
            }
    }

    private fun setRadioAnswer(p1: Boolean) {
        nextButton.isEnabled = p1
    }

    @SuppressLint("SetTextI18n")
    private fun showQuizResult() {
        val animation = AnimationUtils.loadAnimation(this@QuizActivity, R.anim.fade_in_fast)
        val pref = SettingPreferences.getInstance(this.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getUserName().observe(this) { u ->
            val username = u.toString().split(" ").take(1).joinToString(" ")
            binding.textViewUser.text = "Selamat, $username!"
        }

        binding.apply {
            quizDisplayContainer.visibility = View.GONE
            closeQuizBtn.visibility = View.GONE
            textViewTitle.text = "Hasil Kuis"
            textViewTotal.text =  (" ${viewModel.getTrueAnswers()} / ${viewModel.getTotalQuestion()}")
            quizResultDisplayContainer.visibility = View.VISIBLE
            quizResultDisplayContainer.startAnimation(animation)
            homeBtn.setOnClickListener {
                val moveToHome = Intent(this@QuizActivity, MainActivity::class.java)
                startActivity(moveToHome)
                finish()
            }
            resultBtn.setOnClickListener {
                viewModel.questions.observe(this@QuizActivity) { q ->
                    binding.apply {
                        quizResultDisplayContainer.visibility = View.GONE
                        backQuizResultBtn.visibility = View.VISIBLE
                        backQuizResultBtn.setOnClickListener {
                            quizResultListContainer.visibility = View.GONE
                            backQuizResultBtn.visibility = View.GONE
                            quizResultDisplayContainer.visibility = View.VISIBLE
                            quizResultDisplayContainer.startAnimation(animation)
                        }
                        quizResultListContainer.visibility = View.VISIBLE
                        quizResultListContainer.startAnimation(animation)
                    }
                    binding.rvQuizResultList.apply {
                        layoutManager = LinearLayoutManager(this@QuizActivity)
                        setHasFixedSize(true)
                        val adapter = QuizResultListAdapter(q, viewModel.getUserAnswers())
                        rvQuizResultList.adapter = adapter
                    }
                }
            }
        }
    }

    private fun setLoading(p1: Boolean) {
        val animation = AnimationUtils.loadAnimation(this@QuizActivity, R.anim.fade_in_fast)
        if(p1) {
            binding.apply {
                progressBarQuizLoading.visibility = View.VISIBLE
                quizDisplayContainer.visibility = View.GONE
            }
        } else {
            binding.apply {
                progressBarQuizLoading.visibility = View.GONE
                quizDisplayContainer.visibility = View.VISIBLE
                quizDisplayContainer.startAnimation(animation)
            }
        }
    }

    @SuppressLint("ServiceCast", "ObsoleteSdkInt")
    fun isConnectionOk(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork?.let {
                connectivityManager.getNetworkCapabilities(it)
            }
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Quiz")
            .setMessage("Apakah Kamu yakin ingin keluar")
            .setPositiveButton("Ya") { _, _ -> super.onBackPressed() }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}