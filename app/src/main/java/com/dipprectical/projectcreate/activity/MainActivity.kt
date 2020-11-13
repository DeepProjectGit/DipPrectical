package com.dipprectical.projectcreate.activity

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.dipprectical.projectcreate.R
import com.dipprectical.projectcreate.base.BaseActivity
import com.dipprectical.projectcreate.base.BaseAdaptor
import com.dipprectical.projectcreate.model.PaginationItemModel
import com.dipprectical.projectcreate.model.UserItemModel
import com.dipprectical.projectcreate.network.ApiClient
import com.dipprectical.projectcreate.network.RestResponce
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_images.view.*
import kotlinx.android.synthetic.main.item_user.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity:BaseActivity() {
    var CurrentPage:Int=1
    var isLast=true;
    lateinit var userList:ArrayList<UserItemModel>
    var userItemAdaptor:BaseAdaptor<UserItemModel>?=null
    private var isLoading = false
    override fun mSetView(): Int = R.layout.activity_main
    override fun mInit() {
        userList= ArrayList()
        setUserAdaptor(userList)
        rvUserData.layoutManager=LinearLayoutManager(this@MainActivity)
        rvUserData.itemAnimator=DefaultItemAnimator()
        rvUserData.setNestedScrollingEnabled(false)
        Log.e("inRespons1", "getData")

        if(isNetworkAvailable()){
            pbCenterVertical.visibility=View.VISIBLE
            callApi(false)
        }else{
            pbCenterVertical.visibility=View.GONE
            Toast.makeText(this@MainActivity, "Please Connect Network", Toast.LENGTH_SHORT).show()
        }

        Swipe.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                CurrentPage = 1
                Swipe.setRefreshing(false);
                Swipe.canChildScrollUp()
                scrollView.pageScroll(0)
                if (isNetworkAvailable()) {
                    userList.clear()
                    pbCenterVertical.visibility = View.VISIBLE
                    callApi(false)
                } else {
                    pbCenterVertical.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Please Connect Network", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        })

        scrollView.getViewTreeObserver().addOnScrollChangedListener(OnScrollChangedListener {
            val view = scrollView.getChildAt(scrollView.getChildCount() - 1) as View
            val diff: Int = view.bottom - (scrollView.getHeight() + scrollView
                .getScrollY())
            if (diff == 0 && isLast) {
                rlBottom.visibility = View.VISIBLE
                CurrentPage = CurrentPage + 1
                callApi(true)
            }
        })
    }

    fun callApi(isFristPage: Boolean) {
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
                    rlBottom.visibility = View.GONE
                    isLast = response.body()!!.data!!.hasMore!!
                    pbCenterVertical.visibility = View.GONE
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
                setImagesAdaptor(userList[position].items!!, holder.itemView.rvUserImageData)


              /*  holder.itemView.gallery.adapter=GalleryAdapter(userList[position].items!!)
                holder.itemView.gallery.orientation=TwoWayLayoutManager.Orientation.VERTICAL
                val divider = ContextCompat.getDrawable(this@MainActivity, R.drawable.divider)
                holder.itemView.gallery.addItemDecoration(DividerItemDecoration(divider))
*/
            }

            override fun setItemLayout(): Int = R.layout.item_user
        }
        rvUserData.adapter=userItemAdaptor


        /*scrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->

            val nestedScrollView = checkNotNull(v){
                return@setOnScrollChangeListener
            }

            val lastChild = nestedScrollView.getChildAt(nestedScrollView.childCount - 1)

            if (lastChild != null) {

                if ((scrollY >= (lastChild.measuredHeight - nestedScrollView.measuredHeight)) && scrollY > oldScrollY && !isLoading && !isLast) {
                    CurrentPage=CurrentPage+1
                    callApi()
                    //get more items
                }
            }
        }*/
       /* */
    }

    fun setImagesAdaptor(userList: ArrayList<String>, rvUserImageData: RecyclerView){
        val userImageItemAdaptor=object :BaseAdaptor<String>(
            activity = this@MainActivity,
            itemsList = userList
        ){
            override fun BindData(
                holder: RecyclerView.ViewHolder?,
                itemModel: String,
                position: Int
            ) {

                Glide.with(this@MainActivity)
                    .load(userList[position])
                    .into(holder!!.itemView.imageview_gallery_item)


            }

            override fun setItemLayout(): Int = R.layout.item_images
        }

        rvUserImageData.adapter=userImageItemAdaptor
        if(userList.size%2==0){
            val layoutManager = GridLayoutManager(this, 2 ,GridLayoutManager.VERTICAL,
                false)
            rvUserImageData.layoutManager=layoutManager
        }else{
            val layoutManager = GridLayoutManager(this, 2 ,GridLayoutManager.VERTICAL,
                false)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                   if(position==0){
                       return 2
                   }else{
                       return 1
                   }

                }
            }
            rvUserImageData.layoutManager=layoutManager
        }



        rvUserImageData.itemAnimator=DefaultItemAnimator()
    }



   /* private inner class GalleryAdapter constructor(val titlesArray: ArrayList<String>) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
            Log.e("Crate", "in")
            val view: View = LayoutInflater.from(this@MainActivity).inflate(
                R.layout.item_images,
                parent,
                false
            )
            return GalleryViewHolder(view)
        }

        override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
            loadImage(holder.ivImage, titlesArray[position])
            val layoutParams:SpannedGridLayoutManager.LayoutParams = holder.itemView.getLayoutParams() as SpannableGridLayoutManager.LayoutParams
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
                        holder.itemView.layoutParams = SpannedGridLayoutManager.
                    }
                }
            }

            layoutParams.colSpan = colSpan
            layoutParams.rowSpan = rowSpan
            holder.itemView.setLayoutParams(layoutParams)


        }

        private fun loadImage(destination: ImageView, strUrl: String) {
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
    }*/

    @SuppressLint("NewApi")
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


}