package com.frxcl.wastesmart.ui.activity.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frxcl.wastesmart.data.Repository
import com.frxcl.wastesmart.data.remote.response.QuizItem

class QuizViewModel(private val repository: Repository): ViewModel() {
    private val _questions = MutableLiveData<List<QuizItem>>()
    val questions: LiveData<List<QuizItem>> get() = _questions

    private var currentQuestionNo = 0
    private var userTrueAnswers: Int = 0
    private val userAnswers = mutableListOf<String>()

    fun getQuestions() {
        repository.getQuiz { result ->
            _questions.value = result.quiz?.sortedBy { it?.id }?.take(5) as List<QuizItem>
        }
    }

    fun nextQuestion(selectedAnswer: String) {
        userAnswers.add(selectedAnswer)
        currentQuestionNo++
    }

    fun getCurrentQuestion(): QuizItem? {
        return questions.value?.get(currentQuestionNo)
    }

    fun isQuizFinished(): Boolean {
        return currentQuestionNo > (questions.value?.size!! - 1)
    }

    fun isLastQuestion(): Boolean {
        return currentQuestionNo == (questions.value?.size!! - 1)
    }

    fun getUserAnswers(): List<String> {
        return userAnswers
    }

    fun isTrueAnswers(answers: String) {
        if (answers == questions.value!![currentQuestionNo].question.toString()) {
            userTrueAnswers++
        }
    }

    fun getTrueAnswers(): Int {
        for (i in 0 until questions.value!!.size)
            if (userAnswers[i] == questions.value!![i].answer!!.text) {
                println("bener")
                userTrueAnswers++
            } else {
                println("salah")
            }
        return userTrueAnswers
    }

    fun getTotalQuestion(): Int {
        return currentQuestionNo
    }
}