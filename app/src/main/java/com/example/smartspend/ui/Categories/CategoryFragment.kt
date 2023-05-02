package com.example.smartspend.ui.Categories

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
import com.example.smartspend.Category
import com.example.smartspend.R
import com.example.smartspend.databinding.FragmentCategoriesBinding
import com.example.smartspend.databinding.FragmentRemindersBinding
import com.google.firebase.database.*

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<Category>



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

        userRecyclerView = root.findViewById(R.id.categoryList)
        userRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        userRecyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf<Category>()
        getCategoryData()

        return root
    }


    private fun getCategoryData() {
        dbref = FirebaseDatabase.getInstance().getReference("Categories")

        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    for (categorySnapshot in snapshot.children){

                        val category = categorySnapshot.getValue(Category::class.java)

                        userArrayList.add(category!!)

                    }

                    userRecyclerView.adapter = CategoryAdapter(userArrayList)
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