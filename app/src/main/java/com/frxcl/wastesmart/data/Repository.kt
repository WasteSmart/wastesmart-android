package com.frxcl.wastesmart.data

import com.frxcl.wastesmart.data.remote.response.EncyclopediaNonOrganicResponse
import com.frxcl.wastesmart.data.remote.response.EncyclopediaResponse
import com.frxcl.wastesmart.data.remote.response.FunFactsResponse
import com.frxcl.wastesmart.data.remote.response.EncyclopediaOrganicResponse
import com.frxcl.wastesmart.data.remote.response.EncyclopediaToxicResponse
import com.frxcl.wastesmart.data.remote.response.QuizResponse
import com.frxcl.wastesmart.data.remote.response.TipsResponse
import com.frxcl.wastesmart.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository private constructor(
    private val apiService: ApiService
) {
    fun getTips(callback: (TipsResponse) -> Unit) {
        val client = apiService.getTips()
        client.enqueue(object : Callback<TipsResponse> {
            override fun onResponse(call: Call<TipsResponse>, response: Response<TipsResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                }
            }
            override fun onFailure(call: Call<TipsResponse>, t: Throwable) {

            }
        })
    }

    fun getQuiz(callback: (QuizResponse) -> Unit) {
        val client = apiService.getQuiz()
        client.enqueue(object : Callback<QuizResponse> {
            override fun onResponse(call: Call<QuizResponse>, response: Response<QuizResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                }
            }
            override fun onFailure(call: Call<QuizResponse>, t: Throwable) {

            }
        })
    }

    fun getEncyclopedia(callback: (EncyclopediaResponse) -> Unit) {
        val client = apiService.getEncyclopedia()
        client.enqueue(object : Callback<EncyclopediaResponse> {
            override fun onResponse(call: Call<EncyclopediaResponse>, response: Response<EncyclopediaResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                }
            }
            override fun onFailure(call: Call<EncyclopediaResponse>, t: Throwable) {

            }
        })
    }

    fun getFunFacts(callback: (FunFactsResponse?) -> Unit) {
        val client = apiService.getFunFacts()
        client.enqueue(object : Callback<FunFactsResponse> {
            override fun onResponse(call: Call<FunFactsResponse>, response: Response<FunFactsResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                }
            }
            override fun onFailure(call: Call<FunFactsResponse>, t: Throwable) {

            }
        })
    }

    fun getEncyclopediaOrganic(callback: (EncyclopediaOrganicResponse) -> Unit) {
        val client = apiService.getEncyclopediaOrganic()
        client.enqueue(object : Callback<EncyclopediaOrganicResponse> {
            override fun onResponse(call: Call<EncyclopediaOrganicResponse>, response: Response<EncyclopediaOrganicResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                }
            }
            override fun onFailure(call: Call<EncyclopediaOrganicResponse>, t: Throwable) {

            }
        })
    }

    fun getEncyclopediaNonOrganic(callback: (EncyclopediaNonOrganicResponse) -> Unit) {
        val client = apiService.getEncyclopediaNonOrganic()
        client.enqueue(object : Callback<EncyclopediaNonOrganicResponse> {
            override fun onResponse(call: Call<EncyclopediaNonOrganicResponse>, response: Response<EncyclopediaNonOrganicResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                }
            }
            override fun onFailure(call: Call<EncyclopediaNonOrganicResponse>, t: Throwable) {

            }
        })
    }

    fun getEncyclopediaToxic(callback: (EncyclopediaToxicResponse) -> Unit) {
        val client = apiService.getEncyclopediaToxic()
        client.enqueue(object : Callback<EncyclopediaToxicResponse> {
            override fun onResponse(call: Call<EncyclopediaToxicResponse>, response: Response<EncyclopediaToxicResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                }
            }
            override fun onFailure(call: Call<EncyclopediaToxicResponse>, t: Throwable) {

            }
        })
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}