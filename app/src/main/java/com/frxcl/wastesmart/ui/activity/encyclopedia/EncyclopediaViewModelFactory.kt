package com.frxcl.wastesmart.ui.activity.encyclopedia

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.frxcl.wastesmart.data.Repository
import com.frxcl.wastesmart.di.Injection

class EncyclopediaViewModelFactory private constructor(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EncyclopediaViewModel::class.java)) {
            return EncyclopediaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: EncyclopediaViewModelFactory? = null
        fun getInstance(context: Context): EncyclopediaViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: EncyclopediaViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}