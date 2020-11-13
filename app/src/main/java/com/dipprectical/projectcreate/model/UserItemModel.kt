package com.dipprectical.projectcreate.model

import com.google.gson.annotations.SerializedName

data class UserItemModel(
    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("items")
    val items: ArrayList<String>? = null
)