package com.frxcl.wastesmart.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("result")
	val result: List<ResultItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class ResultItem(

	@field:SerializedName("percentage")
	val percentage: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
