package com.frxcl.wastesmart.data.remote.retrofit

import com.frxcl.wastesmart.data.remote.response.EncyclopediaNonOrganicResponse
import com.frxcl.wastesmart.data.remote.response.EncyclopediaOrganicResponse
import com.frxcl.wastesmart.data.remote.response.EncyclopediaResponse
import com.frxcl.wastesmart.data.remote.response.EncyclopediaToxicResponse
import com.frxcl.wastesmart.data.remote.response.FunFactsResponse
import com.frxcl.wastesmart.data.remote.response.PredictResponse
import com.frxcl.wastesmart.data.remote.response.QuizResponse
import com.frxcl.wastesmart.data.remote.response.TipsResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/predicts")
    suspend fun uploadWasteImage(
        @Part file: MultipartBody.Part
    ): PredictResponse

    @GET("quiz")
    fun getQuiz(): Call<QuizResponse>

    @GET("tips")
    fun getTips(): Call<TipsResponse>

    @GET("encyclopedia")
    fun getEncyclopedia(): Call<EncyclopediaResponse>

    @GET("funfacts")
    fun getFunFacts(): Call<FunFactsResponse>

    @GET("encyclopedia/organik")
    fun getEncyclopediaOrganic(): Call<EncyclopediaOrganicResponse>

    @GET("encyclopedia/anorganik")
    fun getEncyclopediaNonOrganic(): Call<EncyclopediaNonOrganicResponse>

    @GET("encyclopedia/b3")
    fun getEncyclopediaToxic(): Call<EncyclopediaToxicResponse>
}