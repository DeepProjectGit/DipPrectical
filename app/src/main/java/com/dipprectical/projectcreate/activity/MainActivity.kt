package com.dipprectical.projectcreate.activity

import android.graphics.Movie
import com.dipprectical.projectcreate.R
import com.dipprectical.projectcreate.base.BaseActivity
import com.dipprectical.projectcreate.model.PaginationItemModel
import com.dipprectical.projectcreate.network.ApiClient
import com.dipprectical.projectcreate.network.RestResponce
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity:BaseActivity() {
   // val CurrentPage=
    override fun mSetView(): Int = R.layout.activity_main
    override fun mInit() {

    }
    fun callApi(){
        val call: Call<RestResponce<PaginationItemModel>> = ApiClient.getClient.getUserDetail("1","10")
        call.enqueue(object:Callback<RestResponce<PaginationItemModel>>{
            override fun onResponse(
                call: Call<RestResponce<PaginationItemModel>>,
                response: Response<RestResponce<PaginationItemModel>>
            ) {

            }

            override fun onFailure(call: Call<RestResponce<PaginationItemModel>>, t: Throwable) {

            }

        })

    }
}