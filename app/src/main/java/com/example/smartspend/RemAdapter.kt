package com.example.smartspend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RemAdapter(private val remList: ArrayList<ReminderModel>) : RecyclerView.Adapter<RemAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rem_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val currentRem = remList[position]
       holder.tvRemName.text = currentRem.reminderDes
    }


    override fun getItemCount(): Int {

        return remList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val tvRemName : TextView = itemView.findViewById(R.id.TV_RemName)

        init{
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }


    }

    init {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Reminders")
        val expList = ArrayList<ReminderModel>()
        dbRef.orderByChild("userId").equalTo(userId).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                expList.clear()
                for (expenseSnapshot in snapshot.children) {
                    val expense = expenseSnapshot.getValue(ReminderModel::class.java)
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