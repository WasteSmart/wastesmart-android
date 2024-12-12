package com.frxcl.wastesmart.di

import com.frxcl.wastesmart.data.Repository
import com.frxcl.wastesmart.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }
}