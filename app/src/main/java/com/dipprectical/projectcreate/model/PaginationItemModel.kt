package com.dipprectical.projectcreate.model

import com.google.gson.annotations.SerializedName

data class PaginationItemModel(
    @field:SerializedName("has_more")
    val hasMore: Boolean? = null,

    @field:SerializedName("users")
    val users: ArrayList<UserItemModel>? = null
)