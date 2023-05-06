package com.example.smartspend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import java.util.*

class AddExpences : AppCompatActivity() {
    private lateinit var etExpenceName: EditText
    private lateinit var etExpenceDescription: EditText
    private lateinit var etExpenceAmout: EditText
    private lateinit var btnAddExpence: Button

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expences)

        etExpenceName = findViewById(R.id.edtExpencename)
        etExpenceDescription = findViewById(R.id.edt_Expencedescription)
        etExpenceAmout = findViewById(R.id.edtExpenceAmount)
        btnAddExpence = findViewById(R.id.btnAddExpences) // Add this line to initialize btnAddCategory

        etExpenceAmout.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL


        dbRef = FirebaseDatabase.getInstance().getReference("ExpencesDB")

        btnAddExpence.setOnClickListener{
            saveExpenceData()
        }

    }

    private fun saveExpenceData(){

        //getting values
        val expenceName = etExpenceName.text.toString()
        val expencedescription = etExpenceDescription.text.toString()
        val expenceAmount = etExpenceAmout.text.toString()

        if(expenceName.isEmpty()){
            etExpenceName.error = "Please enter category name"
        }
        if(expencedescription.isEmpty()){
            etExpenceDescription.error = "Please enter description"
        }
        if(expenceAmount.isEmpty()){
            etExpenceAmout.error = "Please enter Amount"
            return
        }

        val ExpenceId= dbRef.push().key!!

        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.time)
        val currentTime = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(calendar.time)

        val expence = ExpenceModel(ExpenceId,expenceName,expencedescription,expenceAmount,currentDate,currentTime)

        dbRef.child(ExpenceId).setValue(expence).addOnCompleteListener{
            Toast.makeText(this,"Data inserted", Toast.LENGTH_SHORT).show()

            etExpenceName.text.clear()
            etExpenceDescription.text.clear()
            etExpenceAmout.text.clear()

        }.addOnFailureListener { err ->
            Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_SHORT).show()
        }

    }

}