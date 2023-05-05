package com.example.smartspend.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartspend.ExpenceModel
import com.example.smartspend.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ExpenceAdapter(private val expenceList: List<ExpenceModel>, private var mListener: OnItemClickListener? = null) : RecyclerView.Adapter<ExpenceAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.expences_list, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentExpence = expenceList[position]
        holder.tvExpenceName.text = currentExpence.expenceName
        holder.tvExpValue.text = currentExpence.expenceAmount
        holder.tvExpenceDate.text = currentExpence.date
    }

    override fun getItemCount(): Int {
        return expenceList.size
    }

    inner class ViewHolder(itemView: View, private val clickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {

        val tvExpenceName: TextView = itemView.findViewById(R.id.tvExpenceName)
        val tvExpValue: TextView = itemView.findViewById(R.id.tvExpenceValue)
        val tvExpenceDate: TextView = itemView.findViewById(R.id.tvExpenceDate)

        init {
            itemView.setOnClickListener {
                clickListener?.onItemClick(adapterPosition)
            }
        }
    }

    init {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("ExpencesDB")
        val expList = ArrayList<ExpenceModel>()
        dbRef.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                expList.clear()
                for (expenseSnapshot in snapshot.children) {
                    val expense = expenseSnapshot.getValue(ExpenceModel::class.java)
                    expense?.let {
                        expList.add(it)
                    }
                }
                notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}

