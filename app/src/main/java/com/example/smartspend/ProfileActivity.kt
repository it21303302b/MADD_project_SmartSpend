package com.example.smartspend


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userEmailView: TextView
    private lateinit var nameView: TextView
    private lateinit var ageView: TextView
    private lateinit var occupationView: TextView
    private lateinit var updatePasswordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser

        userEmailView = findViewById(R.id.TV_userEmailView)
        nameView = findViewById(R.id.TV_UserNameView)
        ageView = findViewById(R.id.TV_UserAgeView)
        occupationView = findViewById(R.id.TV_UserOccupationView)
        updatePasswordButton = findViewById<Button>(R.id.btn_update_password)

        if (currentUser != null) {
            val userEmail = currentUser.email
            userEmailView.text = userEmail

            val database = FirebaseDatabase.getInstance()
            val usersRef = database.getReference("users")

            usersRef.child(currentUser.uid).get().addOnSuccessListener { dataSnapshot ->
                val user = dataSnapshot.getValue(User::class.java)
                if (user != null) {
                    nameView.text = user.name
                    ageView.text = user.age
                    occupationView.text = user.occupation
                }
            }.addOnFailureListener {
                // Handle database read failure
            }

            // Handle "Update Password" button click
            updatePasswordButton.setOnClickListener {
                val intent = Intent(this, UpdatePasswordActivity::class.java)
                startActivity(intent)
            }
        }

        val deleteAccountButton = findViewById<Button>(R.id.btn_delete_account)

        deleteAccountButton.setOnClickListener {
            val user = auth.currentUser
            if (user != null) {
                // Show confirmation dialog
                AlertDialog.Builder(this)
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete your account?")
                    .setPositiveButton("Yes") { _, _ ->
                        // Delete account and log out user
                        user.delete().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                auth.signOut()
                                Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            } else {
                                // Handle account deletion failure
                                Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show()
                                Log.e("ProfileActivity", "Failed to delete account", task.exception)
                            }
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }
}
