package com.example.smartspend

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmNewPasswordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        auth = FirebaseAuth.getInstance()
        val updatePasswordButton = findViewById<Button>(R.id.btn_update_password)
        currentPasswordEditText = findViewById(R.id.ET_currentPassword)
        newPasswordEditText = findViewById(R.id.ET_newPassword)
        confirmNewPasswordEditText = findViewById(R.id.ET_confirmNewPassword)

        updatePasswordButton.setOnClickListener {
            val currentPassword = currentPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val confirmNewPassword = confirmNewPasswordEditText.text.toString()

            if (TextUtils.isEmpty(currentPassword)) {
                currentPasswordEditText.error = "Please enter your current password"
                currentPasswordEditText.requestFocus()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(newPassword)) {
                newPasswordEditText.error = "Please enter your new password"
                newPasswordEditText.requestFocus()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(confirmNewPassword)) {
                confirmNewPasswordEditText.error = "Please confirm your new password"
                confirmNewPasswordEditText.requestFocus()
                return@setOnClickListener
            }

            if (newPassword != confirmNewPassword) {
                confirmNewPasswordEditText.error = "Passwords do not match"
                confirmNewPasswordEditText.requestFocus()
                return@setOnClickListener
            }

            val currentUser = auth.currentUser
            if (currentUser != null) {
                val credential = EmailAuthProvider.getCredential(currentUser.email!!, currentPassword)
                currentUser.reauthenticate(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        currentUser.updatePassword(newPassword).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this, "Failed to update password. Please try again later.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        currentPasswordEditText.error = "Incorrect password"
                        currentPasswordEditText.requestFocus()
                    }
                }
            }
        }
    }
}
