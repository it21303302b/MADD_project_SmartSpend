package com.example.smartspend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddCategory : AppCompatActivity() {

    private lateinit var etCategoryName: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnAddCategory: Button

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        etCategoryName = findViewById(R.id.edtAddcategory)
        etDescription = findViewById(R.id.edtAddDescription)
        btnAddCategory = findViewById(R.id.submitCategory) // Add this line to initialize btnAddCategory

        dbRef = FirebaseDatabase.getInstance().getReference("CategoryDB")

        btnAddCategory.setOnClickListener{
            saveCategoryData()
        }

    }

    private fun saveCategoryData(){

        //getting values

        val categoryName = etCategoryName.text.toString()
        val description = etDescription.text.toString()

        if(categoryName.isEmpty()){
            etCategoryName.error = "Please enter category name"
        }
        if(description.isEmpty()){
            etDescription.error = "Please enter description"
            return
        }

        val categoryId= dbRef.push().key!!

        val category = CategoryModel(categoryId,categoryName,description)

        dbRef.child(categoryId).setValue(category).addOnCompleteListener{
            Toast.makeText(this,"Data inserted",Toast.LENGTH_SHORT).show()

            etCategoryName.text.clear()
            etDescription.text.clear()

        }.addOnFailureListener { err ->
            Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_SHORT).show()
        }

    }
}