package com.frxcl.wastesmart.di

import android.content.Context
import com.frxcl.wastesmart.data.Repository
import com.frxcl.wastesmart.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }
}