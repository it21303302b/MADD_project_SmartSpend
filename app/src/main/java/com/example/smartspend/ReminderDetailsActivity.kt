package com.example.smartspend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.smartspend.ui.Reminders.RemindersFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ReminderDetailsActivity : AppCompatActivity() {

    private lateinit var tvRemDes: TextView
    private lateinit var tvRemDate: TextView
    private lateinit var tvRemAmount: TextView
    private lateinit var tvRemType: TextView
    private lateinit var btnUpdateRem: Button
    private lateinit var btnDeleteRem: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_details)

        initView()
        setValuesToViews()

        btnUpdateRem.setOnClickListener{
            openUpdateDialog(
                intent.getStringExtra("remId").toString(),
                intent.getStringExtra("reminderDes").toString()
            )
        }

        btnDeleteRem.setOnClickListener{
            deleteRecord(
                intent.getStringExtra("remId").toString()
            )
        }
    }

//    private fun deleteRecord(
//        rId: String
//    ){
//        val dbRef = FirebaseDatabase.getInstance().getReference("Reminders").child(rId)
//        val mTask = dbRef.removeValue()
//
//        mTask.addOnSuccessListener {
//            Toast.makeText(this,"Reminder Data Deleted", Toast.LENGTH_LONG).show()
//
//            val intent = Intent(this, RemindersFragment::class.java)
//            finish()
//            startActivity(intent)
//        }.addOnFailureListener{error->
//
//            Toast.makeText(this,"Deleting Err ${error.message}",Toast.LENGTH_LONG).show()
//
//        }
//    }

    private fun deleteRecord(rId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val dbRef = FirebaseDatabase.getInstance().getReference("Reminders").child(rId)
            val mTask = dbRef.removeValue()

            // Create a confirmation dialog
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to delete this reminder?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    // Delete the record
                    mTask.addOnSuccessListener {
                        Toast.makeText(this, "Reminder Data Deleted", Toast.LENGTH_LONG).show()
                        finish() // Close this activity and return to the previous activity
                    }.addOnFailureListener { error ->
                        Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
                    }
                }
                .setNegativeButton("No") { dialog, _ -> dialog.cancel() }

            // Show the dialog
            val alert = builder.create()
            alert.show()
        }else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_LONG).show()
        }

    }




    private fun initView() {
        tvRemDes = findViewById(R.id.TV_ReminderDescription)
        tvRemDate = findViewById(R.id.TV_ReminderDate)
        tvRemAmount = findViewById(R.id.TV_ReminderAmount)
        tvRemType = findViewById(R.id.TV_ReminderType)
        btnUpdateRem = findViewById(R.id.btn_UpdateRemData)
        btnDeleteRem = findViewById(R.id.btn_DeleteRemData)

    }

    private fun setValuesToViews() {

        tvRemDes.text = intent.getStringExtra("reminderDes")
        tvRemDate.text = intent.getStringExtra("reminderDate")
        tvRemAmount.text = intent.getStringExtra("reminderAmount")
        tvRemType.text = intent.getStringExtra("reminderType")


    }

    private fun openUpdateDialog(
        remId: String,
        reminderDes: String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.reminder_update_dialog,null)


        mDialog.setView(mDialogView)

        val updateRemDes = mDialogView.findViewById<EditText>(R.id.ET_UpdateRemDes)
        val updateRemDate = mDialogView.findViewById<EditText>(R.id.ET_UpdateRemDate)
        val updateRemAmount = mDialogView.findViewById<EditText>(R.id.ET_UpdateRemAmount)
        val btnUpdateReminder = mDialogView.findViewById<Button>(R.id.btn_UpdateRem)
        //val updateRemType = mDialogView.findViewById<Spinner>(R.id.UpdateReminder_spinner)

        val updateRemType = mDialogView.findViewById<Spinner>(R.id.UpdateReminder_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.reminder_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            updateRemType.adapter = adapter

        }

        //val btnUpdateData = mDialogView.findViewById<Button>(R.id.btn_UpdateRem)

        updateRemDes.setText(intent.getStringExtra("reminderDes").toString())
        updateRemDate.setText(intent.getStringExtra("reminderDate").toString())
        updateRemAmount.setText(intent.getStringExtra("reminderAmount").toString())


        val reminderType = intent.getStringExtra("reminderType")
        val reminderTypeArray = resources.getStringArray(R.array.reminder_type)
        val reminderTypeIndex = reminderTypeArray.indexOf(reminderType)
        updateRemType.setSelection(reminderTypeIndex)

        mDialog.setTitle("Updating $reminderDes Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateReminder.setOnClickListener{
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                updateRemData(
                    userId,
                    remId,
                    updateRemDes.text.toString(),
                    updateRemDate.text.toString(),
                    updateRemAmount.text.toString(),
                    updateRemType.selectedItem.toString()
                )
            }


            Toast.makeText(applicationContext,"Reminder Data Updated",Toast.LENGTH_LONG).show()

            //Updating TextViews with new Data
            tvRemDes.text = updateRemDes.text.toString()
            tvRemDate.text = updateRemDate.text.toString()
            tvRemAmount.text = updateRemAmount.text.toString()
            tvRemType.text = updateRemType.selectedItem.toString()

            alertDialog.dismiss()
        }


    }

    private fun updateRemData(
        userId: String,
        rId:String,
        rDes:String,
        rDate:String,
        rAmount:String,
        rType:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Reminders").child(userId).child(rId)
        val remInfo = ReminderModel(rId, rDes, rDate, rAmount, rType)
        dbRef.setValue(remInfo)
    }
}