package com.dipprectical.projectcreate.network

import com.dipprectical.projectcreate.model.PaginationItemModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("api/users")
    fun getUserDetail(
        @Query("offset") offset: String?,
        @Query("limit") limit: String?
    ): Call<RestResponce<PaginationItemModel>>
}