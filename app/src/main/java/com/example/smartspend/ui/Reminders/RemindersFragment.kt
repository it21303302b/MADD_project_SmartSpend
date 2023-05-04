package com.example.smartspend.ui.Reminders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartspend.R
import com.example.smartspend.RemAdapter
import com.example.smartspend.ReminderDetailsActivity
import com.example.smartspend.ReminderModel
import com.example.smartspend.databinding.FragmentRemindersBinding
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class RemindersFragment : Fragment() {

    private lateinit var remRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var remList: ArrayList<ReminderModel>
    private lateinit var dbRef: DatabaseReference

    private var _binding: FragmentRemindersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val reminderViewModel =
            ViewModelProvider(this).get(RemindersViewModel::class.java)

        _binding = FragmentRemindersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textReminders
        reminderViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        remRecyclerView = root.findViewById(R.id.RV_ReminderData)
        remRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        remRecyclerView.setHasFixedSize(true)
        tvLoadingData = root.findViewById(R.id.TV_LoadingData)

        remList = arrayListOf<ReminderModel>()

        getReminderData()

        return root
    }

    private fun getReminderData() {
        remRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val dbRef = FirebaseDatabase.getInstance().getReference("Reminders")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                remList.clear()
                if(snapshot.exists()){
                    for(remSnap in snapshot.children){
                        val remData = remSnap.getValue(ReminderModel::class.java)
                        remList.add(remData!!)
                    }
                    val mAdapter = RemAdapter(remList)
                    remRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : RemAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(requireActivity(), ReminderDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("remId", remList[position].remId)
                            intent.putExtra("reminderDes", remList[position].reminderDes)
                            intent.putExtra("reminderDate", remList[position].reminderDate)
                            intent.putExtra("reminderAmount", remList[position].reminderAmount)
                            intent.putExtra("reminderType", remList[position].reminderType)
                            startActivity(intent)

                        }


                    })

                    remRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}