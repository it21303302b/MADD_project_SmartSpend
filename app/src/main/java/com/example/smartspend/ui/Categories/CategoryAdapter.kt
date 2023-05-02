package com.example.smartspend.ui.Categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartspend.Category
import com.example.smartspend.R

class CategoryAdapter(private val categoryList: ArrayList<Category>) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.categoryitem,parent,false)

        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = categoryList[position]

        holder.category.text = currentitem.category
        holder.description.text = currentitem.description
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val category : TextView = itemView.findViewById(R.id.tvCategory)
        val description : TextView = itemView.findViewById(R.id.tvDescription)

    }

    fun getCategoryList(): ArrayList<Category> {
        return categoryList
    }
}
