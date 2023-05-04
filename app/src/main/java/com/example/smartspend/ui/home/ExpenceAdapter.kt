package com.example.smartspend.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartspend.CategoryModel
import com.example.smartspend.ExpenceModel
import com.example.smartspend.R
import com.example.smartspend.adapters.CategoryAdapter

class ExpenceAdapter (private val expList: ArrayList<ExpenceModel>):
    RecyclerView.Adapter<ExpenceAdapter.ViewHolder>() {

    private lateinit var mListner: OnItemClickListner

    interface OnItemClickListner{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListner(clickListner: OnItemClickListner){
        mListner = clickListner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.expences_list, parent,false)   //inflating the card with data
        return ViewHolder(itemView , mListner)
    }

    override fun onBindViewHolder(holder: ExpenceAdapter.ViewHolder, position: Int) {
        val currentExpence = expList[position]
        holder.tvExpenceName.text = currentExpence.expenceName
        holder.tvExpValue.text = currentExpence.expenceAmount
    }

    override fun getItemCount(): Int {
        return expList.size
    }

    class ViewHolder(itemView: View, clickListner: OnItemClickListner) : RecyclerView.ViewHolder(itemView){

        val tvExpenceName : TextView = itemView.findViewById(R.id.tvExpenceName)
        val tvExpValue : TextView = itemView.findViewById(R.id.tvExpenceValue)

        init {
            itemView.setOnClickListener {
                clickListner.onItemClick(adapterPosition)
            }
        }

    }


}