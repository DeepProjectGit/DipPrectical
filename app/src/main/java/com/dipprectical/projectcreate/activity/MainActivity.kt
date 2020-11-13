package com.dipprectical.projectcreate.activity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dipprectical.projectcreate.R
import com.dipprectical.projectcreate.base.BaseActivity
import com.dipprectical.projectcreate.base.BaseAdaptor
import com.dipprectical.projectcreate.model.PaginationItemModel
import com.dipprectical.projectcreate.model.UserItemModel
import com.dipprectical.projectcreate.network.ApiClient
import com.dipprectical.projectcreate.network.RestResponce
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_user.view.*
import org.lucasr.twowayview.widget.SpannableGridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MainActivity:BaseActivity() {
    val CurrentPage:Int=1
    val isLast=false;
    lateinit var userList:ArrayList<UserItemModel>
    var userItemAdaptor:BaseAdaptor<UserItemModel>?=null
    override fun mSetView(): Int = R.layout.activity_main
    override fun mInit() {
        userList= ArrayList()
        setUserAdaptor(userList)
        Log.e("inRespons1", "getData")
        callApi()
    }
    fun callApi(){
        val call: Call<RestResponce<PaginationItemModel>> = ApiClient.getClient.getUserDetail(
            CurrentPage.toString(),
            "10"
        )
        call.enqueue(object : Callback<RestResponce<PaginationItemModel>> {
            override fun onResponse(
                call: Call<RestResponce<PaginationItemModel>>,
                response: Response<RestResponce<PaginationItemModel>>
            ) {
                Log.e("inRespons", "getData")
                if (response.isSuccessful) {
                    userList.addAll(response.body()!!.data!!.users!!)
                    userItemAdaptor!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<RestResponce<PaginationItemModel>>, t: Throwable) {
                Log.e("inRespons1", "Fail")
            }
        })

    }

    fun setUserAdaptor(userList: ArrayList<UserItemModel>){
        userItemAdaptor=object :BaseAdaptor<UserItemModel>(
            activity = this@MainActivity,
            itemsList = userList
        ){
            override fun BindData(
                holder: RecyclerView.ViewHolder?,
                itemModel: UserItemModel,
                position: Int
            ) {
                holder!!.itemView.tvUserName.text=userList[position].name
                Glide.with(this@MainActivity).load(userList[position].image).into(holder.itemView.ivUser)
                Log.e("inData", userList[position].items!!.size.toString())
                holder.itemView.gallery.adapter=GalleryAdapter(userList[position].items!!)

            }

            override fun setItemLayout(): Int = R.layout.item_user
        }
        rvUserData.adapter=userItemAdaptor
        rvUserData.layoutManager=LinearLayoutManager(this@MainActivity)
        rvUserData.itemAnimator=DefaultItemAnimator()
    }



    private inner class GalleryAdapter constructor(val titlesArray: ArrayList<String>) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
            Log.e("Crate","in")
            val view: View = LayoutInflater.from(this@MainActivity).inflate(R.layout.item_images, parent, false)
            return GalleryViewHolder(view)
        }

        override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
            loadImage(holder.ivImage,titlesArray[position])
            val layoutParams:SpannableGridLayoutManager.LayoutParams =
                holder.itemView.getLayoutParams() as SpannableGridLayoutManager.LayoutParams
            var colSpan = 1
            var rowSpan = 1

            if(titlesArray.size%2==0){
                colSpan=2
                rowSpan=2
            }else{
                if(position==0){
                    colSpan=1
                    rowSpan=1
                }else{
                    if(position%2==0){
                        colSpan=2
                        rowSpan=2
                    }else{
                        colSpan=1
                        rowSpan=1
                    }
                }
            }

            layoutParams.colSpan = colSpan
            layoutParams.rowSpan = rowSpan
            holder.itemView.setLayoutParams(layoutParams)


        }

        private fun loadImage(destination: ImageView,strUrl:String) {
            Glide.with(this@MainActivity)
                .load(strUrl)
                .into(destination)
        }

        override fun getItemCount(): Int {
            return titlesArray.size
        }

        inner class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view){
            val ivImage=view.findViewById<ImageView>(R.id.imageview_gallery_item)
        }
    }


}