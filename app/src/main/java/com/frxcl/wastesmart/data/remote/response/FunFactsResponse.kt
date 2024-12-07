package com.frxcl.wastesmart.data.remote.response

import com.google.gson.annotations.SerializedName

data class FunFactsResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("funfacts")
	val funfacts: List<String?>? = null
)
