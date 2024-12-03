package com.frxcl.wastesmart.data.remote.retrofit

import com.frxcl.wastesmart.data.remote.response.EncyclopediaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEncylopedia(
        @Query("encyclopedia") active: Int
    ) : Call<EncyclopediaResponse>
}