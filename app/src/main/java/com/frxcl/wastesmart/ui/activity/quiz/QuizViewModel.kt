package com.frxcl.wastesmart.ui.activity.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frxcl.wastesmart.data.Repository
import com.frxcl.wastesmart.data.remote.response.QuizItem

class QuizViewModel(private val repository: Repository): ViewModel() {
    private val _quizData = MutableLiveData<List<QuizItem>?>()
    val quizData: LiveData<List<QuizItem>?> get() = _quizData

    fun getQuiz() {
        repository.getQuiz { result ->
            _quizData.value = result.quiz as List<QuizItem>?
        }
    }
}