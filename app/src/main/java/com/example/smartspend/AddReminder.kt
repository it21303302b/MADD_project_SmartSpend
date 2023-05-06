package com.example.smartspend

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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


    @SuppressLint("SimpleDateFormat")
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
        val amount = reminderAmount.toIntOrNull()
        if ((amount == null) || (amount < 0)) {
            remAmount.error = "Please Enter a Valid Amount (a positive integer)"
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date: Date = try {
            dateFormat.parse(reminderDate) as Date
        } catch (e: ParseException) {
            remDate.error = "Please Enter a Valid Date (dd/MM/yyyy)"
            return
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