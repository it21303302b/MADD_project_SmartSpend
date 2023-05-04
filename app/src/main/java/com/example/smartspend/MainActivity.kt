package com.example.smartspend

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.smartspend.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    fun loginActivity(view: View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun registerActivity(view: View){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun userProfileActivity(view: View){
        val intent = Intent(this, UserProfileActivity::class.java)
        startActivity(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Database Integration
        val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView


        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_categories, R.id.navigation_reminders
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    fun addReminder(view: View) {
        val intent = Intent(this, AddReminder::class.java)
        startActivity(intent)

    }
    fun updateReminder(view: View) {
        val intent = Intent(this, ReminderDetailsActivity::class.java)
        startActivity(intent)
    }
    fun addExpence(view: View) {
        val intent = Intent(this, AddExpences::class.java)
        startActivity(intent)
    }

    fun UpdateExpence(view: View) {
        val intent = Intent(this, UpdateExpence::class.java)
        startActivity(intent)
    }

    fun addCategory(view: View) {
        val intent = Intent(this, AddCategory::class.java)
        startActivity(intent)
    }

    fun updateCategory(view: View) {
        val intent = Intent(this, UpdateCategory::class.java)
        startActivity(intent)

    }


}