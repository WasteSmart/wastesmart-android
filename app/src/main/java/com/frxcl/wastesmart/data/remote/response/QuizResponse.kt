package com.frxcl.wastesmart.data.remote.response

import com.google.gson.annotations.SerializedName

data class QuizResponse(

	@field:SerializedName("quiz")
	val quiz: List<QuizItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class Answer(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("text")
	val text: String? = null
)

data class QuizItem(

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("answer")
	val answer: Answer? = null,

	@field:SerializedName("options")
	val options: List<OptionsItem?>? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class OptionsItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("text")
	val text: String? = null
)
