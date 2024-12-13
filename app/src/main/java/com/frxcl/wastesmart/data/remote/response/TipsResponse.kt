package com.frxcl.wastesmart.data.remote.response

import com.google.gson.annotations.SerializedName

data class TipsResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("tips")
	val tips: Tips? = null
)

data class Tips(

	@field:SerializedName("baterai")
	val baterai: String? = null,

	@field:SerializedName("sisa_makanan")
	val sisaMakanan: String? = null,

	@field:SerializedName("kardus")
	val kardus: String? = null,

	@field:SerializedName("baju")
	val baju: String? = null,

	@field:SerializedName("sepatu")
	val sepatu: String? = null,

	@field:SerializedName("logam")
	val logam: String? = null,

	@field:SerializedName("kertas")
	val kertas: String? = null,

	@field:SerializedName("kaca")
	val kaca: String? = null,

	@field:SerializedName("plastik")
	val plastik: String? = null
)
