package com.frxcl.wastesmart.data.remote.response

import com.google.gson.annotations.SerializedName

data class EncyclopediaNonOrganicResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("type_of_waste")
	val typeOfWaste: TypeOfWasteNonOrganic? = null
)

data class TypeOfWasteNonOrganic(

	@field:SerializedName("examples")
	val examples: List<ExamplesItemNonOrganic?>? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("how_to_manage")
	val howToManage: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class ExamplesItemNonOrganic(

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)
