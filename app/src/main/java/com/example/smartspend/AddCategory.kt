package com.example.smartspend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.smartspend.databinding.ActivityAddcategoryBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddCategory : AppCompatActivity() {

    private lateinit var binding: ActivityAddcategoryBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddcategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("Categories")

        binding.btnAddCategory.setOnClickListener {
            val category = binding.edtCategory.text.toString()
            val description = binding.edtDescription.text.toString()

            val categoryID = database.push().key // Generate a unique category ID

            val categoryData = categoryID?.let { it1 -> Category(category, description, it1) }

            database.child(categoryID!!).setValue(categoryData).addOnSuccessListener {
                binding.edtCategory.text.clear()
                binding.edtDescription.text.clear()

                Toast.makeText(this, "Successfully saved category", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
            }

        }
    }
}
