package com.example.smartspend.ui.Categories

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
import com.example.smartspend.CategoryModel
import com.example.smartspend.R
import com.example.smartspend.UpdateCategory
import com.example.smartspend.adapters.CategoryAdapter
import com.example.smartspend.databinding.FragmentCategoriesBinding
import com.example.smartspend.databinding.FragmentRemindersBinding
import com.google.firebase.database.*

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null

    private lateinit var catRecyclerView: RecyclerView
    private lateinit var catList: ArrayList<CategoryModel>
    private lateinit var dbRef: DatabaseReference


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val categoryViewModel =
            ViewModelProvider(this).get(CategoryViewModel::class.java)

        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCategories
        categoryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        catRecyclerView = root.findViewById(R.id.categoryListRV)
        catRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        catRecyclerView.setHasFixedSize(true)

        catList = arrayListOf<CategoryModel>()

        getCategoryData()


        return root
    }

    private fun getCategoryData(){
        catRecyclerView.visibility = View.GONE

        dbRef = FirebaseDatabase.getInstance().getReference("CategoryDB")

        dbRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                catList.clear()
                if (snapshot.exists()){
                    for (catSnap in snapshot.children){
                        val catData = catSnap.getValue(CategoryModel::class.java)
                        catList.add(catData!!)
                    }
                    val mAdapter = CategoryAdapter(catList)
                    catRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListner(object : CategoryAdapter.OnItemClickListner{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(requireActivity(), UpdateCategory::class.java)


                            //put extra
                            intent.putExtra("categoryId",catList[position].categoryId)
                            intent.putExtra("categoryName",catList[position].categoryName)
                            intent.putExtra("description",catList[position].description)
                            startActivity(intent)


                        }


                    })

                    catRecyclerView.visibility = View.VISIBLE
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