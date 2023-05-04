package com.example.smartspend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class AddReminder : AppCompatActivity() {

    private lateinit var remDescription: EditText
    private lateinit var remDate: EditText
    private lateinit var remAmount: EditText
    private lateinit var remType: Spinner
    private lateinit var btnAddRem : Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)


        remDescription = findViewById(R.id.ET_AddReminderDescription)
        remDate = findViewById(R.id.ET_AddReminderDate)
        remAmount = findViewById(R.id.ET_ReminderAmount)

        remType = findViewById(R.id.AddReminder_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.reminder_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            remType.adapter = adapter

        }

        btnAddRem = findViewById(R.id.btn_AddReminder)
        dbRef = FirebaseDatabase.getInstance().getReference("Reminders")

        btnAddRem.setOnClickListener {
            AddReminderData()
        }

    }

    private fun AddReminderData() {

        //getting values
        val reminderDes =  remDescription.text.toString()
        val reminderDate =  remDate.text.toString()
        val reminderAmount = remAmount.text.toString()
        val reminderType =  remType.selectedItem.toString()

        if(reminderDes.isEmpty()) {
            remDescription.error = "Please Enter Reminder Description"
        }

        if(reminderDate.isEmpty()) {
            remDate.error = "Please Enter Reminder Date"
        }

        if(reminderAmount.isEmpty()) {
            remAmount.error = "Please Enter Reminder Amount"
        }


        if (reminderType.isEmpty()) {
            val errorTextView: TextView = findViewById(R.id.error_text_view)
            errorTextView.setText(R.string.error_message_reminder_type)
            errorTextView.setTextColor(ContextCompat.getColor(this, R.color.red))
        }

        val remId = dbRef.push().key!!

        val reminder = ReminderModel(remId, reminderDes, reminderDate, reminderAmount,reminderType)

        dbRef.child(remId).setValue(reminder)
            .addOnCompleteListener {
                Toast.makeText(this,"Reminder Added Successfully", Toast.LENGTH_LONG).show()

                remDescription.text.clear()
                remDate.text.clear()
                remAmount.text.clear()
                remType.setSelection(0)

            }.addOnFailureListener{ err ->
                Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_LONG).show()
            }


    }
}