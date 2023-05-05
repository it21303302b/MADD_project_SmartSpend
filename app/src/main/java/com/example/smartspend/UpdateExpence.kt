package com.example.smartspend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class UpdateExpence : AppCompatActivity() {

    private lateinit var tvShowCurrentExpence: TextView
    private lateinit var tvExpDescription: TextView
    private lateinit var tvExpIdshow: TextView
    private lateinit var tvExpAmounVal: TextView
    private lateinit var btnUpdateExppg : Button
    private lateinit var btnDeleteExp : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_expence)

        initView()
        setValuesToViews()

        btnUpdateExppg.setOnClickListener{
            openUpdateDialog(
                intent.getStringExtra("expenceId").toString(),
                intent.getStringExtra("expenceName").toString()
            )
        }

        btnDeleteExp.setOnClickListener{
            deleteRecord(
                intent.getStringExtra("expenceId").toString()
            )
        }

    }

    private fun deleteRecord(rId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val dbRef = FirebaseDatabase.getInstance().getReference("ExpencesDB").child(currentUser.uid).child(rId)
            val mTask = dbRef.removeValue()

            mTask.addOnSuccessListener {
                Toast.makeText(this, "Expense deleted", Toast.LENGTH_LONG).show()
                finish() // Close this activity and return to the previous activity
            }.addOnFailureListener { error ->
                Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        tvExpIdshow = findViewById(R.id.tvExpIdshow)
        tvShowCurrentExpence = findViewById(R.id.tvShowCurrentExpence)
        tvExpDescription = findViewById(R.id.tvExpDescription)
        tvExpAmounVal = findViewById(R.id.tvExpAmounVal)

        btnUpdateExppg = findViewById(R.id.btnUpdateExppg)
        btnDeleteExp = findViewById(R.id.btnDeleteExp)
    }


    private fun setValuesToViews() {
        tvExpIdshow.text = intent.getStringExtra("expenceId")
        tvShowCurrentExpence.text = intent.getStringExtra("expenceName")
        tvExpDescription.text = intent.getStringExtra("expenceDescription")
        tvExpAmounVal.text = intent.getStringExtra("expenceAmount")
    }

    private fun openUpdateDialog(expenceId: String, expenceName: String) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog_expeces,null)

        mDialog.setView(mDialogView)

        val etExpName = mDialogView.findViewById<EditText>(R.id.edtExpName)
        val etExpDescription = mDialogView.findViewById<EditText>(R.id.edtExpDescription)
        val etAmount = mDialogView.findViewById<EditText>(R.id.edtExpAmount)

        val btnUpdateCategory = mDialogView.findViewById<Button>(R.id.updateExpencesbtn)


        etExpName.setText(intent.getStringExtra("expenceName").toString())
        etExpDescription.setText(intent.getStringExtra("expenceDescription").toString())
        etAmount.setText(intent.getStringExtra("expenceAmount").toString())

        mDialog.setTitle("Updating $expenceName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateCategory.setOnClickListener{
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val expenceId = intent.getStringExtra("expenceId").toString()
            if (userId != null) {
                updateExpenceData(
                    userId,
                    expenceId,
                    etExpName.text.toString(),
                    etExpDescription.text.toString(),
                    etAmount.text.toString()
                )
            }
            Toast.makeText(applicationContext,"Expense updated",Toast.LENGTH_SHORT).show()

            // setting updated data to textViews
            tvShowCurrentExpence.text = etExpName.text.toString()
            tvExpDescription.text = etExpDescription.text.toString()
            tvExpAmounVal.text = etAmount.text.toString()

            alertDialog.dismiss()
        }

    }

    private fun updateExpenceData(
        userId: String,
        id: String,
        expName: String,
        expdescription: String,
        expAmount: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("ExpencesDB").child(userId).child(id)
        val calendar = Calendar.getInstance()
        val expInfo = ExpenceModel(
            id,
            expName,
            expdescription,
            expAmount,
            DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.time)
        )
        dbRef.setValue(expInfo)
            .addOnSuccessListener {
                Toast.makeText(this, "Expense updated successfully", Toast.LENGTH_LONG).show()
                // Update the displayed data
                tvShowCurrentExpence.text = expName
                tvExpDescription.text = expdescription
                tvExpAmounVal.text = expAmount
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Updating expense failed: ${error.message}", Toast.LENGTH_LONG).show()
            }
    }



}