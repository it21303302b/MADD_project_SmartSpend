package com.example.smartspend

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartspend.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Set up the click listener for the "Sign up" button
        binding.TVsignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Set up the click listener for the "Sign in" button
        binding.btnLogin.setOnClickListener {
            val email = binding.emailET.text.toString()
            val pass = binding.passET.text.toString()

            // Validate email and password fields
            if (email.isEmpty()) {
                binding.emailET.error = "Email is required"
                binding.emailET.requestFocus()
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                binding.passET.error = "Password is required"
                binding.passET.requestFocus()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailET.error = "Invalid email address"
                binding.emailET.requestFocus()
                return@setOnClickListener
            }

            // Sign in with email and password if fields are valid
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    val errorMessage = "Failed to sign in. Please check your email and password and try again."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

        }

        // Set up the click listener for the "Forgot password" text view
        binding.forgotPasswordTv.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
