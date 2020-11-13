package com.dipprectical.projectcreate.base

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dipprectical.projectcreate.R
import java.text.FieldPosition

abstract class BaseAdaptor<T>(private  val activity:Activity,private var itemsList:ArrayList<T>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    abstract fun BindData(holder:RecyclerView.ViewHolder?, itemModel:T,position:Int)
    abstract fun setItemLayout():Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view=LayoutInflater.from(activity).inflate(setItemLayout(),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        BindData(holder,itemsList[position],position)
    }

    override fun getItemCount(): Int =itemsList.size

    internal  class ViewHolder(itemview: View):RecyclerView.ViewHolder(itemview)

}