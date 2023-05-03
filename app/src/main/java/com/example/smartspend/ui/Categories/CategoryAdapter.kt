package com.example.smartspend.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartspend.CategoryModel
import com.example.smartspend.R

class CategoryAdapter(private val catList: ArrayList<CategoryModel>):
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_list, parent,false)   //inflating the card with data
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        val currentCategory = catList[position]
        holder.tvCatName.text = currentCategory.categoryName
        holder.tvCatDescription.text = currentCategory.description
    }

    override fun getItemCount(): Int {
        return catList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val tvCatName : TextView = itemView.findViewById(R.id.tvCategory)
        val tvCatDescription : TextView = itemView.findViewById(R.id.tvDescription)

    }


}