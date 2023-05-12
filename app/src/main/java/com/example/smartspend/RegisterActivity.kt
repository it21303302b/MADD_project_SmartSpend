package com.example.smartspend

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
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
            val name = binding.ETName.text.toString().trim()
            val email = binding.emailET.text.toString().trim()
            val pass = binding.passET.text.toString().trim()
            val confirmPass = binding.confirmPassEt.text.toString().trim()

            // Get selected gender from radio button
            var gender = ""
            val selectedGenderId = binding.radioGroup.checkedRadioButtonId
            if (selectedGenderId != -1) {
                val selectedGender = findViewById<RadioButton>(selectedGenderId)
                gender = selectedGender.text.toString()
            }

            // Validations for the fields
            if (name.isEmpty()) {
                binding.ETName.error = "Name is required"
                binding.ETName.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.emailET.error = "Email is required"
                binding.emailET.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailET.error = "Invalid email address"
                binding.emailET.requestFocus()
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                binding.passET.error = "Password is required"
                binding.passET.requestFocus()
                return@setOnClickListener
            }

            if (pass.length < 6) {
                binding.passET.error = "Password must be at least 6 characters long"
                binding.passET.requestFocus()
                return@setOnClickListener
            }

            if (confirmPass.isEmpty()) {
                binding.confirmPassEt.error = "Please confirm your password"
                binding.confirmPassEt.requestFocus()
                return@setOnClickListener
            }

            if (pass != confirmPass) {
                binding.confirmPassEt.error = "Passwords do not match"
                binding.confirmPassEt.requestFocus()
                return@setOnClickListener
            }

            // If all fields are valid, create user in Firebase Auth
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

        }

        val alreadyLoginView = findViewById<TextView>(R.id.TV_alreadyLogin)

        alreadyLoginView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

}
