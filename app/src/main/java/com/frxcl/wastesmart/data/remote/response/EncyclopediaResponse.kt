package com.frxcl.wastesmart.data.remote.response

import com.google.gson.annotations.SerializedName

data class EncyclopediaResponse(

	@field:SerializedName("waste")
	val waste: Waste? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class Waste(

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("general_description")
	val generalDescription: String? = null
)
