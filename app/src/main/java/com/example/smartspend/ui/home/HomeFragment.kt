package com.example.smartspend.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartspend.*
import com.example.smartspend.R
import com.example.smartspend.adapters.CategoryAdapter
import com.example.smartspend.databinding.FragmentHomeBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var expRecyclerView: RecyclerView
    private lateinit var expList: ArrayList<ExpenceModel>
    private lateinit var dbRef: DatabaseReference

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        expRecyclerView = root.findViewById(R.id.expencesListRV)
        expRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        expRecyclerView.setHasFixedSize(true)

        expList = arrayListOf<ExpenceModel>()

        getExpenceData()

        return root
    }

    private fun getExpenceData(){

        expRecyclerView.visibility = View.GONE

        dbRef = FirebaseDatabase.getInstance().getReference("ExpencesDB")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                expList.clear()
                var totalExpence = 0.0 // Initialize totalExpence to 0.0
                if (snapshot.exists()){
                    for (catSnap in snapshot.children){
                        val expData = catSnap.getValue(ExpenceModel::class.java)
                        expList.add(expData!!)
                        expData?.expenceAmount?.toDoubleOrNull()?.let { totalExpence += it } // Add the expenceAmount to totalExpence
                    }
                    val mAdapter = ExpenceAdapter(expList)
                    expRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListner(object : ExpenceAdapter.OnItemClickListner{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(requireActivity(), UpdateExpence::class.java)

                            //put extra
                            intent.putExtra("expenceId",expList[position].expenceId)
                            intent.putExtra("expenceName",expList[position].expenceName)
                            intent.putExtra("expenceDescription",expList[position].expenceDescription)
                            intent.putExtra("expenceAmount",expList[position].expenceAmount)
                            startActivity(intent)
                        }
                    })

                    expRecyclerView.visibility = View.VISIBLE
                }
                binding.ecpenceTotal.text = String.format("%.2f", totalExpence) // Set the text of ecpenceTotal to the formatted value of totalExpence
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