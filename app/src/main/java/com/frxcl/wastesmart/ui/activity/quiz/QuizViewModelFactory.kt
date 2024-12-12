package com.frxcl.wastesmart.ui.activity.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.frxcl.wastesmart.data.Repository
import com.frxcl.wastesmart.di.Injection

class QuizViewModelFactory private constructor(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: QuizViewModelFactory? = null
        fun getInstance(): QuizViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: QuizViewModelFactory(Injection.provideRepository())
            }.also { instance = it }
    }
}