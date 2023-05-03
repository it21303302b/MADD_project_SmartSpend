package com.example.smartspend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase


class UpdateCategory : AppCompatActivity() {

        private lateinit var tvShowCurrentCategory: TextView
        private lateinit var tvCurrentDescription: TextView
        private lateinit var tvCatIdshow: TextView
        private lateinit var btnUpdate : Button


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_update_category)

            initView()
            setValuesToViews()

            btnUpdate.setOnClickListener{
                openUpdateDialog(
                    intent.getStringExtra("categoryId").toString(),
                    intent.getStringExtra("categoryName").toString()
                )
            }
        }

        private fun initView() {
            tvCatIdshow = findViewById(R.id.tvCatIdshow)
            tvShowCurrentCategory = findViewById(R.id.tvShowCurrentCategory)
            tvCurrentDescription = findViewById(R.id.tvCurrentDescription)

            btnUpdate = findViewById(R.id.btnUpdate)
        }

        private fun setValuesToViews() {
            tvCatIdshow.text = intent.getStringExtra("categoryId")
            tvShowCurrentCategory.text = intent.getStringExtra("categoryName")
            tvCurrentDescription.text = intent.getStringExtra("description")
        }

        private fun openUpdateDialog(
            categoryId: String,
            categoryName: String
        ){
            val mDialog = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val mDialogView = inflater.inflate(R.layout.update_dialog,null)

            mDialog.setView(mDialogView)

            val etCatName = mDialogView.findViewById<EditText>(R.id.edtCatName)
            val etDescription = mDialogView.findViewById<EditText>(R.id.edtDescription)

            val btnUpdateCategory = mDialogView.findViewById<Button>(R.id.updateCategorybtn)


            etCatName.setText(intent.getStringExtra("categoryName").toString())
            etDescription.setText(intent.getStringExtra("description").toString())

            mDialog.setTitle("Updating $categoryName Record")

            val alertDialog = mDialog.create()
            alertDialog.show()

            btnUpdateCategory.setOnClickListener{
                updateCategoryData(
                    categoryId,
                    etCatName.text.toString(),
                    etDescription.text.toString()
                )
                Toast.makeText(applicationContext,"Category updated",Toast.LENGTH_SHORT).show()

                //setting updated data to textViews
                tvShowCurrentCategory.text = etCatName.text.toString()
                tvCurrentDescription.text = etDescription.text.toString()

                alertDialog.dismiss()
            }

        }

    private fun updateCategoryData(
        id:String,
        catName: String,
        description: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("CategoryDB").child(id)
        val catInfo = CategoryModel(id,catName,description)
        dbRef.setValue(catInfo)
    }

}
