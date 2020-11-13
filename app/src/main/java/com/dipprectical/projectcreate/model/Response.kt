package com.dipprectical.projectcreate.model

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: Any? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class UsersItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("items")
	val items: List<String?>? = null
)

data class Data(

	@field:SerializedName("has_more")
	val hasMore: Boolean? = null,

	@field:SerializedName("users")
	val users: List<UsersItem?>? = null
)
