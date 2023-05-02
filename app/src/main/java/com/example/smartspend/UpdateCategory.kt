package com.example.smartspend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.smartspend.databinding.ActivityUpdatecategoryBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateCategory : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatecategoryBinding
    private lateinit var database: DatabaseReference
    private lateinit var categoryID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatecategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the categoryID value passed from previous activity
        categoryID = intent.getStringExtra("categoryID") ?: ""

        binding.btnUpdate.setOnClickListener{

            val category = binding.edtupdateCategory.text.toString()
            val description = binding.edtUpdateDescription.text.toString()

            updateData(category, description)

        }

        // Retrieve the data of the category with the specified categoryID and display it in the EditText views
        database = FirebaseDatabase.getInstance().getReference("Categories")
        database.child(categoryID).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val category = snapshot.child("category").getValue(String::class.java)
                val description = snapshot.child("description").getValue(String::class.java)
                binding.edtupdateCategory.setText(category)
                binding.edtUpdateDescription.setText(description)
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Failed to retrieve category data",Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateData(category: String, description: String) {

        database = FirebaseDatabase.getInstance().getReference("Categories")

        // Update the data in the categoryID node
        val cat = mapOf<String,String>(
            "category" to category,
            "description" to description
        )

        database.child(categoryID).updateChildren(cat).addOnSuccessListener {

            binding.edtupdateCategory.text.clear()
            binding.edtUpdateDescription.text.clear()

            Toast.makeText(this,"Successfully updated",Toast.LENGTH_SHORT).show()

        }.addOnFailureListener{
            Toast.makeText(this,"Failed to update",Toast.LENGTH_SHORT).show()
        }
    }
}
