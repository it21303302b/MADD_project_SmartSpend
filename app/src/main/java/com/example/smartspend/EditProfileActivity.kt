package com.example.smartspend

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    private lateinit var nameEditText: EditText
    private lateinit var maleRadioButton: RadioButton
    private lateinit var femaleRadioButton: RadioButton
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userRef = database.getReference("users").child(auth.currentUser!!.uid)

        nameEditText = findViewById(R.id.ET_name)
        maleRadioButton = findViewById(R.id.radioButtonMale)
        femaleRadioButton = findViewById(R.id.radioButtonFemale)
        saveButton = findViewById(R.id.updateProfile)

        // Fetch user's details from Firebase database and populate the UI
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    nameEditText.setText(user.name)
                    if (user.gender == "male") {
                        maleRadioButton.isChecked = true
                    } else if (user.gender == "female") {
                        femaleRadioButton.isChecked = true
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database read failure
            }
        })

        // Handle save button click
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val gender = if (maleRadioButton.isChecked) "male" else "female"

            if (name.isEmpty()) {
                nameEditText.error = "Name is required"
                nameEditText.requestFocus()
                return@setOnClickListener
            }

            // Update user's details in Firebase database
            userRef.updateChildren(mapOf(
                "name" to name,
                "gender" to gender
            )).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()

                    // Update the UI with the new values
                    nameEditText.setText(name)
                    if (gender == "male") {
                        maleRadioButton.isChecked = true
                    } else {
                        femaleRadioButton.isChecked = true
                    }

                    // Set the result code and finish the activity
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    // Handle profile update failure
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }

            }


        }
    }
}
