package com.dipprectical.projectcreate.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val BASE_URL="http://sd2-hiring.herokuapp.com/"
    val getClient:ApiInterface get() {
        val gson=GsonBuilder().setLenient().create()
        val retrofit=Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(ApiInterface::class.java)
    }
}