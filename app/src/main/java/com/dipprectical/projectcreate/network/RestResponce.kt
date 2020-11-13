package com.dipprectical.projectcreate.network

import com.google.gson.annotations.SerializedName

data class RestResponce<T>(
    @field:SerializedName("data")
    val data: T? = null,

    @field:SerializedName("message")
    val message: Any? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)