package com.example.smartspend

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartspend.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")

        binding.TVAlreadyRegistered.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnRegister.setOnClickListener {
            val name = binding.ETName.text.toString()
            val email = binding.emailET.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            // Get selected gender from radio button
            var gender = ""
            val selectedGenderId = binding.radioGroup.checkedRadioButtonId
            if (selectedGenderId != -1) {
                val selectedGender = findViewById<RadioButton>(selectedGenderId)
                gender = selectedGender.text.toString()
            }

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            val userProfileChangeRequest = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()
                            user?.updateProfile(userProfileChangeRequest)

                            // Update the user's name and gender in the database
                            val userId = user?.uid
                            if (userId != null) {
                                val userMap = hashMapOf<String, String>(
                                    "name" to name,
                                    "gender" to gender
                                )
                                usersRef.child(userId).setValue(userMap)
                                    .addOnSuccessListener {
                                        runOnUiThread {
                                            Toast.makeText(this, "Name and gender uploaded successfully!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener {
                                        runOnUiThread {
                                            Toast.makeText(this, "Failed to upload name and gender", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                            }

                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }

        val alreadyLoginView = findViewById<TextView>(R.id.TV_alreadyLogin)

        alreadyLoginView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

}
